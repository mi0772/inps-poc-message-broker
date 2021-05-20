package it.inps.pocmessagebroker.wsclients;

import it.inps.pocmessagebroker.model.ArcaEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
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

    public static final String REQ_GET_INFO = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:arc=\"http://arca.inps.it/ArcaNotificaEventiWS/\">\n" +
            "   <soapenv:Header>\n" +
            "   <inps:Identity xmlns:inps=\"http://inps.it/\">\n" +
            "         <AppName>WA00405</AppName>\n" +
            "         <AppKey>WA00405</AppKey>\n" +
            "         <UserId>WA00405</UserId>\n" +
            "         <IdentityProvider>AD</IdentityProvider>\n" +
            "      </inps:Identity>\n" +
            "</soapenv:Header>\n" +
            "   <soapenv:Body>\n" +
            "      <arc:notificaEventi>\n" +
            "         <notificaEventiRequest>\n" +
            "            <parametriApplicazione>\n" +
            "               <codiceArchivio>%s</codiceArchivio>\n" +
            "               <progetto>%s</progetto>\n" +
            "            </parametriApplicazione>\n" +
            "            <listaEventi>\n" +
            "               <eventiArca>\n" +
            "               </eventiArca>\n" +
            "            </listaEventi>\n" +
            "         </notificaEventiRequest>\n" +
            "      </arc:notificaEventi>\n" +
            "   </soapenv:Body>\n" +
            "</soapenv:Envelope>";

    public List<ArcaEvent> getCustomerInfo(String webServiceEndpoint) {

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "text/xml");

            File file = ResourceUtils.getFile("classpath:notifica_eventi_soap_request.xml");
            String requestString = new String(Files.readAllBytes(file.toPath()));

            HttpEntity<String> request = new HttpEntity<>(String.format(requestString,"WA00405","WA00405","WA00405","AD", "1101", "A"), headers);

            String result = restTemplate.postForObject(webServiceEndpoint, request, String.class);
            log.info("result: {}", result);
            if (!result.contains("<SOAP-ENV:Fault>")) {
                // Do SOAP Envelope body parsing here
            }
        }
        catch(Exception e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }
}

