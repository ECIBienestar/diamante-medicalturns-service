package eci.cvds.ecibeneficio.diamante_medicalturns_service.service.impl;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request.CreateUserRequest;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.response.UserResponse;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.exception.MedicalTurnsException;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.factory.UserFactory;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.factory.UserResponseFactory;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.User;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.repository.UserRepository;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final UserFactory userFactory;
  private final UserResponseFactory userResponseFactory;

  @Override
  public void createUser(CreateUserRequest user) {
    User newUser = userFactory.createUser(user);
    userRepository.save(newUser);
  }

  @Override
  public UserResponse getUser(String id) {
    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new MedicalTurnsException(MedicalTurnsException.USER_NOT_FOUND));

    return userResponseFactory.createResponse(user);
  }
}
