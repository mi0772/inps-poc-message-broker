package it.inps.pocmessagebroker.wsclients;

import it.inps.pocmessagebroker.model.Applicazione;
import it.inps.pocmessagebroker.model.EventoArca;
import it.inps.pocmessagebroker.model.EventoArcaDetailsSearchRequest;
import it.inps.pocmessagebroker.model.EventoArcaPending;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.ResourceUtils;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public interface ArcaNotificaEventiWSClient {
    List<EventoArca> getEventi(String webServiceEndpoint, Applicazione applicazione) ;
    Boolean finalizeEvento(String webServiceEndpoint, List<EventoArcaPending> eventoArca, Applicazione applicazione) throws IOException, JAXBException;
}

