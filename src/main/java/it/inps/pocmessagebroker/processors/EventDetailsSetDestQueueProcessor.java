package it.inps.pocmessagebroker.processors;

import it.inps.pocmessagebroker.domain.Applicazione;
import it.inps.pocmessagebroker.domain.EventoArcaPending;
import it.inps.pocmessagebroker.repository.ApplicazioneRepository;
import it.inps.pocmessagebroker.repository.EventoArcaPendingRepository;
import it.inps.pocmessagebroker.utils.ReadResource;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class EventDetailsSetDestQueueProcessor implements Processor {
    private final ApplicazioneRepository applicazioneRepository;
    private final EventoArcaPendingRepository eventoArcaPendingRepository;
    private final ReadResource readResource;


    @Autowired
    public EventDetailsSetDestQueueProcessor(ApplicazioneRepository applicazioneRepository, EventoArcaPendingRepository eventoArcaPendingRepository, ReadResource readResource) {
        this.applicazioneRepository = applicazioneRepository;
        this.eventoArcaPendingRepository = eventoArcaPendingRepository;
        this.readResource = readResource;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        EventDetailsGetProcessor.Result result = (EventDetailsGetProcessor.Result)exchange.getIn().getBody();
        Applicazione applicazione = applicazioneRepository.findById(result.getIdApplicazione()).orElseThrow(RuntimeException::new);

        EventoArcaPending eventPending = this.eventoArcaPendingRepository.findTopByArcaKeyAndIdApplicazione(result.getEventoArcaPending().getArcaKey(), applicazione.getId()).orElseThrow(RuntimeException::new);
        eventPending.setStato(1);
        this.eventoArcaPendingRepository.save(eventPending);

        exchange.getIn().setHeader("applicationQueue", "jms:queue:" + applicazione.getQueue());
        exchange.getIn().setHeader("eventPending", eventPending);
        exchange.getIn().setHeader("idApplicazione", applicazione.getId());


        //exchange.getIn().setBody();
        exchange.getIn().setBody(createMessage(eventPending, applicazione, result.getEventoArcaPending().getXml()));
    }

    private String createMessage(EventoArcaPending eventoPending, Applicazione applicazione, String xml) throws Exception {
        List<String> codiciEvento = this.eventoArcaPendingRepository.findAllByArcaKeyAndIdApplicazione(eventoPending.getArcaKey(), applicazione.getId())
                .stream()
                .map(EventoArcaPending::getCodiceEvento)
                .collect(Collectors.toList());

        String requestString = readResource.getResourceAsString("queue_message.xml");
        StringBuilder sb = new StringBuilder();
        codiciEvento.forEach(codiceEvento -> sb.append("<Evento>").append(codiceEvento).append("</Evento>"));
        return String.format(requestString, sb, xml);
    }
}
