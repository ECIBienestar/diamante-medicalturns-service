package eci.cvds.ecibeneficio.diamante_medicalturns_service.exception;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.response.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import java.nio.file.AccessDeniedException;
import java.time.format.DateTimeParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.messaging.handler.annotation.support.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalHandlerException {
  // Custom MedicalTurnException
  @ExceptionHandler(MedicalTurnsException.class)
  public ResponseEntity<ApiResponse<Void>> handleReservationException(MedicalTurnsException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(ex.getMessage()));
  }

  // Constraint violations (e.g. from @RequestParam or @PathVariable validation)
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(
      ConstraintViolationException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(ex.getMessage()));
  }

  // Spring Security - unauthorized access
  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ApiResponse<Void>> handleAccessDenied(AccessDeniedException ex) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error("Access denied"));
  }

  // Catch-all for unhandled exceptions
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex) {
    ex.printStackTrace();
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ApiResponse.error("Something went wrong"));
  }

  // Invalid parameter value (e.g., wrongly written enums)
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ApiResponse<Void>> handleTypeMismatch(
      MethodArgumentTypeMismatchException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(ex.getMessage()));
  }

  // Problems when reading the body or parsing dates/values
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ApiResponse<Void>> handleInvalidFormat(HttpMessageNotReadableException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(
            ApiResponse.error(
                "Invalid format in the request body or in a parameter (e.g., date or enum)."));
  }

  // Invalid date format
  @ExceptionHandler(DateTimeParseException.class)
  public ResponseEntity<ApiResponse<Void>> handleDateParse(DateTimeParseException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ApiResponse.error("Invalid date format. Use yyyy-MM-dd."));
  }
}
