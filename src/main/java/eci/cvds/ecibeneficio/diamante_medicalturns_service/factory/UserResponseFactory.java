package eci.cvds.ecibeneficio.diamante_medicalturns_service.factory;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.response.UserResponse;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.User;

public interface UserResponseFactory {
  UserResponse createResponse(User user);
}
