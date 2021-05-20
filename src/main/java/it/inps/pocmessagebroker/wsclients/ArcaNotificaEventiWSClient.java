package it.inps.pocmessagebroker.wsclients;

import it.inps.pocmessagebroker.model.EventoArca;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

@Component
@Slf4j
public class ArcaNotificaEventiWSClient {

    private final RestTemplate restTemplate;

    @Autowired
    public ArcaNotificaEventiWSClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<EventoArca> getCustomerInfo(String webServiceEndpoint) {

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "text/xml");
            File file = ResourceUtils.getFile("classpath:notifica_eventi_soap_request.xml");
            String requestString = new String(Files.readAllBytes(file.toPath()));
            HttpEntity<String> request = new HttpEntity<>(String.format(requestString,"WA00405","WA00405","WA00405","AD", "1101", "A"), headers);

            String result = restTemplate.postForObject(webServiceEndpoint, request, String.class);
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
}

