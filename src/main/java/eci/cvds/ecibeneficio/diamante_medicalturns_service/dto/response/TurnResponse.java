package eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.response;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.SpecialityEnum;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TurnResponse {
  private String code;
  private String userName;
  private SpecialityEnum speciality;
  private LocalDateTime date;
}
