package it.inps.pocmessagebroker.wsclients.impl;

import it.inps.pocmessagebroker.config.EventDetailsRouteConfig;
import it.inps.pocmessagebroker.domain.ApplicationConfig;
import it.inps.pocmessagebroker.model.EventoArcaDetailsSearchRequest;
import it.inps.pocmessagebroker.repository.ApplicationConfigRepository;
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
import java.util.Optional;

@Component
@Slf4j
@Profile("!dev")
public class ArcaIntraWSClientImpl implements ArcaIntraWSClient {

    private final ReadResource readResource;
    //private final ArcaIntraWSConfig config;
    private final RestTemplate restTemplate;
    private final ApplicationConfigRepository applicationConfigRepository;

    private final EventDetailsRouteConfig wsConfig;

    /*
    @Autowired
    public ArcaIntraWSClientImpl(ReadResource readResource, ArcaIntraWSConfig config, RestTemplate restTemplate, ApplicationConfigRepository applicationConfigRepository, EventDetailsRouteConfig wsConfig) {
        this.readResource = readResource;
        this.config = config;
        this.restTemplate = restTemplate;
        this.applicationConfigRepository = applicationConfigRepository;
        this.wsConfig = wsConfig;
    }

     */

    @Autowired
    public ArcaIntraWSClientImpl(ReadResource readResource, RestTemplate restTemplate, ApplicationConfigRepository applicationConfigRepository, EventDetailsRouteConfig wsConfig) {
        this.readResource = readResource;
        this.restTemplate = restTemplate;
        this.applicationConfigRepository = applicationConfigRepository;
        this.wsConfig = wsConfig;
    }


    @Override
    public String getDetails(EventoArcaDetailsSearchRequest request) throws IOException, JAXBException {
        try {
            ApplicationConfig applicationConfig = this.applicationConfigRepository.findAll().stream().findAny().orElseThrow(RuntimeException::new);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "text/xml");
            String requestString = readResource.getResourceAsString("arca_intra_search_codice_arca.xml");

            requestString = replacePlaceHolder(requestString, applicationConfig);

            HttpEntity<String> r = new HttpEntity<>(String.format(
                    requestString,
                    applicationConfig.getAppName(),
                    applicationConfig.getAppKey(),
                    applicationConfig.getUserId(),
                    applicationConfig.getIdentityProvider(),

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

    private String replacePlaceHolder(String requestString, ApplicationConfig applicationConfig) {
        requestString = replaceSinglePlaceholder(requestString, "CODICE_TIPO_OPERATORE", Optional.ofNullable(applicationConfig.getCodiceTipoOperatore()));
        requestString = replaceSinglePlaceholder(requestString, "PROVENIENZA", Optional.ofNullable(applicationConfig.getProvenienza()));
        requestString = replaceSinglePlaceholder(requestString, "CF", Optional.ofNullable(applicationConfig.getCodiceFiscaleOperatore()));
        requestString = replaceSinglePlaceholder(requestString, "MINPS", Optional.ofNullable(applicationConfig.getMatricolaINPS()));
        requestString = replaceSinglePlaceholder(requestString, "SRIC", Optional.ofNullable(applicationConfig.getSedeRichiesta()));
        requestString = replaceSinglePlaceholder(requestString, "GAPPL", Optional.ofNullable(applicationConfig.getGestioneApplOrigine()));
        requestString = replaceSinglePlaceholder(requestString, "SAPPL", Optional.ofNullable(applicationConfig.getSedeApplOrigine()));
        requestString = replaceSinglePlaceholder(requestString, "PGMAPPL", Optional.ofNullable(applicationConfig.getPgmApplOrigine()));
        requestString = replaceSinglePlaceholder(requestString, "WSINTRA", Optional.ofNullable(applicationConfig.getTranApplOrigine()));
        requestString = replaceSinglePlaceholder(requestString, "FUNZ", Optional.ofNullable(applicationConfig.getFunzione()));
        requestString = replaceSinglePlaceholder(requestString, "WSRESERVED", Optional.ofNullable(applicationConfig.getWsReserved()));
        requestString = replaceSinglePlaceholder(requestString, "PROFILO", Optional.ofNullable(applicationConfig.getProfilo()));
        requestString = replaceSinglePlaceholder(requestString, "TIPO_CHIAVE", Optional.ofNullable(applicationConfig.getTipoChiavePresente()));
        return requestString;
    }

    private String replaceSinglePlaceholder(String requestString, String placeHolder, Optional<String> value) {
        return requestString.replace("#"+placeHolder, value.orElse(""));
    }
}
