package it.inps.pocmessagebroker.processors;

import com.google.gson.Gson;
import it.inps.pocmessagebroker.model.EventoArcaDetails;
import it.inps.pocmessagebroker.model.EventoArcaDetailsSearchRequest;
import it.inps.pocmessagebroker.model.EventoArcaPendingMessage;
import it.inps.pocmessagebroker.wsclients.ArcaIntraWSClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EventDetailsGetProcessor implements Processor {

    private final ArcaIntraWSClient wsClient;


    @Autowired
    public EventDetailsGetProcessor(ArcaIntraWSClient wsClient) {
        this.wsClient = wsClient;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        log.info("======================================================================");
        log.info("* EVENTI ARCA - DETTAGLIO EVENTO ");

        log.info("    - MESSAGGIO JMS RICEVUTO : {}", exchange.getIn().getBody());

        var eventoPending = new Gson().fromJson(exchange.getIn().getBody().toString(), EventoArcaPendingMessage.class);

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

        var eventoDetails = EventoArcaDetails.fromWSResponse(response);
        log.info("    - DETTAGLIO LETTO CON SUCCESSO DAL WS: {}", eventoDetails);
        exchange.setProperty("eventoDetails", eventoDetails);
    }
}
