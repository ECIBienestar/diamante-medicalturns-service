package eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.SpecialityEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SkipTurnRequest {
  private String doctorId;
  private SpecialityEnum speciality;
}
