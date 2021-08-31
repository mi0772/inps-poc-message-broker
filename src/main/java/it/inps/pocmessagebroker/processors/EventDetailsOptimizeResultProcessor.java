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

        Map<EventoArcaPending, List<Long>> res = new HashMap<>();
        List<EventoArcaPending> elencoEventi = eventoArcaPendingRepository.findAllByStatoIs(0);
        if (elencoEventi.size() == 0) {
          log.info("Non ci sono eventi per i quali recuperare dettagli");
        } else {
          log.info("Ci sono {} possibili eventi da analizzare per ottimizzare le chiamate...", elencoEventi.size());
          
          Collection<EventoArcaPending> v = elencoEventi.stream().collect(Collectors.toMap(EventoArcaPending::getArcaKey, p -> p, (p, q) -> p)).values();
          v.forEach(eventoArcaPending -> res.put(eventoArcaPending, elencoEventi.stream().filter(x -> x.getArcaKey().equals(eventoArcaPending.getArcaKey())).map(EventoArcaPending::getIdApplicazione).collect(Collectors.toList())));
          
          String msg = "Analisi completata: ";
          if ((res.values().size() - elencoEventi.size()) == 0) {
            log.info("{}saranno elaborati {} eventi", msg, elencoEventi.size());
          } else {
            log.info("{}verranno eseguite {} chiamate al WS di dettaglio invece di {}", msg, res.values().size(), elencoEventi.size());
          }
        }
        log.info("invio dei risultati alle relative code *** multithread: abilitare il log in debug per visualizzare il dettaglio");
        exchange.getIn().setBody(res);
    }
}
