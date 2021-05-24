package it.inps.pocmessagebroker.repository;

import it.inps.pocmessagebroker.model.EventoArcaPending;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EventoArcaPendingRepository extends JpaRepository<EventoArcaPending, Long> {

    Optional<EventoArcaPending> findTopByArcaKey(@Param("key") String key);
    List<EventoArcaPending> findAllByStatoIs(@Param("stato") Integer stato);
    List<EventoArcaPending> findAllByStatoIsAndIdApplicazione(@Param("stato") Integer stato, @Param("idApplicazione") Long idApplicazione);
    Optional<EventoArcaPending> findByArcaKeyAndIdApplicazione(@Param("arcaKey") String arcaKey, @Param("idApplicazione") Long idApplicazione);
}
