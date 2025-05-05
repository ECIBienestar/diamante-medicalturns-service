package eci.cvds.ecibeneficio.diamante_medicalturns_service.service;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request.CreateTurnRequest;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.Doctor;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.Turn;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.repository.projection.AverageLevelByRole;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.repository.projection.AverageLevelBySpeciality;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.repository.projection.CountByRole;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.repository.projection.CountBySpeciality;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.RoleEnum;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.SpecialityEnum;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.StatusEnum;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TurnService {
  Turn createTurn(CreateTurnRequest turn);

  List<Turn> getTurns();

  List<Turn> getTurns(SpecialityEnum speciality);

  Turn getTurn(Long id);

  Optional<Turn> getCurrentTurn(SpecialityEnum speciality);

  Optional<Turn> getLastTurn(SpecialityEnum speciality);

  void finishTurn(SpecialityEnum speciality, int levelAttention, Doctor doctor);

  void startTurn(Turn turn);

  List<AverageLevelByRole> getAverageLevelAttentionByRole(
      RoleEnum role, LocalDate start, LocalDate end);

  List<CountByRole> getTurnCountByRole(
      RoleEnum role, LocalDate start, LocalDate end, StatusEnum status);

  List<AverageLevelBySpeciality> getAverageLevelAttentionBySpeciality(
      SpecialityEnum speciality, LocalDate start, LocalDate end);

  List<CountBySpeciality> getTurnCountBySpeciality(
      SpecialityEnum speciality, LocalDate start, LocalDate end, StatusEnum status);
}
