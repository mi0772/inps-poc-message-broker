package it.inps.pocmessagebroker.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

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

    @Override
    public String toString() {
        return "EventoArcaPending{" +
                "id=" + id +
                ", idApplicazione=" + idApplicazione +
                ", arcaKey='" + arcaKey + '\'' +
                ", stato=" + stato +
                ", xml=" + xml +
                '}';
    }
}
