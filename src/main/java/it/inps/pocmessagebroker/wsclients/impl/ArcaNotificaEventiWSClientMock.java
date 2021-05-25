package it.inps.pocmessagebroker.wsclients.impl;

import it.inps.pocmessagebroker.domain.Applicazione;
import it.inps.pocmessagebroker.model.EventoArca;
import it.inps.pocmessagebroker.domain.EventoArcaPending;
import it.inps.pocmessagebroker.wsclients.ArcaNotificaEventiWSClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Component
@Profile("dev")
@Slf4j
public class ArcaNotificaEventiWSClientMock implements ArcaNotificaEventiWSClient {

    @Autowired
    public ArcaNotificaEventiWSClientMock() {

    }

    public List<EventoArca> getEventi(String webServiceEndpoint, Applicazione applicazione) {
        try {
            File file = ResourceUtils.getFile("classpath:discovery_response.xml");
            String requestString = new String(Files.readAllBytes(file.toPath()));
            return EventoArca.getFromWSResponse(requestString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Boolean finalizeEvento(String webServiceEndpoint, List<EventoArcaPending> eventoArca, Applicazione applicazione) throws IOException, JAXBException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "text/xml");
            File file = ResourceUtils.getFile("classpath:finalize_eventi_soap_request.xml");
            String requestString = new String(Files.readAllBytes(file.toPath()));

            StringBuilder sb = new StringBuilder();
            eventoArca.forEach(evento -> {
                sb.append(evento.getXml());
            });

            HttpEntity<String> request = new HttpEntity<>(String.format(
                    requestString,
                    applicazione.getAppName(),
                    applicazione.getAppKey(),
                    applicazione.getUserId(),
                    applicazione.getIdentityProvvider(),
                    applicazione.getCodiceArchivio(),
                    applicazione.getProgetto(),
                    sb
            ), headers);

            log.debug("XML di richiesta = {}", request.toString());

            return Boolean.TRUE;
        }
        catch(Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }
}
