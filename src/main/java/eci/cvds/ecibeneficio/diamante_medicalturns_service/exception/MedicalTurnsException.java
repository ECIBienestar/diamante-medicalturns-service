package eci.cvds.ecibeneficio.diamante_medicalturns_service.exception;

public class MedicalTurnsException extends RuntimeException {
  // ─── User ───
  public static final String USER_NOT_FOUND = "User not found";
  public static final String ERROR_CREATING_USER = "Failed to create user";

  // ─── SpecialitySequence ───
  public static final String SEQUENCE_NOT_FOUND = "Speciality sequence not found";

  // ─── Turn ───
  public static final String TURN_NOT_FOUND = "Turn not found";

  public MedicalTurnsException(String message) {
    super(message);
  }
}
