package it.inps.pocmessagebroker.processors;

import it.inps.pocmessagebroker.config.EventTransactionFinalizerRouteConfig;
import it.inps.pocmessagebroker.repository.ApplicazioneRepository;
import it.inps.pocmessagebroker.repository.EventoArcaPendingRepository;
import it.inps.pocmessagebroker.wsclients.ArcaNotificaEventiWSClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;


@Component
@Slf4j
public class EventTransactionFinalizerProcessor implements Processor {

    private final EventoArcaPendingRepository eventoArcaPendingRepository;
    private final ApplicazioneRepository applicazioneRepository;
    private final ArcaNotificaEventiWSClient arcaNotificaEventiWSClient;
    private final EventTransactionFinalizerRouteConfig config;

    public EventTransactionFinalizerProcessor(EventoArcaPendingRepository eventoArcaPendingRepository, ApplicazioneRepository applicazioneRepository, ArcaNotificaEventiWSClient arcaNotificaEventiWSClient, EventTransactionFinalizerRouteConfig config) {
        this.eventoArcaPendingRepository = eventoArcaPendingRepository;
        this.applicazioneRepository = applicazioneRepository;
        this.arcaNotificaEventiWSClient = arcaNotificaEventiWSClient;
        this.config = config;
    }

    @Override
    public void process(Exchange exchange) throws Exception {

        log.info("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ");
        log.info("fase 3/3 : invio conferma al WS Arca per gli eventi completati");
        var inviati = new AtomicInteger(0);
        this.applicazioneRepository.findAll()
                .forEach(applicazione -> {
                    try {
                        var elencoMessaggiCompleti = this.eventoArcaPendingRepository.findAllByStatoIsAndIdApplicazione(2, applicazione.getId());
                        log.info("{}: messaggi completati da inviare : {}", applicazione.getAppName(), elencoMessaggiCompleti.size());
                        elencoMessaggiCompleti.forEach(messaggio -> {
                            messaggio.setXml(messaggio.getXml().replace("<RETURNCODE/>","<RETURNCODE>OK</RETURNCODE>"));
                        });
                        log.info("{}: esecuzione chiamata al ws in corso ...", applicazione.getAppName());
                        this.arcaNotificaEventiWSClient.finalizeEvento(this.config.getWsEndpoint(), elencoMessaggiCompleti, applicazione);
                        log.info("{}: esecuzione chiamata al ws completata", applicazione.getAppName());

                        log.info("{}: salvataggio stato eventi come completato in corso ...", applicazione.getAppName());
                        elencoMessaggiCompleti.forEach(x -> x.setStato(100));
                        this.eventoArcaPendingRepository.saveAll(elencoMessaggiCompleti);
                        log.info("{}: salvataggio stato eventi come completato eseguita con successo", applicazione.getAppName());

                        inviati.addAndGet(elencoMessaggiCompleti.size());

                    } catch (IOException | JAXBException e) {
                        e.printStackTrace();
                    }
                    log.info("totale eventi segnati come comletato: {}", inviati.get());
                });




    }
}
