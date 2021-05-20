package it.inps.pocmessagebroker.routes;

import it.inps.pocmessagebroker.config.EventDiscoveryRouteConfig;
import it.inps.pocmessagebroker.processors.EventDiscoveryProcessor;
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

    @Autowired
    public EventDiscoveryRoute(CamelContext context, EventDiscoveryRouteConfig config, EventDiscoveryProcessor discoveryProcessor) {
        super(context);
        this.config = config;
        this.discoveryProcessor = discoveryProcessor;
    }

    @Override
    public void configure() throws Exception {
        from("timer://simpleTimer?period=" + config.getInterval())
                .process(this.discoveryProcessor)
                .setBody(simple("Hello from timer at ${header.firedTime}"))
                .to("stream:out");
    }
}
