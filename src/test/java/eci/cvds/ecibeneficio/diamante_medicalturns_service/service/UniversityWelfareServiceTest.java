package eci.cvds.ecibeneficio.diamante_medicalturns_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request.CreateTurnRequest;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request.CreateUserRequest;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.response.TurnResponse;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.exception.MedicalTurnsException;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.Turn;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.UniversityWelfare;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.User;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.repository.UniversityWelfareRepository;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.service.impl.UniversityWelfareServiceImpl;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.PriorityEnum;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.RoleEnum;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.SpecialityEnum;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class UniversityWelfareServiceTest {

  @InjectMocks private UniversityWelfareServiceImpl universityWelfareService;

  @Mock private TurnService turnService; // El servicio TurnService que maneja la lógica de turnos

  @Mock private UniversityWelfareRepository universityWelfareRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this); // Inicializa los mocks antes de cada test
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

    List<Turn> expectedTurns =
        List.of(
            new Turn(user1, "123", SpecialityEnum.MEDICINA_GENERAL, LocalDateTime.now()),
            new Turn(user2, "124", SpecialityEnum.MEDICINA_GENERAL, LocalDateTime.now()));

    when(turnService.getTurns(SpecialityEnum.MEDICINA_GENERAL)).thenReturn(expectedTurns);

    List<TurnResponse> actualTurns =
        universityWelfareService.getTurns(SpecialityEnum.MEDICINA_GENERAL);

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

    when(turnService.getCurrentTurn(SpecialityEnum.MEDICINA_GENERAL))
        .thenReturn(Optional.of(expectedTurn));

    Optional<TurnResponse> actualTurn =
        universityWelfareService.getCurrentTurn(SpecialityEnum.MEDICINA_GENERAL);

    assertTrue(actualTurn.isPresent());
    assertEquals("123", actualTurn.get().getCode());
    assertEquals("Juan", actualTurn.get().getUserName());
    assertEquals(SpecialityEnum.MEDICINA_GENERAL, actualTurn.get().getSpeciality());

    verify(turnService, times(1)).getCurrentTurn(SpecialityEnum.MEDICINA_GENERAL);
  }

  @Test
  void testSkipTurn() {
    // Mock de turnService.skipTurn
    doNothing().when(turnService).skipTurn(SpecialityEnum.MEDICINA_GENERAL);

    // Ejecutar el método
    universityWelfareService.skipTurn(SpecialityEnum.MEDICINA_GENERAL);

    // Verificar que se llamó correctamente
    verify(turnService, times(1)).skipTurn(SpecialityEnum.MEDICINA_GENERAL);
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

  @Test
  void testDisableTurnsBySpeciality_success() {
    UniversityWelfare welfare = new UniversityWelfare();
    welfare.setDisableTurns(false);

    when(universityWelfareRepository.getUniversityWelfare()).thenReturn(welfare);

    universityWelfareService.disableTurns(SpecialityEnum.MEDICINA_GENERAL);

    verify(universityWelfareRepository).save(welfare);
    assertTrue(welfare.getDisbaleTurnsBySpeciality().contains(SpecialityEnum.MEDICINA_GENERAL));
  }

  @Test
  void testDisableTurnsBySpeciality_alreadyDisabled() {
    UniversityWelfare welfare = new UniversityWelfare();
    welfare.setDisableTurns(true);

    when(universityWelfareRepository.getUniversityWelfare()).thenReturn(welfare);

    assertThrows(
        MedicalTurnsException.class,
        () -> universityWelfareService.disableTurns(SpecialityEnum.MEDICINA_GENERAL));
  }

  @Test
  void testEnableTurnsBySpeciality_success() {
    UniversityWelfare welfare = new UniversityWelfare();
    welfare.setDisableTurns(false);
    welfare.disableTurns(SpecialityEnum.PSICOLOGIA);

    when(universityWelfareRepository.getUniversityWelfare()).thenReturn(welfare);

    universityWelfareService.enableTurns(SpecialityEnum.PSICOLOGIA);

    verify(universityWelfareRepository).save(welfare);
    assertFalse(welfare.getDisbaleTurnsBySpeciality().contains(SpecialityEnum.PSICOLOGIA));
  }

  @Test
  void testEnableTurnsBySpeciality_globallyDisabled() {
    UniversityWelfare welfare = new UniversityWelfare();
    welfare.setDisableTurns(true);

    when(universityWelfareRepository.getUniversityWelfare()).thenReturn(welfare);

    assertThrows(
        MedicalTurnsException.class,
        () -> universityWelfareService.enableTurns(SpecialityEnum.ODONTOLOGIA));
  }

  @Test
  void testCallNextTurnById_success() {
    Turn turn = new Turn();

    // Agregar usuario simulado al Turn
    User user = new User();
    user.setId("user1");
    user.setName("Juan Pérez");
    user.setRole(RoleEnum.ESTUDIANTE);

    turn.setUser(user);
    turn.setCode("T001");
    turn.setSpeciality(SpecialityEnum.MEDICINA_GENERAL);
    turn.setDate(LocalDateTime.now());

    Long turnId = 1L;

    when(turnService.getTurn(turnId)).thenReturn(turn);
    when(turnService.startTurn(turn)).thenReturn(turn);

    TurnResponse response =
        universityWelfareService.callNextTurn(turnId, SpecialityEnum.MEDICINA_GENERAL, 1);

    verify(turnService).finishTurn(SpecialityEnum.MEDICINA_GENERAL, 1);
    assertNotNull(response);
    assertEquals("T001", response.getCode());
    assertEquals("Juan Pérez", response.getUserName());
  }

  @Test
  void testCallNextTurn_success() {
    User user = new User();
    user.setId("user1");
    user.setName("Ana");

    Turn nextTurn = new Turn();
    nextTurn.setCode("CODE123");
    nextTurn.setUser(user);

    when(turnService.startNextTurn(SpecialityEnum.ODONTOLOGIA)).thenReturn(nextTurn);

    TurnResponse response = universityWelfareService.callNextTurn(SpecialityEnum.ODONTOLOGIA, 2);

    verify(turnService).finishTurn(SpecialityEnum.ODONTOLOGIA, 2);
    assertNotNull(response);
    assertEquals("CODE123", response.getCode());
    assertEquals("Ana", response.getUserName());
  }

  @Test
  void testAddTurn_success() {
    CreateTurnRequest request = new CreateTurnRequest();
    request.setPriority(PriorityEnum.DISCAPACIDAD);
    request.setSpeciality(SpecialityEnum.PSICOLOGIA);

    // Simular usuario
    User user = new User();
    user.setId("123");
    user.setName("Juan Pérez");

    // Simular turno con usuario
    Turn turn = new Turn();
    turn.setCode("T1");
    turn.setUser(user);

    when(universityWelfareRepository.getUniversityWelfare()).thenReturn(new UniversityWelfare());
    when(turnService.createTurn(request)).thenReturn(turn);

    TurnResponse response = universityWelfareService.addTurn(request);

    assertEquals("T1", response.getCode());
    assertEquals("Juan Pérez", response.getUserName());
  }

  @Test
  void testAddTurn_whenGlobalDisabled() {
    UniversityWelfare welfare = new UniversityWelfare();
    welfare.setDisableTurns(true);

    when(universityWelfareRepository.getUniversityWelfare()).thenReturn(welfare);

    CreateTurnRequest request = new CreateTurnRequest();
    request.setSpeciality(SpecialityEnum.MEDICINA_GENERAL);

    assertThrows(MedicalTurnsException.class, () -> universityWelfareService.addTurn(request));
  }

  @Test
  void testGetAllTurns() {
    User user = new User();
    user.setId("123");
    user.setName("Juan");
    user.setRole(RoleEnum.ESTUDIANTE);

    Turn turn1 = new Turn();
    turn1.setUser(user);
    turn1.setCode("T1");
    turn1.setSpeciality(SpecialityEnum.MEDICINA_GENERAL);
    turn1.setDate(LocalDateTime.now());

    Turn turn2 = new Turn();
    turn2.setUser(user);
    turn2.setCode("T2");
    turn2.setSpeciality(SpecialityEnum.PSICOLOGIA);
    turn2.setDate(LocalDateTime.now());

    when(turnService.getTurns()).thenReturn(List.of(turn1, turn2));

    List<TurnResponse> responses = universityWelfareService.getTurns();

    assertEquals(2, responses.size());
    assertEquals("Juan", responses.get(0).getUserName());
  }

  @Test
  void testGetLastCurrentTurn() {
    User user = new User();
    user.setId("user123");
    user.setName("John Doe");

    Turn turn = new Turn();
    turn.setCode("ULTIMO");
    turn.setUser(user); // <-- Aquí está la solución

    when(turnService.getLastCurrentTurn()).thenReturn(Optional.of(turn));

    Optional<TurnResponse> response = universityWelfareService.getLastCurrentTurn();

    assertTrue(response.isPresent());
    assertEquals("ULTIMO", response.get().getCode());
    assertEquals("John Doe", response.get().getUserName()); // Verificamos que no haya NullPointer
  }
}
