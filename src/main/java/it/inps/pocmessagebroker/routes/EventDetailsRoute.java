package it.inps.pocmessagebroker.routes;

import it.inps.pocmessagebroker.processors.EventDetailsGetProcessor;
import it.inps.pocmessagebroker.processors.EventDetailsOptimizeResultProcessor;
import it.inps.pocmessagebroker.processors.EventDetailsSetDestQueueProcessor;
import it.inps.pocmessagebroker.processors.EventDetailsSetStatoProcessor;
import it.inps.pocmessagebroker.repository.ApplicazioneRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EventDetailsRoute extends RouteBuilder {

    private final EventDetailsOptimizeResultProcessor eventDetailsOptimizeResultProcessor;
    private final EventDetailsGetProcessor eventDetailsGetProcessor;
    private final EventDetailsSetDestQueueProcessor eventDetailsSetDestQueueProcessor;
    private final EventDetailsSetStatoProcessor eventDetailsSetStatoProcessor;

    @Autowired
    public EventDetailsRoute(CamelContext context, EventDetailsOptimizeResultProcessor eventDetailsOptimizeResultProcessor, EventDetailsGetProcessor eventDetailsGetProcessor, ApplicazioneRepository applicazioneRepository, EventDetailsSetDestQueueProcessor eventDetailsSetDestQueueProcessor, EventDetailsSetStatoProcessor eventDetailsSetStatoProcessor) {
        super(context);
        this.eventDetailsOptimizeResultProcessor = eventDetailsOptimizeResultProcessor;
        this.eventDetailsGetProcessor = eventDetailsGetProcessor;
        this.eventDetailsSetDestQueueProcessor = eventDetailsSetDestQueueProcessor;
        this.eventDetailsSetStatoProcessor = eventDetailsSetStatoProcessor;
    }

    @Override
    public void configure() throws Exception {

        from("direct:event_details")
                .process(eventDetailsOptimizeResultProcessor)
                .process(eventDetailsGetProcessor)
                //.to("direct:event_finalize")
                .split(body())
                .process(this.eventDetailsSetDestQueueProcessor)
                .recipientList(header("applicationQueue"))
                .to("jms:${header.applicationQueue}")
                .process(eventDetailsSetStatoProcessor)
        ;
    }
}
