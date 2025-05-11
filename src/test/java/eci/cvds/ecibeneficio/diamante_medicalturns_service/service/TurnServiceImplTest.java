package eci.cvds.ecibeneficio.diamante_medicalturns_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request.CreateTurnRequest;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request.CreateUserRequest;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.exception.MedicalTurnsException;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.factory.UserFactory;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.Doctor;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.Turn;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.User;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.repository.TurnRepository;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.repository.UserRepository;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.repository.projection.AverageLevelByRole;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.repository.projection.AverageLevelBySpeciality;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.repository.projection.CountByRole;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.repository.projection.CountBySpeciality;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.service.impl.TurnServiceImpl;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.PriorityEnum;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.RoleEnum;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.SpecialityEnum;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.StatusEnum;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TurnServiceImplTest {
  @Mock private TurnRepository turnRepository;
  @Mock private UserRepository userRepository;
  @Mock private UserService userService;
  @Mock private SpecialitySequenceService specialitySequenceService;
  @Mock private UserFactory userFactory;

  @InjectMocks private TurnServiceImpl turnServiceImpl;

  private CreateUserRequest createUserRequest;
  private CreateTurnRequest createTurnRequest;
  private User user;
  private Turn turn;
  private LocalDateTime dateTime;

  @BeforeEach
  void setUp() {
    createUserRequest = getCreateUserRequest();
    createTurnRequest = getCreateTurnRequest();
    user = getUser();
    turn = getTurn();
  }

  @Test
  void shouldCreateTurn() {
    when(userService.getUser(user.getId())).thenReturn(Optional.of(user));
    when(turnRepository.findUserCurrrentTurn(any(), any(), eq(user.getId())))
        .thenReturn(Optional.empty());
    when(specialitySequenceService.getSequence(SpecialityEnum.MEDICINA_GENERAL)).thenReturn(0);
    when(turnRepository.save(any())).thenAnswer(i -> i.getArgument(0));

    Turn result = turnServiceImpl.createTurn(createTurnRequest);
    result.setDate(dateTime);

    assertNotNull(result);
    assertEquals(result, turn);
    verify(specialitySequenceService).incrementSequence(SpecialityEnum.MEDICINA_GENERAL);
  }

  @Test
  void shouldThrowExceptionIfUserHasTurn() {
    when(userService.getUser(user.getId())).thenReturn(Optional.of(user));
    when(turnRepository.findUserCurrrentTurn(any(), any(), eq(user.getId())))
        .thenReturn(Optional.of(new Turn()));

    assertThrows(MedicalTurnsException.class, () -> turnServiceImpl.createTurn(createTurnRequest));
  }

  @Test
  void shouldSetPriorityWhenPriorityIsNotNull() {
    when(userService.getUser(user.getId())).thenReturn(Optional.of(user));
    when(turnRepository.findUserCurrrentTurn(any(), any(), eq(user.getId())))
        .thenReturn(Optional.empty());
    when(turnRepository.save(any())).thenAnswer(i -> i.getArgument(0));

    createTurnRequest.setPriority(PriorityEnum.EMBARAZO);
    Turn result = turnServiceImpl.createTurn(createTurnRequest);

    assertNotNull(result);
    assertEquals(PriorityEnum.EMBARAZO, result.getPriority());
  }

  @Test
  void shouldCreateUserWhenUserDoesNotExist() {
    when(userService.getUser(createUserRequest.getId()))
        .thenReturn(Optional.empty())
        .thenReturn(Optional.of(user));

    doNothing().when(userService).createUser(createUserRequest);

    when(turnRepository.findUserCurrrentTurn(any(), any(), eq(user.getId())))
        .thenReturn(Optional.empty());
    when(turnRepository.save(any(Turn.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Turn newTurn = turnServiceImpl.createTurn(createTurnRequest);

    assertNotNull(newTurn);
    assertEquals(user, newTurn.getUser());
    assertEquals(SpecialityEnum.MEDICINA_GENERAL, newTurn.getSpeciality());

    verify(userService, times(2)).getUser(user.getId());
    verify(userService, times(1)).createUser(createUserRequest);
    verify(turnRepository, times(1)).save(any(Turn.class));
  }

  @Test
  void shouldThrowExceptionWhenUserCreationFails() {
    when(userService.getUser(createUserRequest.getId())).thenReturn(Optional.empty());
    doNothing().when(userService).createUser(createUserRequest);
    when(userService.getUser(createUserRequest.getId())).thenReturn(Optional.empty());

    MedicalTurnsException e =
        assertThrows(
            MedicalTurnsException.class, () -> turnServiceImpl.createTurn(createTurnRequest));

    assertEquals(MedicalTurnsException.ERROR_CREATING_USER, e.getMessage());

    verify(userService, times(2)).getUser(createUserRequest.getId());
    verify(userService, times(1)).createUser(createUserRequest);
  }

  @Test
  void shouldReturnAllTodayTurns() {
    when(turnRepository.findTurnsForToday(any(), any(), eq(null))).thenReturn(List.of(turn));

    List<Turn> result = turnServiceImpl.getTurns();

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(turn, result.get(0));
  }

  @Test
  void shouldReturnTodayTurnsBySpeciality() {
    when(turnRepository.findTurnsForToday(any(), any(), eq(SpecialityEnum.MEDICINA_GENERAL)))
        .thenReturn(List.of(turn));

    List<Turn> result = turnServiceImpl.getTurns(SpecialityEnum.MEDICINA_GENERAL);

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(turn, result.get(0));
  }

  @Test
  void shouldGetTurnById() {
    when(turnRepository.findById(eq(turn.getId()))).thenReturn(Optional.of(turn));

    Turn turnFound = turnServiceImpl.getTurn(turn.getId());

    assertNotNull(turnFound);
    assertEquals(turn, turnFound);
  }

  @Test
  void shouldThrowIfTurnNotFound() {
    when(turnRepository.findById(2L)).thenReturn(Optional.empty());

    assertThrows(MedicalTurnsException.class, () -> turnServiceImpl.getTurn(2L));
  }

  @Test
  void shouldReturnCurrentTurnBySpeciality() {
    turn.setStatus(StatusEnum.CURRENT);

    when(turnRepository.findCurrentTurn(any(), any(), eq(SpecialityEnum.MEDICINA_GENERAL)))
        .thenReturn(Optional.of(turn));

    Optional<Turn> result = turnServiceImpl.getCurrentTurn(SpecialityEnum.MEDICINA_GENERAL);

    assertTrue(result.isPresent());
    assertEquals(turn, result.get());
  }

  @Test
  void shouldReturnLastCurrentTurn() {
    turn.setStatus(StatusEnum.CURRENT);

    when(turnRepository.findCurrentTurns(any(), any())).thenReturn(List.of(turn));

    Optional<Turn> result = turnServiceImpl.getLastCurrentTurn();

    assertTrue(result.isPresent());
    assertEquals(turn, result.get());
  }

  @Test
  void shouldReturnLastTurnBySpeciality() {
    when(turnRepository.findTurnsForToday(any(), any(), eq(SpecialityEnum.MEDICINA_GENERAL)))
        .thenReturn(List.of(turn));

    Optional<Turn> result = turnServiceImpl.getLastTurn(SpecialityEnum.MEDICINA_GENERAL);

    assertTrue(result.isPresent());
    assertEquals(turn, result.get());
  }

  @Test
  void shouldFinishCurrentTurn() {
    turn.setStatus(StatusEnum.CURRENT);
    Doctor doctor = new Doctor();

    when(turnRepository.findCurrentTurn(any(), any(), eq(SpecialityEnum.MEDICINA_GENERAL)))
        .thenReturn(Optional.of(turn));

    turnServiceImpl.finishTurn(SpecialityEnum.MEDICINA_GENERAL, 4, doctor);

    assertEquals(StatusEnum.COMPLETED, turn.getStatus());
    assertEquals(4, turn.getLevelAttention());
    assertEquals(doctor, turn.getDoctor());
  }

  @Test
  void shouldSkipCurrentTurn() {
    turn.setStatus(StatusEnum.CURRENT);
    Doctor doctor = new Doctor();

    when(turnRepository.findCurrentTurn(any(), any(), eq(SpecialityEnum.MEDICINA_GENERAL)))
        .thenReturn(Optional.of(turn));
    when(turnRepository.save(turn)).thenReturn(turn);

    turnServiceImpl.skipTurn(SpecialityEnum.MEDICINA_GENERAL, doctor);

    assertEquals(StatusEnum.FINISHED, turn.getStatus());
    assertEquals(doctor, turn.getDoctor());
  }

  @Test
  void shouldStartNextTurn() {
    turn.setStatus(StatusEnum.PENDING);

    when(turnRepository.findTurnsForToday(any(), any(), eq(SpecialityEnum.MEDICINA_GENERAL)))
        .thenReturn(List.of(turn));
    when(turnRepository.save(turn)).thenReturn(turn);
    when(turnRepository.findById(turn.getId())).thenReturn(Optional.of(turn));

    Turn started = turnServiceImpl.startNextTurn(SpecialityEnum.MEDICINA_GENERAL);

    assertEquals(StatusEnum.CURRENT, started.getStatus());
  }

  @Test
  void shouldThrowMedicalTurnsExceptionWhenNoLastTurn() {
    when(turnRepository.findTurnsForToday(any(), any(), eq(SpecialityEnum.MEDICINA_GENERAL)))
        .thenReturn(Collections.emptyList());

    assertThrows(
        MedicalTurnsException.class,
        () -> turnServiceImpl.startNextTurn(SpecialityEnum.MEDICINA_GENERAL));
  }

  @Test
  void shouldStartTurn() {
    turn.setStatus(StatusEnum.PENDING);

    when(turnRepository.save(turn)).thenReturn(turn);
    when(turnRepository.findById(1L)).thenReturn(Optional.of(turn));

    Turn started = turnServiceImpl.startTurn(turn);

    assertNotNull(started);
    assertEquals(StatusEnum.CURRENT, started.getStatus());
  }

  @Test
  void shouldThrowIfTurnIsNotPending() {
    turn.setStatus(StatusEnum.CURRENT);

    assertThrows(MedicalTurnsException.class, () -> turnServiceImpl.startTurn(turn));
  }

  @Test
  void shouldReturnEmptyListWhenNoAverageLevelByRoleData() {
    when(turnRepository.getAverageLevelAttentionByRole(any(), any())).thenReturn(List.of());

    List<AverageLevelByRole> result =
        turnServiceImpl.getAverageLevelAttentionByRole(null, LocalDate.now(), LocalDate.now());

    assertTrue(result.isEmpty());
  }

  @Test
  void shouldFilterAverageLevelByRole() {
    AverageLevelByRole avg1 =
        new AverageLevelByRole() {
          @Override
          public RoleEnum getRole() {
            return RoleEnum.DOCTOR;
          }

          @Override
          public Double getAverageLevel() {
            return 4.0;
          }
        };

    AverageLevelByRole avg2 =
        new AverageLevelByRole() {
          @Override
          public RoleEnum getRole() {
            return RoleEnum.ESTUDIANTE;
          }

          @Override
          public Double getAverageLevel() {
            return 3.0;
          }
        };

    when(turnRepository.getAverageLevelAttentionByRole(any(), any()))
        .thenReturn(List.of(avg1, avg2));

    // Role != null
    List<AverageLevelByRole> result =
        turnServiceImpl.getAverageLevelAttentionByRole(
            RoleEnum.DOCTOR, LocalDate.now(), LocalDate.now());

    assertEquals(1, result.size());
    assertTrue(result.contains(avg1));

    // Role == null
    List<AverageLevelByRole> resultWithNullRole =
        turnServiceImpl.getAverageLevelAttentionByRole(null, LocalDate.now(), LocalDate.now());

    assertEquals(2, resultWithNullRole.size());
    assertTrue(resultWithNullRole.contains(avg1));
    assertTrue(resultWithNullRole.contains(avg2));
  }

  @Test
  void shouldReturnEmptyListWhenNoTurnCountByRoleData() {
    when(turnRepository.getTurnCountByRole(any(), any())).thenReturn(List.of());

    List<CountByRole> result =
        turnServiceImpl.getTurnCountByRole(null, LocalDate.now(), LocalDate.now(), null);

    assertTrue(result.isEmpty());
  }

  @Test
  void shouldFilterTurnCountByRole() {
    CountByRole count1 =
        new CountByRole() {
          @Override
          public RoleEnum getRole() {
            return RoleEnum.DOCTOR;
          }

          @Override
          public StatusEnum getStatus() {
            return StatusEnum.PENDING;
          }

          @Override
          public Long getCount() {
            return 10L;
          }
        };

    CountByRole count2 =
        new CountByRole() {
          @Override
          public RoleEnum getRole() {
            return RoleEnum.ESTUDIANTE;
          }

          @Override
          public StatusEnum getStatus() {
            return StatusEnum.COMPLETED;
          }

          @Override
          public Long getCount() {
            return 5L;
          }
        };

    when(turnRepository.getTurnCountByRole(any(), any())).thenReturn(List.of(count1, count2));

    // Role != null and Status != null
    List<CountByRole> result =
        turnServiceImpl.getTurnCountByRole(
            RoleEnum.DOCTOR, LocalDate.now(), LocalDate.now(), StatusEnum.PENDING);

    assertEquals(1, result.size());
    assertTrue(result.contains(count1));

    // Role == null and Status != null
    List<CountByRole> resultWithNullRole =
        turnServiceImpl.getTurnCountByRole(
            null, LocalDate.now(), LocalDate.now(), StatusEnum.PENDING);

    assertEquals(1, resultWithNullRole.size());
    assertTrue(resultWithNullRole.contains(count1));

    // Role == null and Status != null
    List<CountByRole> resultWithNullStatus =
        turnServiceImpl.getTurnCountByRole(
            RoleEnum.ESTUDIANTE, LocalDate.now(), LocalDate.now(), null);

    assertEquals(1, resultWithNullStatus.size());
    assertTrue(resultWithNullStatus.contains(count2));

    // Role == null and Status == null
    List<CountByRole> resultWithNullRoleAndStatus =
        turnServiceImpl.getTurnCountByRole(null, LocalDate.now(), LocalDate.now(), null);

    assertEquals(2, resultWithNullRoleAndStatus.size());
    assertTrue(resultWithNullRoleAndStatus.contains(count1));
    assertTrue(resultWithNullRoleAndStatus.contains(count2));
  }

  @Test
  void shouldReturnEmptyListWhenNoAverageLevelBySpecialityData() {
    when(turnRepository.getAverageLevelAttentionBySpeciality(any(), any())).thenReturn(List.of());

    List<AverageLevelBySpeciality> result =
        turnServiceImpl.getAverageLevelAttentionBySpeciality(
            null, LocalDate.now(), LocalDate.now());

    assertTrue(result.isEmpty());
  }

  @Test
  void shouldFilterAverageLevelBySpeciality() {
    AverageLevelBySpeciality avg1 =
        new AverageLevelBySpeciality() {
          @Override
          public SpecialityEnum getSpeciality() {
            return SpecialityEnum.MEDICINA_GENERAL;
          }

          @Override
          public Double getAverageLevel() {
            return 4.5;
          }
        };

    AverageLevelBySpeciality avg2 =
        new AverageLevelBySpeciality() {
          @Override
          public SpecialityEnum getSpeciality() {
            return SpecialityEnum.ODONTOLOGIA;
          }

          @Override
          public Double getAverageLevel() {
            return 3.8;
          }
        };

    when(turnRepository.getAverageLevelAttentionBySpeciality(any(), any()))
        .thenReturn(List.of(avg1, avg2));

    // Speciality != null
    List<AverageLevelBySpeciality> result =
        turnServiceImpl.getAverageLevelAttentionBySpeciality(
            SpecialityEnum.MEDICINA_GENERAL, LocalDate.now(), LocalDate.now());

    assertEquals(1, result.size());
    assertTrue(result.contains(avg1));

    // Speciality == null
    List<AverageLevelBySpeciality> resultWithNullSpeciality =
        turnServiceImpl.getAverageLevelAttentionBySpeciality(
            null, LocalDate.now(), LocalDate.now());

    assertEquals(2, resultWithNullSpeciality.size());
    assertTrue(resultWithNullSpeciality.contains(avg1));
    assertTrue(resultWithNullSpeciality.contains(avg2));
  }

  @Test
  void shouldReturnEmptyListWhenNoTurnCountBySpecialityData() {
    when(turnRepository.getTurnCountBySpeciality(any(), any())).thenReturn(List.of());

    List<CountBySpeciality> result =
        turnServiceImpl.getTurnCountBySpeciality(null, LocalDate.now(), LocalDate.now(), null);

    assertTrue(result.isEmpty());
  }

  @Test
  void shouldFilterTurnCountBySpeciality() {
    CountBySpeciality count1 =
        new CountBySpeciality() {
          @Override
          public SpecialityEnum getSpeciality() {
            return SpecialityEnum.MEDICINA_GENERAL;
          }

          @Override
          public StatusEnum getStatus() {
            return StatusEnum.PENDING;
          }

          @Override
          public Long getCount() {
            return 15L;
          }
        };

    CountBySpeciality count2 =
        new CountBySpeciality() {
          @Override
          public SpecialityEnum getSpeciality() {
            return SpecialityEnum.ODONTOLOGIA;
          }

          @Override
          public StatusEnum getStatus() {
            return StatusEnum.COMPLETED;
          }

          @Override
          public Long getCount() {
            return 8L;
          }
        };

    when(turnRepository.getTurnCountBySpeciality(any(), any())).thenReturn(List.of(count1, count2));

    // Speciality != null and Status != null
    List<CountBySpeciality> result =
        turnServiceImpl.getTurnCountBySpeciality(
            SpecialityEnum.MEDICINA_GENERAL, LocalDate.now(), LocalDate.now(), StatusEnum.PENDING);

    assertEquals(1, result.size());
    assertTrue(result.contains(count1));

    // Speciality == null and Status != null
    List<CountBySpeciality> resultWithNullSpeciality =
        turnServiceImpl.getTurnCountBySpeciality(
            null, LocalDate.now(), LocalDate.now(), StatusEnum.PENDING);

    assertEquals(1, resultWithNullSpeciality.size());
    assertTrue(resultWithNullSpeciality.contains(count1));

    // Speciality == null and Status != null
    List<CountBySpeciality> resultWithNullStatus =
        turnServiceImpl.getTurnCountBySpeciality(
            SpecialityEnum.ODONTOLOGIA, LocalDate.now(), LocalDate.now(), null);

    assertEquals(1, resultWithNullStatus.size());
    assertTrue(resultWithNullStatus.contains(count2));

    // Speciality == null and Status == null
    List<CountBySpeciality> resultWithNullSpecialityAndStatus =
        turnServiceImpl.getTurnCountBySpeciality(null, LocalDate.now(), LocalDate.now(), null);

    assertEquals(2, resultWithNullSpecialityAndStatus.size());
    assertTrue(resultWithNullSpecialityAndStatus.contains(count1));
    assertTrue(resultWithNullSpecialityAndStatus.contains(count2));
  }

  private CreateUserRequest getCreateUserRequest() {
    createUserRequest = new CreateUserRequest();
    createUserRequest.setId("1032373105");
    createUserRequest.setName("Daniel");
    createUserRequest.setRole(RoleEnum.ESTUDIANTE);

    return createUserRequest;
  }

  private CreateTurnRequest getCreateTurnRequest() {
    createTurnRequest = new CreateTurnRequest();
    createTurnRequest.setUser(createUserRequest);
    createTurnRequest.setSpeciality(SpecialityEnum.MEDICINA_GENERAL);

    return createTurnRequest;
  }

  private User getUser() {
    return new User("1032373105", "Daniel", RoleEnum.ESTUDIANTE);
  }

  private Turn getTurn() {
    dateTime = LocalDateTime.now();
    Turn newTurn = new Turn(user, "M-0", SpecialityEnum.MEDICINA_GENERAL, dateTime);
    newTurn.setId(1L);

    return newTurn;
  }
}
