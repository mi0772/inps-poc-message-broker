package it.inps.pocmessagebroker.wsclients;

import it.inps.pocmessagebroker.model.EventoArcaDetailsSearchRequest;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public interface ArcaIntraWSClient {
    String getDetails(EventoArcaDetailsSearchRequest request) throws IOException, JAXBException;
}
