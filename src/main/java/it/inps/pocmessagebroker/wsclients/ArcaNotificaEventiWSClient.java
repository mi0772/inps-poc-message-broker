package it.inps.pocmessagebroker.wsclients;

import it.inps.pocmessagebroker.model.ArcaEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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

            HttpEntity<String> request = new HttpEntity<>(String.format(REQ_GET_INFO, "1101", "A"), headers);

            String result = restTemplate.postForObject(webServiceEndpoint, request, String.class);

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

