package it.inps.pocmessagebroker.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "applicazioni")
public class Applicazione implements Serializable {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    /*
    @Column(name = "app_name")
    private String appName;

    @Column(name = "app_key")
    private String appKey;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "identity_provider")
    private String identityProvvider;
*/
    @Column(name = "codice_archivio")
    private String codiceArchivio;

    @Column(name = "progetto")
    private String progetto;

    @Column(name = "queue")
    private String queue;
}
