package it.inps.pocmessagebroker.processors;

import it.inps.pocmessagebroker.domain.Applicazione;
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
import java.util.concurrent.atomic.AtomicInteger;

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

        log.info("* EVENTI ARCA - SALVATAGGIO NUOVI EVENTI SU TABELLA DI CONTROLLO");
        var salvati = new AtomicInteger(0);
        var scartati = new AtomicInteger(0);
        eventiArca.keySet()
                .forEach(applicazione -> {
                    log.info("     processo eventi per applicazione : {}", applicazione);
                    var eventi = eventiArca.get(applicazione);
                    eventi.forEach(eventoArca -> {

                        if (!this.eventoArcaPendingRepository.findTopByArcaKey(eventoArca.getChiaveArca()).isPresent()) {
                            EventoArcaPending eventoArcaPending = new EventoArcaPending();
                            eventoArcaPending.setIdApplicazione(applicazione.getId());
                            eventoArcaPending.setArcaKey(eventoArca.getChiaveArca());
                            eventoArcaPending.setXml(eventoArca.getXml());
                            eventoArcaPending.setStato(0);
                            this.eventoArcaPendingRepository.save(eventoArcaPending);
                            salvati.incrementAndGet();
                        }
                        else
                            scartati.incrementAndGet();
                    });
                });

        log.info("          salvati {} nuovi eventi", salvati.get());
        log.info("          scartati {} eventi poichè già presente come chiave", scartati.get());
    }
}
