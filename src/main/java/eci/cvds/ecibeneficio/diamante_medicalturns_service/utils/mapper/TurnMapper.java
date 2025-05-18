package eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.mapper;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.response.TurnResponse;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.response.UserResponse;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.Turn;

public class TurnMapper {

  private TurnMapper() {}

  public static TurnResponse toResponse(Turn turn) {
    UserResponse user =
        new UserResponse(
            turn.getUser().getName(), turn.getUser().getId(), turn.getUser().getRole());

    return new TurnResponse(
        turn.getId(), turn.getCode(), user, turn.getSpeciality(), turn.hasPriority());
  }
}
