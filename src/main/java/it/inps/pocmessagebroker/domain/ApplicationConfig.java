package it.inps.pocmessagebroker.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "application_config")
public class ApplicationConfig {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "app_name")
    private String appName;

    @Column(name = "app_key")
    private String appKey;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "identity_provider")
    private String identityProvider;

    @Column(name = "codice_tipo_operatore")
    private String codiceTipoOperatore;

    @Column(name = "provenienza")
    private String provenienza;

    @Column(name = "codice_fiscale_operatore")
    private String codiceFiscaleOperatore;

    @Column(name = "matricola_inps")
    private String matricolaINPS;

    @Column(name = "sede_richiesta")
    private String sedeRichiesta;

    @Column(name = "gestione_appl_origine")
    private String gestioneApplOrigine;

    @Column(name = "sede_appl_origine")
    private String sedeApplOrigine;

    @Column(name = "pgm_appl_origine")
    private String pgmApplOrigine;

    @Column(name = "tran_appl_origine")
    private String tranApplOrigine;

    @Column(name = "funzione")
    private String funzione;

    @Column(name = "wsReserved")
    private String wsReserved;

    @Column(name = "profilo")
    private String profilo;

    @Column(name = "tipo_chiave_presente")
    private String tipoChiavePresente;

}
