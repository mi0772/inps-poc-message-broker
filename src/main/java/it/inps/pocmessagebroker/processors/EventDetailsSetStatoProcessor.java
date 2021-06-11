package it.inps.pocmessagebroker.processors;

import com.google.gson.Gson;
import it.inps.pocmessagebroker.domain.Applicazione;
import it.inps.pocmessagebroker.domain.EventoArcaPending;
import it.inps.pocmessagebroker.model.EventoArcaDetails;
import it.inps.pocmessagebroker.model.EventoArcaPendingMessage;
import it.inps.pocmessagebroker.repository.ApplicazioneRepository;
import it.inps.pocmessagebroker.repository.EventoArcaPendingRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EventDetailsSetStatoProcessor implements Processor {

    private final EventoArcaPendingRepository eventoArcaPendingRepository;
    private final ApplicazioneRepository applicazioneRepository;

    @Autowired
    public EventDetailsSetStatoProcessor(EventoArcaPendingRepository eventoArcaPendingRepository, ApplicazioneRepository applicazioneRepository) {
        this.eventoArcaPendingRepository = eventoArcaPendingRepository;
        this.applicazioneRepository = applicazioneRepository;
    }

    @Override
    public void process(Exchange exchange) throws Exception {

        EventoArcaPending eventoArcaPending = (EventoArcaPending)exchange.getIn().getHeader("eventPending");
        Long idApplicazione = (Long)exchange.getIn().getHeader("idApplicazione");

        log.info("    - MEMORIZZATO EVENTO CON STATO 2 IN TABELLA, MARCATO PER L'INVIO COME EVENTO COMPLETATO: {}", eventoArcaPending.getArcaKey());

        EventoArcaPending eventPending = this.eventoArcaPendingRepository.findTopByArcaKeyAndIdApplicazione(eventoArcaPending.getArcaKey(), idApplicazione).orElseThrow(RuntimeException::new);
        eventPending.setStato(2);
        this.eventoArcaPendingRepository.save(eventPending);
    }
}
