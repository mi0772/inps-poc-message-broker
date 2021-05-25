package it.inps.pocmessagebroker.routes;

import it.inps.pocmessagebroker.config.EventDetailsRouteConfig;
import it.inps.pocmessagebroker.processors.EventDetailsGetProcessor;
import it.inps.pocmessagebroker.processors.EventDetailsOptimizeResultProcessor;
import it.inps.pocmessagebroker.processors.EventDetailsSetDestQueueProcessor;
import it.inps.pocmessagebroker.repository.ApplicazioneRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.AggregationStrategy;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

import static java.util.stream.Collectors.groupingBy;

@Component
@Slf4j
public class EventDetailsRoute extends RouteBuilder {

    private final EventDetailsRouteConfig config;
    private final EventDetailsOptimizeResultProcessor eventDetailsOptimizeResultProcessor;
    private final EventDetailsGetProcessor eventDetailsGetProcessor;
    private final EventDetailsSetDestQueueProcessor eventDetailsSetDestQueueProcessor;

    @Autowired
    public EventDetailsRoute(CamelContext context, EventDetailsRouteConfig config, EventDetailsOptimizeResultProcessor eventDetailsOptimizeResultProcessor, EventDetailsGetProcessor eventDetailsGetProcessor, ApplicazioneRepository applicazioneRepository, EventDetailsSetDestQueueProcessor eventDetailsSetDestQueueProcessor) {
        super(context);
        this.config = config;
        this.eventDetailsOptimizeResultProcessor = eventDetailsOptimizeResultProcessor;
        this.eventDetailsGetProcessor = eventDetailsGetProcessor;
        this.eventDetailsSetDestQueueProcessor = eventDetailsSetDestQueueProcessor;
    }

    @Override
    public void configure() throws Exception {

        from("direct:event_details")
                .process(eventDetailsOptimizeResultProcessor)
                .process(eventDetailsGetProcessor)
                .to("direct:event_finalize")
                .split(body())
                .process(this.eventDetailsSetDestQueueProcessor)
                .recipientList(header("applicationQueue"))
                .to("jms:queue:${header.applicationQueue}")
        ;
    }
}
