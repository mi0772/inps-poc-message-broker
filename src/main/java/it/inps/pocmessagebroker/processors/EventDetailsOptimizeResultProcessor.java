package it.inps.pocmessagebroker.processors;

import it.inps.pocmessagebroker.domain.EventoArcaPending;
import it.inps.pocmessagebroker.repository.EventoArcaPendingRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class EventDetailsOptimizeResultProcessor implements Processor {
    private EventoArcaPendingRepository eventoArcaPendingRepository;

    @Autowired
    public EventDetailsOptimizeResultProcessor(EventoArcaPendingRepository eventoArcaPendingRepository) {
        this.eventoArcaPendingRepository = eventoArcaPendingRepository;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        List<EventoArcaPending> elencoEventi = eventoArcaPendingRepository.findAllByStatoIs(0);
        log.info("trovati {} eventi da processare in dettaglio", elencoEventi.size());

        log.info("ottimizzazione delle chiamate in corso");
        Map<EventoArcaPending, List<Long>> res = new HashMap<>();

        var v = elencoEventi.stream().collect(Collectors.toMap(EventoArcaPending::getArcaKey, p -> p, (p, q) -> p)).values();
        v.forEach(eventoArcaPending -> {
            res.put(eventoArcaPending, elencoEventi.stream().filter(x -> x.getArcaKey().equals(eventoArcaPending.getArcaKey())).map(EventoArcaPending::getIdApplicazione).collect(Collectors.toList()));
        });
        log.info("ottimizzazione completata, verranno eseguite {} chiamate al WS di dettaglio", res.values().size());

        exchange.getIn().setBody(res);
        //ora prendi questo nel successivo processor ed invoca il ws di dettaglio

    }
}
