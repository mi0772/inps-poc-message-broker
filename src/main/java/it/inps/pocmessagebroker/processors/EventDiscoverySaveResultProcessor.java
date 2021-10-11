package it.inps.pocmessagebroker.processors;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.inps.pocmessagebroker.domain.Applicazione;
import it.inps.pocmessagebroker.domain.EventoArcaPending;
import it.inps.pocmessagebroker.model.EventoArca;
import it.inps.pocmessagebroker.repository.EventoArcaPendingRepository;
import lombok.extern.slf4j.Slf4j;

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
        Map<Applicazione, List<EventoArca>> eventiArca = (Map<Applicazione, List<EventoArca>>)exchange.getProperties().get("events");

        log.info("salvataggio lista eventi su database");

        AtomicInteger salvati = new AtomicInteger(0);
        AtomicInteger scartati = new AtomicInteger(0);

        eventiArca.keySet().forEach(applicazione -> {
                    AtomicInteger tSalvati = new AtomicInteger();
                    AtomicInteger tScartati = new AtomicInteger();
                    log.info("{}_{}: controllo eventi ricevuti",  applicazione.getCodiceArchivio(), applicazione.getProgetto());
                    List<EventoArca> eventi = eventiArca.get(applicazione);
                    eventi.forEach(eventoArca -> {

                        List<EventoArcaPending> liveEvents = this.eventoArcaPendingRepository.findAllByArcaKeyAndIdApplicazione(eventoArca.getChiaveArca(), applicazione.getId());
                        if (liveEvents.stream().anyMatch(x -> x.getCodiceEvento().contains(eventoArca.getCODICEEVENTO()))) {
                            log.info("l'evento {} è già presente lo ignoro", eventoArca.getChiaveArca());
                            scartati.incrementAndGet();
                            tScartati.getAndIncrement();;
                        }

                        if (liveEvents == null || liveEvents.isEmpty()) {
                            log.info("registro il nuovo evento : {}", eventoArca.getChiaveArca());
                            EventoArcaPending eventoArcaPending = new EventoArcaPending();
                            eventoArcaPending.setIdApplicazione(applicazione.getId());
                            eventoArcaPending.setArcaKey(eventoArca.getChiaveArca());
                            eventoArcaPending.setXml(eventoArca.getXml());
                            eventoArcaPending.setStato(0);
                            eventoArcaPending.setDataEvento(new Timestamp(new Date().getTime()));
                            eventoArcaPending.setCodiceEvento(eventoArca.getCODICEEVENTO());
                            this.eventoArcaPendingRepository.save(eventoArcaPending);
                            salvati.incrementAndGet();
                            tSalvati.getAndIncrement();

                        }
                        else {
                            log.info("aggiorno la riga event_pendings per nuovo codice evento");
                            EventoArcaPending eventoArcaPending = liveEvents.stream().findAny().orElseThrow(RuntimeException::new);
                            eventoArcaPending.setCodiceEvento(eventoArcaPending.getCodiceEvento() + ";" + eventoArca.getCODICEEVENTO());
                            this.eventoArcaPendingRepository.save(eventoArcaPending);
                            salvati.incrementAndGet();
                            tSalvati.getAndIncrement();
                        }
                    });

                    log.info("{}_{}: {} nuovi eventi registrati",  applicazione.getCodiceArchivio(), applicazione.getProgetto(), tSalvati.get());
                    log.info("{}_{}: {} nuovi eventi ignorati (registrati in precedenza)",  applicazione.getCodiceArchivio(), applicazione.getProgetto(), tScartati.get());
                });

        log.info("totale nuovi eventi registrati = {}",salvati.get());
        log.info("totale nuovi eventi ignorati (registrati in precedenza) = {}", scartati.get());
    }
}
