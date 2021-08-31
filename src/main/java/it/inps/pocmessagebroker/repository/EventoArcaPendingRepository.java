package it.inps.pocmessagebroker.repository;

import it.inps.pocmessagebroker.domain.EventoArcaPending;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EventoArcaPendingRepository extends JpaRepository<EventoArcaPending, Long> {

    Optional<EventoArcaPending> findTopByArcaKeyAndIdApplicazioneAndCodiceEvento(@Param("key") String key, @Param("idApplicazione") Long idApplicazione, @Param("codiceEvento") String codiceEvento);

    Optional<EventoArcaPending> findTopByArcaKeyAndIdApplicazione(@Param("key") String key, @Param("idApplicazione") Long idApplicazione);
    List<EventoArcaPending> findAllByStatoIs(@Param("stato") Integer stato);
    List<EventoArcaPending> findAllByStatoIsAndIdApplicazione(@Param("stato") Integer stato, @Param("idApplicazione") Long idApplicazione);
    List<EventoArcaPending> findAllByArcaKeyAndIdApplicazione(@Param("arcaKey") String arcaKey, @Param("idApplicazione") Long idApplicazione);

}
