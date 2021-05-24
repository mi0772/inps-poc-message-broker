package it.inps.pocmessagebroker.routes;

import it.inps.pocmessagebroker.config.EventDetailsRouteConfig;
import it.inps.pocmessagebroker.processors.EventDetailsGetProcessor;
import it.inps.pocmessagebroker.processors.EventDetailsSetStatoProcessor;
import it.inps.pocmessagebroker.wsclients.ArcaIntraWSClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EventDetailsRoute extends RouteBuilder {

    private final EventDetailsRouteConfig config;
    private final EventDetailsGetProcessor eventDetailsGetProcessor;
    private final EventDetailsSetStatoProcessor eventDetailsSetStatoProcessor;

    @Autowired
    public EventDetailsRoute(CamelContext context, EventDetailsRouteConfig config, ArcaIntraWSClient wsClient, EventDetailsGetProcessor eventDetailsGetProcessor, EventDetailsSetStatoProcessor eventDetailsSetStatoProcessor) {
        super(context);
        this.config = config;
        this.eventDetailsGetProcessor = eventDetailsGetProcessor;
        this.eventDetailsSetStatoProcessor = eventDetailsSetStatoProcessor;
    }

    @Override
    public void configure() throws Exception {

        from("jms:queue:" + this.config.getInputQueueName())
                .process(eventDetailsGetProcessor)
                .process(eventDetailsSetStatoProcessor)
                .recipientList(header("applicationQueue"));
                //.to("jms:queue:${header.applicationQueue}");

    }
}
