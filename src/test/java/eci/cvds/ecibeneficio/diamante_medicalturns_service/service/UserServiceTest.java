package eci.cvds.ecibeneficio.diamante_medicalturns_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request.CreateUserRequest;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.User;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.repository.UserRepository;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.service.impl.UserServiceImpl;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.RoleEnum;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class UserServiceTest {

  @Mock private UserRepository userRepository;

  @InjectMocks private UserServiceImpl userService;

  private CreateUserRequest createUserRequest;
  private User mockUser;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    createUserRequest = new CreateUserRequest();
    createUserRequest.setId("12345");
    createUserRequest.setName("John Doe");
    createUserRequest.setRole(RoleEnum.STUDENT);

    mockUser = new User("12345", "John Doe", RoleEnum.STUDENT);
  }

  @Test
  void testCreateUser() {
    userService.createUser(createUserRequest);

    ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
    verify(userRepository, times(1)).save(userCaptor.capture());
  }

  @Test
  void testGetUser_ExistingUser() {
    when(userRepository.findById("12345")).thenReturn(Optional.of(mockUser));

    Optional<User> foundUser = userService.getUser("12345");

    assertTrue(foundUser.isPresent());
    assertEquals("John Doe", foundUser.get().getName());
    assertEquals(RoleEnum.STUDENT, foundUser.get().getRole());
  }

  @Test
  void testGetUser_NonExistingUser() {
    when(userRepository.findById("67890")).thenReturn(Optional.empty());

    Optional<User> foundUser = userService.getUser("67890");

    assertFalse(foundUser.isPresent());
  }
}
