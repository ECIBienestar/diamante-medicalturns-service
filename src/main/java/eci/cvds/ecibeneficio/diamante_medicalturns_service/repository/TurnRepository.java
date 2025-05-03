package eci.cvds.ecibeneficio.diamante_medicalturns_service.repository;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.Turn;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.SpecialityEnum;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.StatusEnum;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TurnRepository extends JpaRepository<Turn, Long> {
  @Query(
      "SELECT t FROM Turn t WHERE t.date BETWEEN :startOfDay AND :endOfDay "
          + "AND (:speciality IS NULL OR t.speciality = :speciality) "
          + "ORDER BY t.priority DESC NULLS LAST, t.date ASC")
  List<Turn> findTurnsForToday(
      @Param("startOfDay") LocalDateTime startOfDay,
      @Param("endOfDay") LocalDateTime endOfDay,
      @Param("speciality") SpecialityEnum speciality);

  @Query("SELECT t FROM Turn t WHERE  t.status = :status AND t.speciality = :speciality")
  Optional<Turn> findCurrentTurn(
      @Param("status") StatusEnum status, @Param("speciality") SpecialityEnum speciality);
}
