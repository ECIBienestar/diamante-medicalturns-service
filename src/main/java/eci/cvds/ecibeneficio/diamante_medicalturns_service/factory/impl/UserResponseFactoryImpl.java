package eci.cvds.ecibeneficio.diamante_medicalturns_service.factory.impl;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.response.DoctorResponse;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.response.UserResponse;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.factory.UserResponseFactory;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.Doctor;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.User;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.RoleEnum;
import org.springframework.stereotype.Component;

@Component
public class UserResponseFactoryImpl implements UserResponseFactory {

  @Override
  public UserResponse createResponse(User user) {
    if (user.getRole().equals(RoleEnum.DOCTOR)) {
      return createDoctorResponse((Doctor) user);
    } else {
      return createUserResponse(user);
    }
  }

  private UserResponse createDoctorResponse(Doctor doctor) {
    return new DoctorResponse(
        doctor.getId(), doctor.getName(), doctor.getRole(), doctor.getSpeciality());
  }

  private UserResponse createUserResponse(User user) {
    return new UserResponse(user.getId(), user.getName(), user.getRole());
  }
}
