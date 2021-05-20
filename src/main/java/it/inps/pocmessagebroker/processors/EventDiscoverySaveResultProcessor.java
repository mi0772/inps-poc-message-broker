package it.inps.pocmessagebroker.processors;

import it.inps.pocmessagebroker.model.Applicazione;
import it.inps.pocmessagebroker.model.EventoArca;
import it.inps.pocmessagebroker.model.EventoArcaPending;
import it.inps.pocmessagebroker.repository.EventoArcaPendingRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class EventDiscoverySaveResultProcessor implements Processor {

    private final EventoArcaPendingRepository eventoArcaPendingRepository;

    @Autowired
    public EventDiscoverySaveResultProcessor(EventoArcaPendingRepository eventoArcaPendingRepository) {
        this.eventoArcaPendingRepository = eventoArcaPendingRepository;
    }

    @Override
    public void process(Exchange exchange) {
        var eventiArca = (Map<Applicazione, List<EventoArca>>)exchange.getProperties().get("events");

        log.info("trovate {} applicazioni con chiavi arca");

        eventiArca.keySet()
                .forEach(applicazione -> {
                    log.info("processo eventi per applicazione : {}", applicazione);
                    var eventi = eventiArca.get(applicazione);
                    eventi.forEach(eventoArca -> {
                        EventoArcaPending eventoArcaPending = new EventoArcaPending();
                        eventoArcaPending.setIdApplicazione(applicazione.getId());
                        eventoArcaPending.setArcaKey(eventoArca.getChiaveArca());
                        eventoArcaPending.setStato(0);
                        this.eventoArcaPendingRepository.save(eventoArcaPending);
                        log.info("salvato {}", eventoArcaPending);
                    });
                });

        exchange.getIn().setBody(""+eventiArca.size());
    }
}
