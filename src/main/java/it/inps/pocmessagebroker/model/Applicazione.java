package it.inps.pocmessagebroker.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "applicazioni")
public class Applicazione {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "app_name")
    private String appName;

    @Column(name = "app_key")
    private String appKey;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "identity_provider")
    private String identityProvvider;

    @Column(name = "codice_archivio")
    private String codiceArchivio;

    @Column(name = "progetto")
    private String progetto;

    @Override
    public String toString() {
        return "Applicazione{" +
                "id=" + id +
                ", appName='" + appName + '\'' +
                ", appKey='" + appKey + '\'' +
                ", userId='" + userId + '\'' +
                ", identityProvvider='" + identityProvvider + '\'' +
                ", codiceArchivio='" + codiceArchivio + '\'' +
                ", progetto='" + progetto + '\'' +
                '}';
    }
}
