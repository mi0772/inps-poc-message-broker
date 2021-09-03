package it.inps.pocmessagebroker.routes;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import it.inps.pocmessagebroker.config.EventDiscoveryRouteConfig;
import it.inps.pocmessagebroker.processors.EventDiscoveryProcessor;
import it.inps.pocmessagebroker.processors.EventDiscoverySaveResultProcessor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EventDiscoveryRoute extends RouteBuilder {

    @Autowired
    private ConfigurableApplicationContext appContext;

//  private CamelContext camelContext;

    private final EventDiscoveryProcessor discoveryProcessor;
    private final EventDiscoverySaveResultProcessor eventDiscoverySaveResultProcessor;

    @Autowired
    public EventDiscoveryRoute(CamelContext context, EventDiscoveryRouteConfig config, EventDiscoveryProcessor discoveryProcessor, EventDiscoverySaveResultProcessor eventDiscoverySaveResultProcessor) {
        super(context);
        this.discoveryProcessor = discoveryProcessor;
        this.eventDiscoverySaveResultProcessor = eventDiscoverySaveResultProcessor;
//        this.camelContext = context;
    }

    @Override
    public void configure() throws Exception {
        from("timer://manualRestart?repeatCount=1").onCompletion()
                .to("direct:event_finalize")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        log.info("elaborazione terminata");
                        CamelContext camelContext = exchange.getContext();
                        log.info("*** Camel ShutdownStrategy timeout = {} seconds", camelContext.getShutdownStrategy().getTimeout());
                        camelContext.getShutdownStrategy().setShutdownRoutesInReverseOrder(true);
                        camelContext.stop();
                        log.info("*** Start process shutdown...");
                        appContext.close();
                        log.info("*** Process shutdown complete.");
                    }
                }).end()
                .process(this.discoveryProcessor)
                .process(this.eventDiscoverySaveResultProcessor)
                .to("direct:event_details")
        ;
    }
}
