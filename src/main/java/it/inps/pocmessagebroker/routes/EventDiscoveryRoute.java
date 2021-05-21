package it.inps.pocmessagebroker.routes;

import it.inps.pocmessagebroker.config.EventDiscoveryRouteConfig;
import it.inps.pocmessagebroker.processors.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EventDiscoveryRoute extends RouteBuilder {

    private final EventDiscoveryRouteConfig config;
    private final EventDiscoveryProcessor discoveryProcessor;
    private final EventDiscoverySaveResultProcessor eventDiscoverySaveResultProcessor;
    private final EventDiscoverySendResultProcessor eventDiscoverySendResultProcessor;
    private final EventDiscoveryMarkSentProcessor eventDiscoveryMarkSentProcessor;
    private final EventDiscoveryEndProcessor eventDiscoveryEndProcessor;

    @Autowired
    public EventDiscoveryRoute(CamelContext context, EventDiscoveryRouteConfig config, EventDiscoveryProcessor discoveryProcessor, EventDiscoverySaveResultProcessor eventDiscoverySaveResultProcessor, EventDiscoverySendResultProcessor eventDiscoverySendResultProcessor, EventDiscoveryMarkSentProcessor eventDiscoveryMarkSentProcessor, EventDiscoveryEndProcessor eventDiscoveryEndProcessor) {
        super(context);
        this.config = config;
        this.discoveryProcessor = discoveryProcessor;
        this.eventDiscoverySaveResultProcessor = eventDiscoverySaveResultProcessor;
        this.eventDiscoverySendResultProcessor = eventDiscoverySendResultProcessor;
        this.eventDiscoveryMarkSentProcessor = eventDiscoveryMarkSentProcessor;
        this.eventDiscoveryEndProcessor = eventDiscoveryEndProcessor;
    }

    @Override
    public void configure() throws Exception {
        from("timer://simpleTimer?period=" + config.getInterval())
                .onCompletion().process(this.eventDiscoveryEndProcessor).end()
                .process(this.discoveryProcessor)
                .process(this.eventDiscoverySaveResultProcessor)
                .process(this.eventDiscoverySendResultProcessor)
                .split(body())
                //.parallelProcessing()
                .to("jms:queue:" + this.config.getOutputQueueName())
                .process(this.eventDiscoveryMarkSentProcessor)

        ;
    }
}
