DROP TABLE IF EXISTS applicazioni;

CREATE TABLE applicazioni (
                              id INT AUTO_INCREMENT  PRIMARY KEY,
                              app_name VARCHAR(250) NOT NULL,
                              app_key VARCHAR(250) NOT NULL,
                              user_id VARCHAR(250) DEFAULT NULL,
                              identity_provider VARCHAR(250) DEFAULT NULL,
                              codice_archivio VARCHAR(250) DEFAULT NULL,
                              progetto VARCHAR(250) DEFAULT NULL

);

INSERT INTO applicazioni (app_name, app_key, user_id, identity_provider, codice_archivio, progetto) VALUES
('WA00405', 'WA00405', 'WA00405', 'AD','1101','A');

DROP TABLE IF EXISTS arca_events_pending;

CREATE TABLE arca_events_pending (
    id INT AUTO_INCREMENT PRIMARY KEY ,
    id_applicazione INT NOT NULL,
    arca_key VARCHAR(11),
    stato INT
)
