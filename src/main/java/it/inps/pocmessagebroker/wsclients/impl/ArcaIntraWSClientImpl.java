package it.inps.pocmessagebroker.wsclients.impl;

import it.inps.pocmessagebroker.config.ArcaIntraWSConfig;
import it.inps.pocmessagebroker.config.EventDetailsRouteConfig;
import it.inps.pocmessagebroker.model.EventoArcaDetailsSearchRequest;
import it.inps.pocmessagebroker.utils.ReadResource;
import it.inps.pocmessagebroker.wsclients.ArcaIntraWSClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.xml.bind.JAXBException;
import java.io.IOException;

@Component
@Slf4j
@Profile("!dev")
public class ArcaIntraWSClientImpl implements ArcaIntraWSClient {

    private final ReadResource readResource;
    private final ArcaIntraWSConfig config;
    private final RestTemplate restTemplate;


    private final EventDetailsRouteConfig wsConfig;

    @Autowired
    public ArcaIntraWSClientImpl(ReadResource readResource, ArcaIntraWSConfig config, RestTemplate restTemplate, EventDetailsRouteConfig wsConfig) {
        this.readResource = readResource;
        this.config = config;
        this.restTemplate = restTemplate;
        this.wsConfig = wsConfig;
    }

    @Override
    public String getDetails(EventoArcaDetailsSearchRequest request) throws IOException, JAXBException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "text/xml");
            String requestString = readResource.getResourceAsString("arca_intra_search_codice_arca.xml");
//            File file = ResourceUtils.getFile("classpath:arca_intra_search_codice_arca.xml");
//            String requestString = new String(Files.readAllBytes(file.toPath()));
            HttpEntity<String> r = new HttpEntity<>(String.format(
                    requestString,
                    config.getAppName(),
                    config.getAppKey(),
                    config.getUserId(),
                    config.getIdentityProvider(),
                    request.getArcaKey()), headers);

            String result = restTemplate.postForObject(wsConfig.getWsEndpoint(), r, String.class);
            if (!result.contains("<SOAP-ENV:Fault>")) {
                return result;
            } else {
                throw new RuntimeException("Impossibile leggere la lista eventi : " + result);
            }
        }
        catch(Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }
}
