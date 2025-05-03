package eci.cvds.ecibeneficio.diamante_medicalturns_service.service;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request.CreateTurnRequest;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.response.TurnResponse;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.SpecialityEnum;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.StatusEnum;
import java.util.List;

public interface TurnService {
  TurnResponse createTurn(CreateTurnRequest turn);

  List<TurnResponse> getTurns();

  List<TurnResponse> getTurns(SpecialityEnum speciality);

  TurnResponse getTurn(Long id);

  TurnResponse getCurrentTurn(SpecialityEnum speciality);

  void setStatus(Long id, StatusEnum status);

  void setLevelAttention(Long id, int levelAttention);
}
