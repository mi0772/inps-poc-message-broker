package it.inps.pocmessagebroker.wsclients;

import it.inps.pocmessagebroker.domain.Applicazione;
import it.inps.pocmessagebroker.model.EventoArca;
import it.inps.pocmessagebroker.domain.EventoArcaPending;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;

public interface ArcaNotificaEventiWSClient {
    List<EventoArca> getEventi(String webServiceEndpoint, Applicazione applicazione) ;
    Boolean finalizeEvento(String webServiceEndpoint, List<EventoArcaPending> eventoArca, Applicazione applicazione) throws IOException, JAXBException;
}

