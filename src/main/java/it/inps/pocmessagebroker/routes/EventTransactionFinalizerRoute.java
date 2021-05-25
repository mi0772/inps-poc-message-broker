package it.inps.pocmessagebroker.routes;

import it.inps.pocmessagebroker.config.EventTransactionFinalizerRouteConfig;
import it.inps.pocmessagebroker.processors.EventTransactionFinalizerProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EventTransactionFinalizerRoute extends RouteBuilder {

    private final EventTransactionFinalizerRouteConfig config;
    private final EventTransactionFinalizerProcessor eventTransactionFinalizerProcessor;

    public EventTransactionFinalizerRoute(CamelContext context, EventTransactionFinalizerRouteConfig config, EventTransactionFinalizerProcessor eventTransactionFinalizerProcessor) {
        super(context);
        this.config = config;
        this.eventTransactionFinalizerProcessor = eventTransactionFinalizerProcessor;
    }

    @Override
    public void configure() throws Exception {
        from("direct:event_finalize").process(eventTransactionFinalizerProcessor);
    }
}
