package it.inps.pocmessagebroker.processors;

import it.inps.pocmessagebroker.domain.Applicazione;
import it.inps.pocmessagebroker.domain.EventoArcaPending;
import it.inps.pocmessagebroker.jms.MessageSender;
import it.inps.pocmessagebroker.repository.ApplicazioneRepository;
import it.inps.pocmessagebroker.repository.EventoArcaPendingRepository;
import it.inps.pocmessagebroker.utils.ReadResource;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EventDetailsSendToQueueProcessor implements Processor {

    private final ApplicazioneRepository applicazioneRepository;
    private final EventoArcaPendingRepository eventoArcaPendingRepository;
    private final MessageSender messageSender;
    private final ReadResource readResource;

    @Autowired
    public EventDetailsSendToQueueProcessor(ApplicazioneRepository applicazioneRepository, EventoArcaPendingRepository eventoArcaPendingRepository, MessageSender messageSender, ReadResource readResource) {
        this.applicazioneRepository = applicazioneRepository;
        this.eventoArcaPendingRepository = eventoArcaPendingRepository;
        this.messageSender = messageSender;
        this.readResource = readResource;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        EventoArcaPending eventoPending = (EventoArcaPending)exchange.getIn().getHeader("eventPending");
        Applicazione applicazione = this.applicazioneRepository.findById(eventoPending.getIdApplicazione()).orElseThrow(RuntimeException::new);
/*
        List<String> codiciEvento = this.eventoArcaPendingRepository.findAllByArcaKeyAndIdApplicazione(eventoPending.getArcaKey(), applicazione.getId())
                .stream()
                .map(EventoArcaPending::getCodiceEvento)
                .collect(Collectors.toList());

        String requestString = readResource.getResourceAsString("queue_message.xml");
        StringBuilder sb = new StringBuilder();
        codiciEvento.forEach(codiceEvento -> sb.append("<Evento>").append(codiceEvento).append("</Evento>"));
        String msgBody = String.format(requestString, sb.toString(), eventoPending.getXml());
*/
        //this.messageSender.send(msgBody, applicazione.getQueue());
    }
}
