package eci.cvds.ecibeneficio.diamante_medicalturns_service.service.impl;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request.CreateUserRequest;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.User;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.repository.UserRepository;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.service.UserService;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;

  @Override
  public void createUser(CreateUserRequest user) {
    User newUser = new User(user.getId(), user.getName(), user.getRole());
    userRepository.save(newUser);
  }

  @Override
  public Optional<User> getUser(String id) {
    return userRepository.findById(id);
  }
}
