package eci.cvds.ecibeneficio.diamante_medicalturns_service.repository.projection;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.RoleEnum;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.StatusEnum;

public interface CountByRole {
  RoleEnum getRole();

  StatusEnum getStatus();

  Long getCount();
}
