package eci.cvds.ecibeneficio.diamante_medicalturns_service.model;

import static org.junit.jupiter.api.Assertions.*;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.PriorityEnum;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.RoleEnum;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.SpecialityEnum;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.StatusEnum;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TurnTest {
  private User user;
  private LocalDateTime date;
  private Turn turn;

  @BeforeEach
  void setUp() {
    user = new User("1", "Daniel", RoleEnum.STUDENT);
    date = LocalDateTime.of(2025, 5, 10, 10, 0);
    turn = createTurn();
  }

  @Test
  void shouldReturnTrueWhenPriorityIsNotNull() {
    turn.setPriority(PriorityEnum.DISCAPACIDAD);
    assertTrue(turn.hasPriority());
  }

  @Test
  void shouldReturnFalseWhenPriorityIsNull() {
    turn.setPriority(null);
    assertFalse(turn.hasPriority());
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
    turn2.setUser(new User("99", "Otro", RoleEnum.STUDENT));
    assertNotEquals(turn, turn2);
  }

  @Test
  void shouldReturnFalseWhenUserIsNull() {
    Turn turn2 = createTurn();
    turn2.setUser(null);
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
    Turn newTurn = new Turn(user, "M-0", SpecialityEnum.GENERAL_MEDICINE, date);
    newTurn.setPriority(PriorityEnum.DISCAPACIDAD);
    newTurn.setStatus(StatusEnum.PENDING);
    newTurn.setLevelAttention(1);

    return newTurn;
  }
}
