package it.inps.pocmessagebroker.routes;

import com.google.gson.Gson;
import it.inps.pocmessagebroker.config.EventDetailsRouteConfig;
import it.inps.pocmessagebroker.model.EventoArcaDetailsSearchRequest;
import it.inps.pocmessagebroker.model.EventoArcaPendingMessage;
import it.inps.pocmessagebroker.wsclients.ArcaIntraWSClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EventDetailsRoute extends RouteBuilder {

    private final EventDetailsRouteConfig config;
    private final ArcaIntraWSClient wsClient;

    @Autowired
    public EventDetailsRoute(CamelContext context, EventDetailsRouteConfig config, ArcaIntraWSClient wsClient) {
        super(context);
        this.config = config;
        this.wsClient = wsClient;
    }

    @Override
    public void configure() throws Exception {

        //non è un timer, ma un from ArtemisMQ
        from("jms:queue:" + this.config.getInputQueueName())
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        log.info("message received : {}", exchange.getIn().getBody());

                        var eventoPending = new Gson().fromJson(exchange.getIn().getBody().toString(), EventoArcaPendingMessage.class);
                        log.info("evento : {}", eventoPending);
                        var request = EventoArcaDetailsSearchRequest.builder()
                                .applicazione(eventoPending.getApplicazione())
                                .ricerca(EventoArcaDetailsSearchRequest.Ricerca
                                        .builder()
                                        .tipoChiaveRicerca("a")
                                        .build())
                                .sicurezza(EventoArcaDetailsSearchRequest.Sicurezza
                                        .builder()
                                        .build());

                        var response = wsClient.getDetails(request.build());

                    }
                });
    }
}
