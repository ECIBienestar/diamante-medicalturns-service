package eci.cvds.ecibeneficio.diamante_medicalturns_service.factory.impl;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request.CreateDoctorRequest;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request.CreateUserRequest;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.factory.UserFactory;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.Doctor;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.User;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.RoleEnum;
import org.springframework.stereotype.Component;

@Component
public class UserFactoryImpl implements UserFactory {

  @Override
  public User createUser(CreateUserRequest user) {
    if (user.getRole().equals(RoleEnum.DOCTOR)) {
      return createDoctor((CreateDoctorRequest) user);
    } else {
      return createSimpleUser(user);
    }
  }

  private User createDoctor(CreateDoctorRequest doctor) {
    return new Doctor(doctor.getId(), doctor.getName(), doctor.getRole(), doctor.getSpeciality());
  }

  private User createSimpleUser(CreateUserRequest user) {
    return new User(user.getId(), user.getName(), user.getRole());
  }
}
