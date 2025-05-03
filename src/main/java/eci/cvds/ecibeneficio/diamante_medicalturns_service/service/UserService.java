package eci.cvds.ecibeneficio.diamante_medicalturns_service.service;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request.CreateUserRequest;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.User;
import java.util.Optional;

public interface UserService {
  void createUser(CreateUserRequest user);

  Optional<User> getUser(String id);
}
