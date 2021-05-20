package it.inps.pocmessagebroker.processors;

import it.inps.pocmessagebroker.config.EventDiscoveryRouteConfig;
import it.inps.pocmessagebroker.model.Applicazione;
import it.inps.pocmessagebroker.model.EventoArca;
import it.inps.pocmessagebroker.repository.ApplicationeRepository;
import it.inps.pocmessagebroker.wsclients.ArcaNotificaEventiWSClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class EventDiscoveryProcessor implements Processor {

    private final EventDiscoveryRouteConfig config;
    private final ArcaNotificaEventiWSClient arcaNotificaEventiWSClient;
    private final ApplicationeRepository applicationeRepository;

    @Autowired
    public EventDiscoveryProcessor(EventDiscoveryRouteConfig config, ArcaNotificaEventiWSClient arcaNotificaEventiWSClient, ApplicationeRepository applicationeRepository) {
        this.config = config;
        this.arcaNotificaEventiWSClient = arcaNotificaEventiWSClient;
        this.applicationeRepository = applicationeRepository;
    }

    @Override
    public void process(Exchange exchange) {
        log.info("ws url endpoint for discovery found: {}", this.config.getWsEndpoint());
        Map<Applicazione, List<EventoArca>> result = new HashMap<>(2000);

        this.applicationeRepository.findAll()
                .forEach(applicazione -> {
                    log.info("{}", applicazione);
                    result.put(applicazione, this.arcaNotificaEventiWSClient.getCustomerInfo(this.config.getWsEndpoint(), applicazione));
                });

        exchange.setProperty("events", result);
    }
}
