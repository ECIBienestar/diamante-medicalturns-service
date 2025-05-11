package eci.cvds.ecibeneficio.diamante_medicalturns_service.model;

import static org.junit.jupiter.api.Assertions.*;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.PriorityEnum;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.RoleEnum;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.SpecialityEnum;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.StatusEnum;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TurnTest {
  private User user;
  private Doctor doctor;
  private LocalDateTime date;
  private Turn turn;

  @BeforeEach
  public void setUp() {
    user = new User("1", "Daniel", RoleEnum.ESTUDIANTE);
    doctor = new Doctor("2", "Carlos", RoleEnum.DOCTOR, SpecialityEnum.MEDICINA_GENERAL);
    date = LocalDateTime.of(2025, 5, 10, 10, 0);
    turn = createTurn();
  }

  @Test
  void shouldReturnTrueForSameAttributes() {
    Turn turn2 = createTurn();

    assertEquals(turn, turn2);
    assertEquals(turn.hashCode(), turn2.hashCode());
  }

  @Test
  void shouldReturnFalseWhenDifferentAttributes() {
    Turn turn2 = createTurn();
    turn2.setDate(date.plusDays(1));

    assertNotEquals(turn, turn2);
    assertNotEquals(turn.hashCode(), turn2.hashCode());
  }

  @Test
  void shouldReturnFalseWhenComparedWithNull() {
    assertNotEquals(null, turn);
  }

  @Test
  void shouldReturnFalseWhenComparedWithDifferentClass() {
    Object otherObject = "not a Turn";
    assertNotEquals(otherObject, turn);
  }

  @Test
  void shouldReturnFalseWhenLevelAttentionIsDifferent() {
    Turn turn2 = createTurn();
    turn2.setLevelAttention(5);
    assertNotEquals(turn, turn2);
  }

  @Test
  void shouldReturnFalseWhenUserIsDifferent() {
    Turn turn2 = createTurn();
    turn2.setUser(new User("99", "Otro", RoleEnum.ESTUDIANTE));
    assertNotEquals(turn, turn2);
  }

  @Test
  void shouldReturnFalseWhenUserIsNull() {
    Turn turn2 = createTurn();
    turn2.setUser(null);
    assertNotEquals(turn, turn2);
  }

  @Test
  void shouldReturnFalseWhenDoctorIsDifferent() {
    Turn turn2 = createTurn();
    turn2.setDoctor(new Doctor("99", "Otro", RoleEnum.DOCTOR, SpecialityEnum.PSICOLOGIA));
    assertNotEquals(turn, turn2);
  }

  @Test
  void shouldReturnFalseWhenDoctorIsNull() {
    Turn turn2 = createTurn();
    turn2.setDoctor(null);
    assertNotEquals(turn, turn2);
  }

  @Test
  void shouldReturnFalseWhenCodeIsDifferent() {
    Turn turn2 = createTurn();
    turn2.setCode("Otro código");
    assertNotEquals(turn, turn2);
  }

  @Test
  void shouldReturnFalseWhenCodeIsNull() {
    Turn turn2 = createTurn();
    turn2.setCode(null);
    assertNotEquals(turn, turn2);
  }

  @Test
  void shouldReturnFalseWhenDateIsDifferent() {
    Turn turn2 = createTurn();
    turn2.setDate(date.plusHours(2));
    assertNotEquals(turn, turn2);
  }

  @Test
  void shouldReturnFalseWhenDateIsNull() {
    Turn turn2 = createTurn();
    turn2.setDate(null);
    assertNotEquals(turn, turn2);
  }

  private Turn createTurn() {
    Turn newTurn = new Turn(user, "M-0", SpecialityEnum.MEDICINA_GENERAL, date);
    newTurn.setDoctor(doctor);
    newTurn.setPriority(PriorityEnum.DISCAPACIDAD);
    newTurn.setStatus(StatusEnum.PENDING);
    newTurn.setLevelAttention(1);

    return newTurn;
  }
}
