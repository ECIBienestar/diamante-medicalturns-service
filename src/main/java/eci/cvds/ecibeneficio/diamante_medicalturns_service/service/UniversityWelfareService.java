package eci.cvds.ecibeneficio.diamante_medicalturns_service.service;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request.CreateTurnRequest;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.response.TurnResponse;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.SpecialityEnum;
import java.util.List;

public interface UniversityWelfareService {
  TurnResponse addTurn(CreateTurnRequest turn);

  List<TurnResponse> getTurns();

  List<TurnResponse> getTurns(SpecialityEnum speciality);

  TurnResponse getLastTurn();

  TurnResponse getLastTurn(SpecialityEnum speciality);

  TurnResponse callNextTurn(SpecialityEnum speciality, int levelAttention);

  TurnResponse callNextTurn(Long nextTurn, SpecialityEnum speciality, int levelAttention);

  void disableTurns();

  void enableTurns();

  void disableTurns(SpecialityEnum speciality);

  void enableTurns(SpecialityEnum speciality);
}
