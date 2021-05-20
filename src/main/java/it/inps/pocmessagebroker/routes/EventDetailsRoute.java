package it.inps.pocmessagebroker.routes;

import it.inps.pocmessagebroker.config.EventDetailsRouteConfig;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventDetailsRoute extends RouteBuilder {

    private final EventDetailsRouteConfig config;

    @Autowired
    public EventDetailsRoute(CamelContext context, EventDetailsRouteConfig config) {
        super(context);
        this.config = config;
    }

    @Override
    public void configure() throws Exception {

    }
}
