package it.inps.pocmessagebroker.wsclients.impl;

import it.inps.pocmessagebroker.domain.ApplicationConfig;
import it.inps.pocmessagebroker.domain.Applicazione;
import it.inps.pocmessagebroker.model.EventoArca;
import it.inps.pocmessagebroker.repository.ApplicationConfigRepository;
import it.inps.pocmessagebroker.utils.ReadResource;
import it.inps.pocmessagebroker.domain.EventoArcaPending;
import it.inps.pocmessagebroker.wsclients.ArcaNotificaEventiWSClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;

@Component
@Slf4j
@Profile("!dev")
public class ArcaNotificaEventiWSClientImpl implements ArcaNotificaEventiWSClient {

  @Autowired
  ReadResource readResource;
  
    private final RestTemplate restTemplate;
    private final ApplicationConfigRepository applicationConfigRepository;

    @Autowired
    public ArcaNotificaEventiWSClientImpl(RestTemplate restTemplate, ApplicationConfigRepository applicationConfigRepository) {
        this.restTemplate = restTemplate;
        this.applicationConfigRepository = applicationConfigRepository;
    }

    public List<EventoArca> getEventi(String webServiceEndpoint, Applicazione applicazione) {
        try {
            ApplicationConfig applicationConfig = this.applicationConfigRepository.findAll().stream().findAny().orElseThrow(RuntimeException::new);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "text/xml");
            String requestString = readResource.getResourceAsString("notifica_eventi_soap_request.xml");
//            File file = ResourceUtils.getFile("classpath:notifica_eventi_soap_request.xml");
//            String requestString = new String(Files.readAllBytes(file.toPath()));
            HttpEntity<String> request = new HttpEntity<>(String.format(
                    requestString,
                    applicationConfig.getAppName(),
                    applicationConfig.getAppKey(),
                    applicationConfig.getUserId(),
                    applicationConfig.getIdentityProvider(),
                    applicazione.getCodiceArchivio(),
                    applicazione.getProgetto()), headers);

            log.info("*** Recupera eventi per '{}'-{}_{}", applicationConfig.getAppName(), applicazione.getCodiceArchivio(), applicazione.getProgetto());
            log.debug("*** XML di richiesta:\n{}", request.toString());
            String result = restTemplate.postForObject(webServiceEndpoint, request, String.class);
            log.debug("*** XML di risposta:\n{}", result);
            if (!result.contains("<SOAP-ENV:Fault>")) {
                return EventoArca.getFromWSResponse(result);
            } else {
                throw new RuntimeException("Impossibile leggere la lista eventi");
            }
        }
        catch(Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Boolean finalizeEvento(String webServiceEndpoint, List<EventoArcaPending> eventoArca, Applicazione applicazione) throws IOException, JAXBException {
        try {
            ApplicationConfig applicationConfig = this.applicationConfigRepository.findAll().stream().findAny().orElseThrow(RuntimeException::new);


            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "text/xml");
            String requestString = readResource.getResourceAsString("finalize_eventi_soap_request.xml");

            StringBuilder sb = new StringBuilder();
            eventoArca.forEach(evento -> {
                sb.append(evento.getXml());
            });

            HttpEntity<String> request = new HttpEntity<>(String.format(
                    requestString,
                    applicationConfig.getAppName(),
                    applicationConfig.getAppKey(),
                    applicationConfig.getUserId(),
                    applicationConfig.getIdentityProvider(),
                    applicazione.getCodiceArchivio(),
                    applicazione.getProgetto(),
                    sb.toString()
            ), headers);


            log.info("*** Conferma eventi per '{}'-{}_{}", applicationConfig.getAppName(), applicazione.getCodiceArchivio(), applicazione.getProgetto());

            String result = restTemplate.postForObject(webServiceEndpoint, request, String.class);
            if (!result.contains("<SOAP-ENV:Fault>")) {
                return Boolean.TRUE;
            } else {
                throw new RuntimeException("Impossibile inviare OK per il messaggio : "+ eventoArca.toString());
            }
        }
        catch(Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }
}
