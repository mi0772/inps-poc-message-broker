package it.inps.pocmessagebroker.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "arca_events_pending")
public class EventoArcaPending {

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name="id_applicazione")
    private Long idApplicazione;

    @Column(name = "arca_key")
    private String arcaKey ;

    @Column(name = "stato")
    private Integer stato;

    @Column(name = "xml")
    private String xml;

    @Column(name = "codice_evento")
    private String codiceEvento ;
    
    @Column(name = "data_evento")
    private Timestamp dataEvento;

    @Override
    public String toString() {
        return "EventoArcaPending{" +
                "id=" + id +
                ", idApplicazione=" + idApplicazione +
                ", arcaKey='" + arcaKey + '\'' +
                ", stato=" + stato +
                ", xml=" + xml +
                ", codiceEvento=" + codiceEvento +
                ", dataEvento=" + codiceEvento +
                '}';
    }

    public String getXMLforFinalizeRequest() {
        return removeParts(this.getXml(), "CODICEEVENTO", "ARCHIVIO_SUB", "PROGETTO_SUB");
    }

    private static String removePart(String s, String tag) {
        int start = s.indexOf("<"+tag+">");
        if (start < 0)
            return s;
        int end = s.indexOf("</"+tag+">") + tag.length() + 3;
        return s.substring(0, start) + s.substring(end);
    }

    private static String removeParts(String s, String... tags) {
        StringBuilder r = new StringBuilder(s);
        for (String tag : tags) {
            String temp = removePart(r.toString(), tag);
            r.delete(0,  r.length());
            r.append(temp);
        }
        return r.toString().replace("EventoArcaCompleta", "EventoArca");
    }
}
