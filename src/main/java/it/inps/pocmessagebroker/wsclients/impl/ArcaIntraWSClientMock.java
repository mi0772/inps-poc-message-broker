package it.inps.pocmessagebroker.wsclients.impl;

import it.inps.pocmessagebroker.model.EventoArcaDetailsSearchRequest;
import it.inps.pocmessagebroker.wsclients.ArcaIntraWSClient;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Component
@Profile("dev")
public class ArcaIntraWSClientMock implements ArcaIntraWSClient {

    @Override
    public String getDetails(EventoArcaDetailsSearchRequest request) {

        try {
            File file = ResourceUtils.getFile("classpath:arca_intra_search_codice_arca_response.xml");
            return new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
