package it.inps.pocmessagebroker.processors;

import it.inps.pocmessagebroker.config.EventTransactionFinalizerRouteConfig;
import it.inps.pocmessagebroker.repository.ApplicationeRepository;
import it.inps.pocmessagebroker.repository.EventoArcaPendingRepository;
import it.inps.pocmessagebroker.wsclients.ArcaNotificaEventiWSClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBException;
import java.io.IOException;


@Component
@Slf4j
public class EventTransactionFinalizerProcessor implements Processor {

    private final EventoArcaPendingRepository eventoArcaPendingRepository;
    private final ApplicationeRepository applicationeRepository;
    private final ArcaNotificaEventiWSClient arcaNotificaEventiWSClient;
    private final EventTransactionFinalizerRouteConfig config;

    public EventTransactionFinalizerProcessor(EventoArcaPendingRepository eventoArcaPendingRepository, ApplicationeRepository applicationeRepository, ArcaNotificaEventiWSClient arcaNotificaEventiWSClient, EventTransactionFinalizerRouteConfig config) {
        this.eventoArcaPendingRepository = eventoArcaPendingRepository;
        this.applicationeRepository = applicationeRepository;
        this.arcaNotificaEventiWSClient = arcaNotificaEventiWSClient;
        this.config = config;
    }

    @Override
    public void process(Exchange exchange) throws Exception {

        log.info("======================================================================");
        log.info("* EVENTI ARCA - INVIO A WS EVENTI COMPLETATI");

        this.applicationeRepository.findAll()
                .forEach(applicazione -> {
                    log.info("    Applicazione : {}", applicazione.getAppName());

                    try {
                        var elencoMessaggiCompleti = this.eventoArcaPendingRepository.findAllByStatoIsAndIdApplicazione(2, applicazione.getId());
                        elencoMessaggiCompleti.forEach(messaggio -> {
                            messaggio.setXml(messaggio.getXml().replace("<RETURNCODE/>","<RETURNCODE>OK</RETURNCODE>"));
                        });

                        this.arcaNotificaEventiWSClient.finalizeEvento(this.config.getWsEndpoint(), elencoMessaggiCompleti, applicazione);

                        log.info("      RICHIESTA WS ESEGUITA CON SUCCESSO");

                        elencoMessaggiCompleti.forEach(x -> x.setStato(100));
                        this.eventoArcaPendingRepository.saveAll(elencoMessaggiCompleti);

                        log.info("      RIMOSSI {} MESSAGGI DAL PROCESSO", elencoMessaggiCompleti.size());

                    } catch (IOException | JAXBException e) {
                        e.printStackTrace();
                    }

                });




    }
}
