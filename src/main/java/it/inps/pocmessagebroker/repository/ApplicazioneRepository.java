package it.inps.pocmessagebroker.repository;

import it.inps.pocmessagebroker.domain.Applicazione;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicazioneRepository extends JpaRepository<Applicazione, Long> {
}
