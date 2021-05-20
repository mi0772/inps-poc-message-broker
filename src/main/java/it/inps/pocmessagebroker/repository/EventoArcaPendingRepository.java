package it.inps.pocmessagebroker.repository;

import it.inps.pocmessagebroker.model.EventoArcaPending;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoArcaPendingRepository extends JpaRepository<EventoArcaPending, Long> {
}
