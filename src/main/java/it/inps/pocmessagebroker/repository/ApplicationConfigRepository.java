package it.inps.pocmessagebroker.repository;

import it.inps.pocmessagebroker.domain.ApplicationConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationConfigRepository extends JpaRepository<ApplicationConfig, Long> {
}
