package eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.response;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
  private String id;
  private String name;
  private RoleEnum role;
}
