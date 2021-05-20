package it.inps.pocmessagebroker.routes;

import it.inps.pocmessagebroker.config.EventDiscoveryRouteConfig;
import it.inps.pocmessagebroker.model.EventoArca;
import it.inps.pocmessagebroker.processors.EventDiscoveryProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        var eventiArca = (List<EventoArca>)exchange.getProperties().get("events");
                        log.info("trovati numero {} di eventi da salvare sul db", eventiArca.size());
                        exchange.getIn().setBody(eventiArca.toString());

                    }
                })
                .to("stream:out");
    }
}
