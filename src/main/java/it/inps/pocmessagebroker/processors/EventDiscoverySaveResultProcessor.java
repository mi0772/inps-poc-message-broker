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
        Timestamp timeStamp = new Timestamp(new Date().getTime());

        eventiArca.keySet()
                .forEach(applicazione -> {
                    AtomicInteger tSalvati = new AtomicInteger();
                    AtomicInteger tScartati = new AtomicInteger();
                    log.info("{}_{}: controllo eventi ricevuti",  applicazione.getCodiceArchivio(), applicazione.getProgetto());
                    List<EventoArca> eventi = eventiArca.get(applicazione);
                    eventi.forEach(eventoArca -> {

                        //if (!this.eventoArcaPendingRepository.findTopByArcaKeyAndIdApplicazione(eventoArca.getChiaveArca(), applicazione.getId()).isPresent()) {
                        if (!this.eventoArcaPendingRepository.findTopByArcaKeyAndIdApplicazioneAndCodiceEvento(eventoArca.getChiaveArca(), applicazione.getId(), eventoArca.getCODICEEVENTO()).isPresent()) {
                            EventoArcaPending eventoArcaPending = new EventoArcaPending();
                            eventoArcaPending.setIdApplicazione(applicazione.getId());
                            eventoArcaPending.setArcaKey(eventoArca.getChiaveArca());
                            eventoArcaPending.setXml(eventoArca.getXml());
                            eventoArcaPending.setStato(0);
                            eventoArcaPending.setDataEvento(timeStamp);
                            eventoArcaPending.setCodiceEvento(eventoArca.getCODICEEVENTO());
                            this.eventoArcaPendingRepository.save(eventoArcaPending);
                            salvati.incrementAndGet();
                            tSalvati.getAndIncrement();
                        }
                        else {
                            scartati.incrementAndGet();
                            tScartati.getAndIncrement();
                        }
                    });

                    log.info("{}_{}: {} nuovi eventi registrati",  applicazione.getCodiceArchivio(), applicazione.getProgetto(), tSalvati.get());
                    log.info("{}_{}: {} nuovi eventi ignorati (registrati in precedenza)",  applicazione.getCodiceArchivio(), applicazione.getProgetto(), tScartati.get());
                });

        log.info("totale nuovi eventi registrati = {}",salvati.get());
        log.info("totale nuovi eventi ignorati (registrati in precedenza) = {}", scartati.get());
    }
}
