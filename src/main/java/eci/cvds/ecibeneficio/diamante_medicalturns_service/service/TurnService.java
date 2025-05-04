package eci.cvds.ecibeneficio.diamante_medicalturns_service.service;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request.CreateTurnRequest;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.Doctor;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.Turn;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.SpecialityEnum;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.StatusEnum;
import java.util.List;
import java.util.Optional;

public interface TurnService {
  Turn createTurn(CreateTurnRequest turn);

  List<Turn> getTurns();

  List<Turn> getTurns(SpecialityEnum speciality);

  Turn getTurn(Long id);

  Optional<Turn> getCurrentTurn(SpecialityEnum speciality);

  Optional<Turn> getLastTurn(SpecialityEnum speciality);

  void updateStatus(Long id, StatusEnum status);

  void updateLevelAttention(Long id, int levelAttention);

  void updateDoctor(Long turnId, Doctor doctor);
}
