package it.inps.pocmessagebroker.routes;

import it.inps.pocmessagebroker.config.EventTransactionFinalizerRouteConfig;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;

public class EventTransactionFinalizerRoute extends RouteBuilder {

    private final EventTransactionFinalizerRouteConfig config;

    public EventTransactionFinalizerRoute(CamelContext context, EventTransactionFinalizerRouteConfig config) {
        super(context);
        this.config = config;
    }

    @Override
    public void configure() throws Exception {

    }
}
