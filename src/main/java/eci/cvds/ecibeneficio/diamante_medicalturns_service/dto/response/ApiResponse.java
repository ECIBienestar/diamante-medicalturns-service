package eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiResponse<T> {
  private boolean success;
  private String message;
  private T data;

  public static <T> ApiResponse<T> success(String message, T data) {
    return new ApiResponse<>(true, message, data);
  }

  public static <T> ApiResponse<T> success(String message) {
    return new ApiResponse<>(true, message, null);
  }

  public static <T> ApiResponse<T> error(String message) {
    return new ApiResponse<>(false, message, null);
  }
}
