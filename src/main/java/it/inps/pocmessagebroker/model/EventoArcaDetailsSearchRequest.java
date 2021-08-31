package it.inps.pocmessagebroker.model;

import lombok.Builder;
import lombok.Getter;

@Getter
public class EventoArcaDetailsSearchRequest {
    private final String arcaKey;

    public EventoArcaDetailsSearchRequest(String arcaKey) {
        this.arcaKey = arcaKey;
    }
}
