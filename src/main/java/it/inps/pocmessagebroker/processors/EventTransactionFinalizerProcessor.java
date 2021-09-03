package it.inps.pocmessagebroker.processors;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.bind.JAXBException;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import it.inps.pocmessagebroker.config.EventTransactionFinalizerRouteConfig;
import it.inps.pocmessagebroker.domain.Applicazione;
import it.inps.pocmessagebroker.domain.EventoArcaPending;
import it.inps.pocmessagebroker.repository.ApplicazioneRepository;
import it.inps.pocmessagebroker.repository.EventoArcaPendingRepository;
import it.inps.pocmessagebroker.wsclients.ArcaNotificaEventiWSClient;
import lombok.extern.slf4j.Slf4j;


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
        AtomicInteger inviati = new AtomicInteger(0);
        this.applicazioneRepository.findAll()
                .forEach(applicazione -> {
                    try {
//                      verifyPendingMessages(applicazione);
                        List<EventoArcaPending> elencoMessaggiCompleti = this.eventoArcaPendingRepository.findAllByStatoIsAndIdApplicazione(2, applicazione.getId());
                        log.info("messaggi completati da inviare : {}",  elencoMessaggiCompleti.size());
                        elencoMessaggiCompleti.forEach(messaggio -> {
                            messaggio.setXml(messaggio.getXml().replace("<RETURNCODE/>","<RETURNCODE>OK</RETURNCODE>"));
                            //messaggio.setXml(messaggio.getXMLforFinalizeRequest().replace("<RETURNCODE/>","<RETURNCODE>OK</RETURNCODE>"));
                        });
                        this.arcaNotificaEventiWSClient.finalizeEvento(this.config.getWsEndpoint(), elencoMessaggiCompleti, applicazione);
                        log.info("esecuzione chiamata al ws completata");

                        log.info("salvataggio stato eventi come completato in corso ...");
                        elencoMessaggiCompleti.forEach(x -> x.setStato(99));
                        this.eventoArcaPendingRepository.saveAll(elencoMessaggiCompleti);
                        log.info("salvataggio stato eventi come completato eseguita con successo");

                        inviati.addAndGet(elencoMessaggiCompleti.size());

                    } catch (IOException | JAXBException e) {
                        e.printStackTrace();
                    }
                    log.info("totale eventi segnati come completato: {}", inviati.get());
                });
    }
    
    private void verifyPendingMessages(Applicazione applicazione) {
      log.info("*** ID Applicazione={}", applicazione.getId());
      List<EventoArcaPending> elencoMessaggiCompleti = this.eventoArcaPendingRepository.findAll();
      elencoMessaggiCompleti.forEach(messaggio -> {
        log.info("{}-{}", messaggio.getArcaKey(), messaggio.getStato());
      });
      
    }
}
