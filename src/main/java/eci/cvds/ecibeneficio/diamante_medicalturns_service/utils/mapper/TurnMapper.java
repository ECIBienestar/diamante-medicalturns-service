package eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.mapper;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.response.TurnResponse;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.Turn;

public class TurnMapper {
  public static TurnResponse toResponse(Turn turn) {
    return new TurnResponse(
        turn.getCode(), turn.getUser().getName(), turn.getSpeciality(), turn.getDate());
  }
}
