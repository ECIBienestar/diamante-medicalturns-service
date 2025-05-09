package eci.cvds.ecibeneficio.diamante_medicalturns_service.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request.CreateUserRequest;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.factory.UserFactory;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.User;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.repository.UserRepository;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.service.impl.UserServiceImpl;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.RoleEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserFactory userFactory; // <- agregado

    @InjectMocks
    private UserServiceImpl userService;

    private CreateUserRequest createUserRequest;
    private User mockUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        createUserRequest = new CreateUserRequest();
        createUserRequest.setId("12345");
        createUserRequest.setName("John Doe");
        createUserRequest.setRole(RoleEnum.ESTUDIANTE);

        mockUser = new User("12345", "John Doe", RoleEnum.ESTUDIANTE);
    }

    @Test
    public void testCreateUser() {
        when(userFactory.createUser(any(CreateUserRequest.class))).thenReturn(mockUser);

        userService.createUser(createUserRequest);

        verify(userRepository, times(1)).save(mockUser);
    }

    @Test
    public void testGetUser_ExistingUser() {
        when(userRepository.findById("12345")).thenReturn(Optional.of(mockUser));

        Optional<User> foundUser = userService.getUser("12345");

        assertTrue(foundUser.isPresent());
        assertEquals("John Doe", foundUser.get().getName());
        assertEquals(RoleEnum.ESTUDIANTE, foundUser.get().getRole());
    }

    @Test
    public void testGetUser_NonExistingUser() {
        when(userRepository.findById("67890")).thenReturn(Optional.empty());

        Optional<User> foundUser = userService.getUser("67890");

        assertFalse(foundUser.isPresent());
    }
}
