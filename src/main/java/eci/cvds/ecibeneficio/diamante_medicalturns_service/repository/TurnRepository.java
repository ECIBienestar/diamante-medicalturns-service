package eci.cvds.ecibeneficio.diamante_medicalturns_service.repository;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.Turn;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.repository.projection.AverageLevelByRole;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.repository.projection.AverageLevelBySpeciality;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.repository.projection.CountByRole;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.repository.projection.CountBySpeciality;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.SpecialityEnum;
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
      "SELECT t FROM Turn t WHERE t.date BETWEEN :startOfDay AND :endOfDay AND t.user.id = :userId AND t.status != eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.StatusEnum.COMPLETED AND t.status != eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.StatusEnum.FINISHED")
  Optional<Turn> findUserCurrrentTurn(
      @Param("startOfDay") LocalDateTime startOfDay,
      @Param("endOfDay") LocalDateTime endOfDay,
      @Param("userId") String userId);

  @Query(
      "SELECT t FROM Turn t WHERE t.date BETWEEN :startOfDay AND :endOfDay AND t.status = eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.StatusEnum.PENDING "
          + "AND (:speciality IS NULL OR t.speciality = :speciality) "
          + "ORDER BY t.priority DESC NULLS LAST, t.date ASC")
  List<Turn> findTurnsForToday(
      @Param("startOfDay") LocalDateTime startOfDay,
      @Param("endOfDay") LocalDateTime endOfDay,
      @Param("speciality") SpecialityEnum speciality);

  @Query(
      "SELECT t FROM Turn t WHERE t.date BETWEEN :startOfDay AND :endOfDay AND t.status = eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.StatusEnum.CURRENT AND t.speciality = :speciality")
  Optional<Turn> findCurrentTurn(
      @Param("startOfDay") LocalDateTime startOfDay,
      @Param("endOfDay") LocalDateTime endOfDay,
      @Param("speciality") SpecialityEnum speciality);

  @Query(
      "SELECT t FROM Turn t WHERE t.date BETWEEN :startOfDay AND :endOfDay AND t.status = eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.StatusEnum.CURRENT ORDER BY t.dateAttention DESC")
  List<Turn> findLastCurrentTurn(
      @Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

  @Query(
      "SELECT t.user.role AS role, AVG(t.levelAttention) AS averageLevel FROM Turn t WHERE t.date BETWEEN :startOfDay AND :endOfDay AND t.status = eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.StatusEnum.COMPLETED GROUP BY t.user.role")
  List<AverageLevelByRole> getAverageLevelAttentionByRole(
      @Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

  @Query(
      "SELECT t.user.role AS role, t.status AS status, COUNT(t) AS count FROM Turn t WHERE t.date BETWEEN :startOfDay AND :endOfDay GROUP BY t.user.role, t.status")
  List<CountByRole> getTurnCountByRole(
      @Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

  @Query(
      "SELECT t.speciality AS speciality, AVG(t.levelAttention) AS averageLevel FROM Turn t WHERE t.date BETWEEN :startOfDay AND :endOfDay AND t.status = eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.StatusEnum.COMPLETED GROUP BY t.speciality")
  List<AverageLevelBySpeciality> getAverageLevelAttentionBySpeciality(
      @Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

  @Query(
      "SELECT t.speciality AS speciality, t.status AS status, COUNT(t) AS count FROM Turn t WHERE t.date BETWEEN :startOfDay AND :endOfDay GROUP BY t.speciality, t.status")
  List<CountBySpeciality> getTurnCountBySpeciality(
      @Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);
}
