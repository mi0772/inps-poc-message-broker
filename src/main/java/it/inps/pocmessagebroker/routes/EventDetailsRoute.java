package it.inps.pocmessagebroker.routes;

import it.inps.pocmessagebroker.processors.*;
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
    private final EventDetailsSendToQueueProcessor eventDetailsSendToQueueProcessor;

    @Autowired
    public EventDetailsRoute(CamelContext context, EventDetailsOptimizeResultProcessor eventDetailsOptimizeResultProcessor, EventDetailsGetProcessor eventDetailsGetProcessor, ApplicazioneRepository applicazioneRepository, EventDetailsSetDestQueueProcessor eventDetailsSetDestQueueProcessor, EventDetailsSetStatoProcessor eventDetailsSetStatoProcessor, EventDetailsSendToQueueProcessor eventDetailsSendToQueueProcessor) {
        super(context);
        this.eventDetailsOptimizeResultProcessor = eventDetailsOptimizeResultProcessor;
        this.eventDetailsGetProcessor = eventDetailsGetProcessor;
        this.eventDetailsSetDestQueueProcessor = eventDetailsSetDestQueueProcessor;
        this.eventDetailsSetStatoProcessor = eventDetailsSetStatoProcessor;
        this.eventDetailsSendToQueueProcessor = eventDetailsSendToQueueProcessor;
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
                //.to("jms:queue:${header.applicationQueue}")
                //.to("amqp:queue:${header.applicationQueue}")
                //.to("${header.applicationQueue}")
                .process(this.eventDetailsSendToQueueProcessor)
                .process(eventDetailsSetStatoProcessor)
        ;
    }
}
