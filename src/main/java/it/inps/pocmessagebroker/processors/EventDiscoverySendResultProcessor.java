package it.inps.pocmessagebroker.processors;

import it.inps.pocmessagebroker.model.EventoArcaPendingMessage;
import it.inps.pocmessagebroker.repository.ApplicationeRepository;
import it.inps.pocmessagebroker.repository.EventoArcaPendingRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class EventDiscoverySendResultProcessor implements Processor {

    private final EventoArcaPendingRepository eventoArcaPendingRepository;
    private final ApplicationeRepository applicationeRepository;

    @Autowired
    public EventDiscoverySendResultProcessor(EventoArcaPendingRepository eventoArcaPendingRepository, ApplicationeRepository applicationeRepository) {
        this.eventoArcaPendingRepository = eventoArcaPendingRepository;
        this.applicationeRepository = applicationeRepository;
    }

    @Override
    public void process(Exchange exchange) {
        log.info("* EVENTI ARCA - INVIO MESSAGGI ARTEMISMQ PER NUOVI EVENTI DA PROCESSARE");
        var result = new ArrayList<String>(5000);
        var inviati = new AtomicInteger(0);
        var listEventPending = this.eventoArcaPendingRepository.findAllByStatoIs(0);

        log.info("     trovati {} eventi pending da inviare", listEventPending.size());

        listEventPending.forEach(eventoArcaPending -> {
            var applicazione = this.applicationeRepository.findById(eventoArcaPending.getIdApplicazione());
            var message = EventoArcaPendingMessage.builder()
                    .arcaKey(eventoArcaPending.getArcaKey())
                    .applicazione(applicazione.orElseThrow(RuntimeException::new));
            result.add(message.build().toJSON());
            inviati.incrementAndGet();

        });
        log.info("messaggi preparati per invio ad artemis : {}", inviati.get());
        exchange.getIn().setBody(result);
    }
}
