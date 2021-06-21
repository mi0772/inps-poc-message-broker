package it.inps.pocmessagebroker.processors;

import it.inps.pocmessagebroker.domain.EventoArcaPending;
import it.inps.pocmessagebroker.repository.EventoArcaPendingRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class EventDetailsOptimizeResultProcessor implements Processor {
    private final EventoArcaPendingRepository eventoArcaPendingRepository;

    @Autowired
    public EventDetailsOptimizeResultProcessor(EventoArcaPendingRepository eventoArcaPendingRepository) {
        this.eventoArcaPendingRepository = eventoArcaPendingRepository;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        log.info("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ");
        log.info("fase 2/3 : richiesta dettaglio eventi");

        List<EventoArcaPending> elencoEventi = eventoArcaPendingRepository.findAllByStatoIs(0);
        log.info("ci sono {} eventi da processare in dettaglio", elencoEventi.size());

        log.info("analisi elenco eventi per ottimizzazione delle chiamate ...");
        Map<EventoArcaPending, List<Long>> res = new HashMap<>();

        Collection<EventoArcaPending> v = elencoEventi.stream().collect(Collectors.toMap(EventoArcaPending::getArcaKey, p -> p, (p, q) -> p)).values();
        v.forEach(eventoArcaPending -> res.put(eventoArcaPending, elencoEventi.stream().filter(x -> x.getArcaKey().equals(eventoArcaPending.getArcaKey())).map(EventoArcaPending::getIdApplicazione).collect(Collectors.toList())));

        log.info("ottimizzazione completata, verranno eseguite {} chiamate al WS di dettaglio invece di {}", res.values().size(), elencoEventi.size());

        log.info("invio dei risultati alle relative code, l'invio avverrà in modalità multithread, abilitare il log in debug per visualizzare il dettaglio");
        exchange.getIn().setBody(res);
    }
}