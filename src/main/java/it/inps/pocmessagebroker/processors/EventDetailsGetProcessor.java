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
    private final ApplicazioneRepository applicazioneRepository;

    @Autowired
    public EventDetailsGetProcessor(ArcaIntraWSClient wsClient, ApplicazioneRepository applicazioneRepository) {
        this.wsClient = wsClient;
        this.applicazioneRepository = applicazioneRepository;
    }

    @Override
    public void process(Exchange exchange) throws Exception {

        var elencoEventi = (Map<EventoArcaPending, List<Long>>)exchange.getIn().getBody();
        var result = new ArrayList<Result>(1000);

        elencoEventi.keySet().forEach(evento -> {
            List<Long> applicazioni = elencoEventi.get(evento);
            var applicazione = this.applicazioneRepository.getOne(applicazioni.get(0));

            var request = EventoArcaDetailsSearchRequest.builder()
                    .applicazione(applicazione)
                    .ricerca(EventoArcaDetailsSearchRequest.Ricerca
                            .builder()
                            .tipoChiaveRicerca("a")
                            .build())
                    .sicurezza(EventoArcaDetailsSearchRequest.Sicurezza
                            .builder()
                            .build());

            try {
                var response = wsClient.getDetails(request.build());
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
