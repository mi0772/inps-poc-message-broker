package it.inps.pocmessagebroker.wsclients.impl;

import it.inps.pocmessagebroker.model.Applicazione;
import it.inps.pocmessagebroker.model.EventoArca;
import it.inps.pocmessagebroker.wsclients.ArcaNotificaEventiWSClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Component
@Profile("dev")
public class ArcaNotificaEventiWSClientMock implements ArcaNotificaEventiWSClient {

    @Autowired
    public ArcaNotificaEventiWSClientMock() {

    }

    public List<EventoArca> getCustomerInfo(String webServiceEndpoint, Applicazione applicazione) {
        try {
            File file = ResourceUtils.getFile("classpath:discovery_response.xml");
            String requestString = new String(Files.readAllBytes(file.toPath()));
            return EventoArca.getFromWSResponse(requestString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
