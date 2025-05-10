package eci.cvds.ecibeneficio.diamante_medicalturns_service.factory;

import static org.junit.jupiter.api.Assertions.*;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request.CreateDoctorRequest;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request.CreateUserRequest;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.factory.impl.UserFactoryImpl;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.Doctor;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.User;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.RoleEnum;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.SpecialityEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserFactoryImplTest {
  private UserFactoryImpl userFactory;

  @BeforeEach
  public void setUp() {
    userFactory = new UserFactoryImpl();
  }

  @Test
  public void testCreateSimpleUser() {
    CreateUserRequest request = new CreateUserRequest();
    request.setId("123");
    request.setName("Daniel Diaz");
    request.setRole(RoleEnum.ESTUDIANTE);

    User user = userFactory.createUser(request);

    assertNotNull(user);
    assertEquals("123", user.getId());
    assertEquals("Daniel Diaz", user.getName());
    assertEquals(RoleEnum.ESTUDIANTE, user.getRole());
    assertFalse(user instanceof Doctor);
  }

  @Test
  public void testCreateDoctor() {
    CreateDoctorRequest request = new CreateDoctorRequest();
    request.setId("123");
    request.setName("Daniel Diaz");
    request.setRole(RoleEnum.DOCTOR);
    request.setSpeciality(SpecialityEnum.MEDICINA_GENERAL);

    User user = userFactory.createUser(request);

    assertNotNull(user);
    assertInstanceOf(Doctor.class, user);

    Doctor doctor = (Doctor) user;

    assertEquals("123", doctor.getId());
    assertEquals("Daniel Diaz", doctor.getName());
    assertEquals(RoleEnum.DOCTOR, doctor.getRole());
    assertEquals(SpecialityEnum.MEDICINA_GENERAL, doctor.getSpeciality());
  }
}
