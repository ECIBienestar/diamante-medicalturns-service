package eci.cvds.ecibeneficio.diamante_medicalturns_service.repository.projection;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.RoleEnum;

public interface AverageLevelByRole {
  RoleEnum getRole();

  Double getAverageLevel();
}
