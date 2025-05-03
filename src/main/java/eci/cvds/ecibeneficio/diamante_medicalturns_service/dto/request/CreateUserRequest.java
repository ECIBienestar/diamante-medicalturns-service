package eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.RoleEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateUserRequest {
  private String id;
  private String name;
  private RoleEnum role;
}
