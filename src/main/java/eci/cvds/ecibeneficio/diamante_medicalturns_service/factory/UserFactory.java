package eci.cvds.ecibeneficio.diamante_medicalturns_service.factory;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request.CreateUserRequest;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.User;

public interface UserFactory {
  User createUser(CreateUserRequest user);
}
