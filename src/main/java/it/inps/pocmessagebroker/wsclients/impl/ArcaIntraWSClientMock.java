package it.inps.pocmessagebroker.wsclients.impl;

import it.inps.pocmessagebroker.config.ArcaIntraWSConfig;
import it.inps.pocmessagebroker.model.EventoArcaDetailsSearchRequest;
import it.inps.pocmessagebroker.wsclients.ArcaIntraWSClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Component
@Profile("dev")
public class ArcaIntraWSClientMock implements ArcaIntraWSClient {

    private final ArcaIntraWSConfig arcaIntraWSConfig;

    @Autowired
    public ArcaIntraWSClientMock(ArcaIntraWSConfig arcaIntraWSConfig) {
        this.arcaIntraWSConfig = arcaIntraWSConfig;
    }

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
