package eci.cvds.ecibeneficio.diamante_medicalturns_service.exception;

public class MedicalTurnsException extends RuntimeException {
  // ─── User ───
  public static final String USER_NOT_FOUND = "User not found";
  public static final String ERROR_CREATING_USER = "Failed to create user";
  public static final String USER_HAVE_TURN = "User have already a turn";

  // ─── SpecialitySequence ───
  public static final String SEQUENCE_NOT_FOUND = "Speciality sequence not found";

  // ─── Turn ───
  public static final String TURNS_DISABLED = "Turns are disabled";
  public static final String TURN_NOT_FOUND = "Turn not found";
  public static final String TURNS_ALREADY_DISABLED = "All turns are already disabled";
  public static final String LAST_TURN = "There are not more turns";
  public static final String TURN_COMPLETED = "Turn is already completed";

  public MedicalTurnsException(String message) {
    super(message);
  }
}
