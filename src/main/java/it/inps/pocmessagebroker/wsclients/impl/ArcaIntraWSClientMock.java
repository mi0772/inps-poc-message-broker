package it.inps.pocmessagebroker.wsclients.impl;

import it.inps.pocmessagebroker.model.EventoArcaDetailsSearchRequest;
import it.inps.pocmessagebroker.utils.ReadResource;
import it.inps.pocmessagebroker.wsclients.ArcaIntraWSClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Profile("dev")
public class ArcaIntraWSClientMock implements ArcaIntraWSClient {

  @Autowired
  ReadResource readResource;

    //private final ArcaIntraWSConfig arcaIntraWSConfig;

    /*
    @Autowired
    public ArcaIntraWSClientMock(ArcaIntraWSConfig arcaIntraWSConfig) {
        this.arcaIntraWSConfig = arcaIntraWSConfig;
    }

     */



    @Override
    public String getDetails(EventoArcaDetailsSearchRequest request) {

        try {
          return readResource.getResourceAsString("arca_intra_search_codice_arca_response.xml");
//            File file = ResourceUtils.getFile("classpath:arca_intra_search_codice_arca_response.xml");
//            return new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
