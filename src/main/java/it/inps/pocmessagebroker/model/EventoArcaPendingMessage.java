package it.inps.pocmessagebroker.model;

import com.google.gson.Gson;
import it.inps.pocmessagebroker.domain.Applicazione;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
public class EventoArcaPendingMessage implements Serializable {
    private final String arcaKey ;
    private final Applicazione applicazione;

    public String toJSON() {
        return new Gson().toJson(this);
    }

}
