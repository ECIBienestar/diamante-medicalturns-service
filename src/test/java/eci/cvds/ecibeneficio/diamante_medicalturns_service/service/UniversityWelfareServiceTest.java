package eci.cvds.ecibeneficio.diamante_medicalturns_service.service;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request.CreateTurnRequest;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.response.TurnResponse;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.Doctor;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.Turn;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.UniversityWelfare;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.User;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.repository.UniversityWelfareRepository;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.service.impl.UniversityWelfareServiceImpl;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.RoleEnum;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.SpecialityEnum;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.PriorityEnum;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request.CreateUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

class UniversityWelfareServiceTest {

    @InjectMocks
    private UniversityWelfareServiceImpl universityWelfareService;

    @Mock
    private UserService userService;

    @Mock
    private TurnService turnService;  // El servicio TurnService que maneja la lógica de turnos

    @Mock
    private UniversityWelfareRepository universityWelfareRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Inicializa los mocks antes de cada test
        UniversityWelfare welfare = new UniversityWelfare();
        when(universityWelfareRepository.getUniversityWelfare()).thenReturn(welfare);

    }

    // Prueba de agregar turno
    @Test
    void testAddTurn() {
        // Crear usuario de prueba
        User user = new User("1", "Juan", RoleEnum.ESTUDIANTE);

        // Crear petición de turno
        CreateTurnRequest request = new CreateTurnRequest();
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setName("Juan");
        userRequest.setRole(RoleEnum.ESTUDIANTE);
        request.setUser(userRequest);
        request.setSpeciality(SpecialityEnum.MEDICINA_GENERAL);
        request.setPriority(PriorityEnum.EMBARAZO);

        // Turno esperado con usuario
        Turn expectedTurn = new Turn(user, "123", SpecialityEnum.MEDICINA_GENERAL, LocalDateTime.now());

        when(turnService.createTurn(request)).thenReturn(expectedTurn);

        TurnResponse actualResponse = universityWelfareService.addTurn(request);

        assertNotNull(actualResponse);
        assertEquals("123", actualResponse.getCode());
        assertEquals("Juan", actualResponse.getUserName());
        assertEquals(SpecialityEnum.MEDICINA_GENERAL, actualResponse.getSpeciality());

        verify(turnService, times(1)).createTurn(request);
    }


    @Test
    void testGetTurnsBySpeciality() {
        User user1 = new User("1", "Juan", RoleEnum.ESTUDIANTE);
        User user2 = new User("2", "Pedro", RoleEnum.ESTUDIANTE);

        List<Turn> expectedTurns = List.of(
                new Turn(user1, "123", SpecialityEnum.MEDICINA_GENERAL, LocalDateTime.now()),
                new Turn(user2, "124", SpecialityEnum.MEDICINA_GENERAL, LocalDateTime.now())
        );

        when(turnService.getTurns(SpecialityEnum.MEDICINA_GENERAL)).thenReturn(expectedTurns);

        List<TurnResponse> actualTurns = universityWelfareService.getTurns(SpecialityEnum.MEDICINA_GENERAL);

        assertNotNull(actualTurns);
        assertEquals(2, actualTurns.size());
        assertEquals("Juan", actualTurns.get(0).getUserName());
        assertEquals(SpecialityEnum.MEDICINA_GENERAL, actualTurns.get(0).getSpeciality());

        verify(turnService, times(1)).getTurns(SpecialityEnum.MEDICINA_GENERAL);
    }


    @Test
    void testGetCurrentTurn() {
        User user = new User("1", "Juan", RoleEnum.ESTUDIANTE);
        Turn expectedTurn = new Turn(user, "123", SpecialityEnum.MEDICINA_GENERAL, LocalDateTime.now());

        when(turnService.getCurrentTurn(SpecialityEnum.MEDICINA_GENERAL)).thenReturn(Optional.of(expectedTurn));

        Optional<TurnResponse> actualTurn = universityWelfareService.getCurrentTurn(SpecialityEnum.MEDICINA_GENERAL);

        assertTrue(actualTurn.isPresent());
        assertEquals("123", actualTurn.get().getCode());
        assertEquals("Juan", actualTurn.get().getUserName());
        assertEquals(SpecialityEnum.MEDICINA_GENERAL, actualTurn.get().getSpeciality());

        verify(turnService, times(1)).getCurrentTurn(SpecialityEnum.MEDICINA_GENERAL);
    }


    @Test
    void testSkipTurn() {
        // Crear un mock de Doctor que también es un User
        Doctor doctor = new Doctor();
        doctor.setId("doctorId");

        // userService.getUser() devuelve Optional<User>
        when(userService.getUser("doctorId")).thenReturn(Optional.of(doctor));

        // Mock de turnService.skipTurn
        doNothing().when(turnService).skipTurn(SpecialityEnum.MEDICINA_GENERAL, doctor);

        // Ejecutar el método
        universityWelfareService.skipTurn("doctorId", SpecialityEnum.MEDICINA_GENERAL);

        // Verificar que se llamó correctamente
        verify(turnService, times(1)).skipTurn(SpecialityEnum.MEDICINA_GENERAL, doctor);
    }



    @Test
    void testDisableTurns() {
        UniversityWelfare welfare = new UniversityWelfare();
        welfare.setDisableTurns(false);

        when(universityWelfareRepository.getUniversityWelfare()).thenReturn(welfare);
        when(universityWelfareRepository.save(any(UniversityWelfare.class))).thenReturn(welfare);

        universityWelfareService.disableTurns();

        assertTrue(welfare.isDisableTurns()); // validamos el cambio en el objeto
        verify(universityWelfareRepository, times(1)).getUniversityWelfare();
        verify(universityWelfareRepository, times(1)).save(welfare);
    }

    @Test
    void testEnableTurns() {
        UniversityWelfare welfare = new UniversityWelfare();
        welfare.setDisableTurns(true);

        when(universityWelfareRepository.getUniversityWelfare()).thenReturn(welfare);
        when(universityWelfareRepository.save(any(UniversityWelfare.class))).thenReturn(welfare);

        universityWelfareService.enableTurns();

        assertFalse(welfare.isDisableTurns()); // validamos el cambio en el objeto
        verify(universityWelfareRepository, times(1)).getUniversityWelfare();
        verify(universityWelfareRepository, times(1)).save(welfare);
    }
}
