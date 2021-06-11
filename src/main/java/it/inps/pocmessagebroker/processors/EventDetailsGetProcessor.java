package it.inps.pocmessagebroker.processors;

import it.inps.pocmessagebroker.domain.EventoArcaPending;
import it.inps.pocmessagebroker.model.EventoArcaDetails;
import it.inps.pocmessagebroker.model.EventoArcaDetailsSearchRequest;
import it.inps.pocmessagebroker.repository.ApplicazioneRepository;
import it.inps.pocmessagebroker.wsclients.ArcaIntraWSClient;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class EventDetailsGetProcessor implements Processor {

    private final ArcaIntraWSClient wsClient;

    @Autowired
    public EventDetailsGetProcessor(ArcaIntraWSClient wsClient, ApplicazioneRepository applicazioneRepository) {
        this.wsClient = wsClient;
    }

    @Override
    public void process(Exchange exchange) throws Exception {

        Map<EventoArcaPending, List<Long>> elencoEventi = (Map<EventoArcaPending, List<Long>>)exchange.getIn().getBody();
        List<Result> result = new ArrayList<Result>(1000);

        elencoEventi.keySet().forEach(evento -> {
            List<Long> applicazioni = elencoEventi.get(evento);
            EventoArcaDetailsSearchRequest request = new EventoArcaDetailsSearchRequest(evento.getArcaKey());

            try {
                String response = wsClient.getDetails(request);
                evento.setXml(EventoArcaDetails.fromWSResponse(response).getXml());
                applicazioni.forEach(app -> result.add(new Result(app, evento)));
            }
            catch (JAXBException | IOException e) {
                throw new RuntimeException();
            }
        });

        exchange.getIn().setBody(result);
    }

    @Getter
    public static class Result {
        private final Long idApplicazione;
        private final EventoArcaPending eventoArcaPending;

        public Result(Long idApplicazione, EventoArcaPending eventoArcaPending) {
            this.idApplicazione = idApplicazione;
            this.eventoArcaPending = eventoArcaPending;
        }
    }
}
