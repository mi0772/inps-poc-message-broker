package it.inps.pocmessagebroker.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EventoArcaDetailsSearchRequest {
    private Applicazione applicazione;

    @Getter
    @Builder
    public static class Sicurezza {
        private String provenienza;
        private String codiceFiscaleOperatore;
        private String matricolaINPS;
        private String sedeRichiesta;
        private String gestioneApplOrigine;
        private String sedeApplOrigine;
        private String pgmApplOrigine;
        private String tranApplOrigine;
        private String funzione;
    }

    @Getter
    @Builder
    public static class Ricerca {
        private String profilo;
        private String tipoChiaveRicerca;
        private String chiaveArca;
    }

    private Sicurezza sicurezza;
    private Ricerca ricerca;

    private EventoArcaDetailsSearchRequest(Applicazione applicazione, Sicurezza sicurezza, Ricerca ricerca) {
        this.applicazione = applicazione;
        this.sicurezza = sicurezza;
        this.ricerca = ricerca;
    }
}
