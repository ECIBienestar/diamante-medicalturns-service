package eci.cvds.ecibeneficio.diamante_medicalturns_service.exception;

import static org.junit.jupiter.api.Assertions.*;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.response.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import java.nio.file.AccessDeniedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class GlobalExceptionHandlerTest {
  private GlobalExceptionHandler handlerException;

  @BeforeEach
  void setUp() {
    handlerException = new GlobalExceptionHandler();
  }

  @Test
  void shouldHandleMedicalTurnsException() {
    String message = MedicalTurnsException.USER_HAVE_TURN;
    MedicalTurnsException ex = new MedicalTurnsException(message);

    ResponseEntity<ApiResponse<Void>> response = handlerException.handleReservationException(ex);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertNotNull(response.getBody());
    assertFalse(response.getBody().isSuccess());
    assertEquals(message, response.getBody().getMessage());
    assertNull(response.getBody().getData());
  }

  @Test
  void shouldHandleConstraintViolationException() {
    String message = "Invalid input";
    ConstraintViolationException ex = new ConstraintViolationException(message, null);

    ResponseEntity<ApiResponse<Void>> response = handlerException.handleConstraintViolation(ex);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertNotNull(response.getBody());
    assertFalse(response.getBody().isSuccess());
    assertEquals(message, response.getBody().getMessage());
    assertNull(response.getBody().getData());
  }

  @Test
  void shouldHandleAccessDeniedException() {
    AccessDeniedException ex = new AccessDeniedException("Forbidden access");

    ResponseEntity<ApiResponse<Void>> response = handlerException.handleAccessDenied(ex);

    assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    assertNotNull(response.getBody());
    assertFalse(response.getBody().isSuccess());
    assertEquals("Access denied", response.getBody().getMessage());
    assertNull(response.getBody().getData());
  }

  @Test
  void shouldHandleGenericException() {
    Exception ex = new RuntimeException("Unexpected error");

    ResponseEntity<ApiResponse<Void>> response = handlerException.handleGenericException(ex);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertNotNull(response.getBody());
    assertFalse(response.getBody().isSuccess());
    assertEquals("Something went wrong", response.getBody().getMessage());
    assertNull(response.getBody().getData());
  }
}
