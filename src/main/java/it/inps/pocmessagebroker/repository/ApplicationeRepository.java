package it.inps.pocmessagebroker.repository;

import it.inps.pocmessagebroker.model.Applicazione;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationeRepository extends JpaRepository<Applicazione, Long> {
}
