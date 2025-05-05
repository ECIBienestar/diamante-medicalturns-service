package eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.response;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.RoleEnum;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.SpecialityEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DoctorResponse extends UserResponse {
  private SpecialityEnum speciality;

  public DoctorResponse(String id, String name, RoleEnum role, SpecialityEnum speciality) {
    super(id, name, role);
    this.speciality = speciality;
  }
}
