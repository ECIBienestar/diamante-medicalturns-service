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

  Optional<TurnResponse> getCurrentTurn();

  Optional<TurnResponse> getCurrentTurn(SpecialityEnum speciality);

  TurnResponse callNextTurn(String doctorId, SpecialityEnum speciality, int levelAttention);

  TurnResponse callNextTurn(
      String doctorId, Long nextTurn, SpecialityEnum speciality, int levelAttention);

  void disableTurns();

  void enableTurns();

  void disableTurns(SpecialityEnum speciality);

  void enableTurns(SpecialityEnum speciality);
}
