package it.inps.pocmessagebroker.routes;

import it.inps.pocmessagebroker.config.EventDiscoveryRouteConfig;
import it.inps.pocmessagebroker.processors.EventDiscoveryProcessor;
import it.inps.pocmessagebroker.processors.EventDiscoverySaveResultProcessor;
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

    @Autowired
    public EventDiscoveryRoute(CamelContext context, EventDiscoveryRouteConfig config, EventDiscoveryProcessor discoveryProcessor, EventDiscoverySaveResultProcessor eventDiscoverySaveResultProcessor) {
        super(context);
        this.config = config;
        this.discoveryProcessor = discoveryProcessor;
        this.eventDiscoverySaveResultProcessor = eventDiscoverySaveResultProcessor;
    }

    @Override
    public void configure() throws Exception {
        from("timer://simpleTimer?period=" + config.getInterval())
                .process(this.discoveryProcessor)
                .process(this.eventDiscoverySaveResultProcessor)
                .to("stream:out");
    }
}
