package it.inps.pocmessagebroker.processors;

import it.inps.pocmessagebroker.config.EventDiscoveryRouteConfig;
import it.inps.pocmessagebroker.wsclients.ArcaNotificaEventiWSClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EventDiscoveryProcessor implements Processor {

    private final EventDiscoveryRouteConfig config;
    private final ArcaNotificaEventiWSClient arcaNotificaEventiWSClient;

    @Autowired
    public EventDiscoveryProcessor(EventDiscoveryRouteConfig config, ArcaNotificaEventiWSClient arcaNotificaEventiWSClient) {
        this.config = config;
        this.arcaNotificaEventiWSClient = arcaNotificaEventiWSClient;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        log.info("ws url endpoint for discovery found: {}", this.config.getWsEndpoint());
        var arcaEvents = this.arcaNotificaEventiWSClient.getCustomerInfo(this.config.getWsEndpoint());
        exchange.setProperty("events", arcaEvents);
    }
}
