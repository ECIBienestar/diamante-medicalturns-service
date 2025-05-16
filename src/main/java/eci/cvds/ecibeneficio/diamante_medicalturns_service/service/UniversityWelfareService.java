package eci.cvds.ecibeneficio.diamante_medicalturns_service.service;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request.CreateTurnRequest;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.response.TurnResponse;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.SpecialityEnum;
import java.util.List;
import java.util.Optional;

public interface UniversityWelfareService {
  TurnResponse addTurn(CreateTurnRequest turn);

  List<TurnResponse> getTurns();

  List<TurnResponse> getTurns(SpecialityEnum speciality);

  Optional<TurnResponse> getCurrentTurn(SpecialityEnum speciality);

  Optional<TurnResponse> getLastCurrentTurn();

  TurnResponse callNextTurn(SpecialityEnum speciality, int levelAttention);

  TurnResponse callNextTurn(Long nextTurn, SpecialityEnum speciality, int levelAttention);

  void skipTurn(SpecialityEnum speciality);

  void disableTurns();

  void enableTurns();

  void disableTurns(SpecialityEnum speciality);

  void enableTurns(SpecialityEnum speciality);
}
