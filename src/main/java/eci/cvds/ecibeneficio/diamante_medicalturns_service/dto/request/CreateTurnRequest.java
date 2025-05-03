package eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.PriorityEnum;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.SpecialityEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateTurnRequest {
  private CreateUserRequest user;
  private SpecialityEnum speciality;
  private PriorityEnum priority;
}
