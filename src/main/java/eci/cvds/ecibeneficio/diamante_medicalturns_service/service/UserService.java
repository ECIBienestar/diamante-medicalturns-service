package eci.cvds.ecibeneficio.diamante_medicalturns_service.service;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request.CreateUserRequest;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.response.UserResponse;

public interface UserService {
  void createUser(CreateUserRequest user);

  UserResponse getUser(String id);
}
