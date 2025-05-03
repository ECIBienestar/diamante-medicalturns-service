package eci.cvds.ecibeneficio.diamante_medicalturns_service.exception;

public class MedicalTurnsException extends RuntimeException {
  // ─── User ───
  public static final String USER_NOT_FOUND = "User not found";

  public MedicalTurnsException(String message) {
    super(message);
  }
}
