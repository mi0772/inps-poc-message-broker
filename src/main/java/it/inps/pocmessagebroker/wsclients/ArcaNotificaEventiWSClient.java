package it.inps.pocmessagebroker.wsclients;

import it.inps.pocmessagebroker.model.Applicazione;
import it.inps.pocmessagebroker.model.EventoArca;

import java.util.List;

public interface ArcaNotificaEventiWSClient {
    List<EventoArca> getCustomerInfo(String webServiceEndpoint, Applicazione applicazione) ;
}

