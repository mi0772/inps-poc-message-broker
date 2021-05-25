package it.inps.pocmessagebroker.processors;

import it.inps.pocmessagebroker.config.EventDiscoveryRouteConfig;

import it.inps.pocmessagebroker.domain.Applicazione;
import it.inps.pocmessagebroker.model.EventoArca;
import it.inps.pocmessagebroker.repository.ApplicazioneRepository;
import it.inps.pocmessagebroker.wsclients.ArcaNotificaEventiWSClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class EventDiscoveryProcessor implements Processor {

    private final EventDiscoveryRouteConfig config;
    private final ArcaNotificaEventiWSClient arcaNotificaEventiWSClient;
    private final ApplicazioneRepository applicazioneRepository;

    @Autowired
    public EventDiscoveryProcessor(EventDiscoveryRouteConfig config, ArcaNotificaEventiWSClient arcaNotificaEventiWSClient, ApplicazioneRepository applicazioneRepository) {
        this.config = config;
        this.arcaNotificaEventiWSClient = arcaNotificaEventiWSClient;
        this.applicazioneRepository = applicazioneRepository;
    }

    @Override
    public void process(Exchange exchange) {
        Map<Applicazione, List<EventoArca>> result = new HashMap<>(2000);

        log.info("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ");
        log.info("fase 1/3 : lettura dei nuovi eventi da ARCA");

        this.applicazioneRepository.findAll()
                .forEach(applicazione -> {
                    log.info("{}: interrogazione WS in corso ...", applicazione.getAppName());
                    var nuoviEventi = this.arcaNotificaEventiWSClient.getEventi(this.config.getWsEndpoint(), applicazione);
                    result.put(applicazione, nuoviEventi);
                    log.info("{}: il servizio ha restituito nr: {} eventi", applicazione.getAppName(), nuoviEventi.size());
                });


        exchange.setProperty("events", result);
    }
}
