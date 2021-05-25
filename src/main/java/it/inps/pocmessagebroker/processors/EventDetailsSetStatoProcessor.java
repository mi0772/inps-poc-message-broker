package it.inps.pocmessagebroker.processors;

import com.google.gson.Gson;
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

        //INVIARE QUESTO COME DESTINAZIONE, per il momento viene inviato come STRING sulla coda destinazione
        var eventoDetails = (EventoArcaDetails)exchange.getProperty("eventoDetails");


        var eventoPending = new Gson().fromJson(exchange.getIn().getBody().toString(), EventoArcaPendingMessage.class);
        var ev = this.eventoArcaPendingRepository.findByArcaKeyAndIdApplicazione(eventoPending.getArcaKey(), eventoPending.getApplicazione().getId()).orElseThrow(RuntimeException::new);
        ev.setStato(2);
        this.eventoArcaPendingRepository.save(ev);
        log.info("    - MEMORIZZATO EVENTO CON STATO 2 IN TABELLA, MARCATO PER L'INVIO COME EVENTO COMPLETATO: {}", eventoDetails);

        var application = this.applicazioneRepository.findById(ev.getIdApplicazione()).orElseThrow(RuntimeException::new);

        exchange.getIn().setBody(eventoDetails.getXml());
        exchange.getIn().setHeader("applicationQueue", "jms:queue:" + application.getQueue());
    }
}
