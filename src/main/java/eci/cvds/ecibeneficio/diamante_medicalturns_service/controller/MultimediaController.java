package eci.cvds.ecibeneficio.diamante_medicalturns_service.controller;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request.CreateMultimediaRequest;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.response.ApiResponse;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.response.MultimediaResponse;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.service.MultimediaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/multimedia")
@RequiredArgsConstructor
@Tag(name = "Multimedia", description = "Operaciones sobre el contenido informativo ")
public class MultimediaController {
  private final MultimediaService multimediaService;

  @Operation(
      summary = "Subir multimedia",
      description = "Permite subir un archivo multimedia al sistema.")
  @ApiResponses(
      value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Archivo multimedia subido correctamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor al subir el archivo multimedia")
      })
  @PreAuthorize("hasAnyRole('MEDICAL_SECRETARY', 'ADMINISTRATOR')")
  @PostMapping
  public ResponseEntity<ApiResponse<Void>> uploadMultimedia(
      @ModelAttribute CreateMultimediaRequest request) {
    try {
      multimediaService.createMultimedia(request);
      return ResponseEntity.ok(ApiResponse.success("Multimedia uploaded"));
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body(ApiResponse.error(e.getMessage()));
    }
  }

  @Operation(
      summary = "Obtener multimedia por ID",
      description = "Obtiene un archivo multimedia específico mediante su ID.")
  @ApiResponses(
      value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Archivo multimedia encontrado"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Archivo multimedia no encontrado"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor al obtener el archivo multimedia")
      })
  @PreAuthorize("hasRole('MEDICAL_SECRETARY')")
  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<MultimediaResponse>> getById(@PathVariable Long id) {
    return ResponseEntity.ok(
        ApiResponse.success("Multimedia found", multimediaService.getMultimedia(id)));
  }

  @Operation(
      summary = "Obtener todos los archivos multimedia",
      description = "Obtiene todos los archivos multimedia almacenados en el sistema.")
  @ApiResponses(
      value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Lista de archivos multimedia"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "No se encontraron archivos multimedia"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor al obtener los archivos multimedia")
      })
  @PreAuthorize("hasRole('MEDICAL_SECRETARY')")
  @GetMapping
  public ResponseEntity<ApiResponse<List<MultimediaResponse>>> getAll() {
    return ResponseEntity.ok(
        ApiResponse.success("Multimedia found", multimediaService.getAllMultimedia()));
  }

  @Operation(
      summary = "Eliminar multimedia",
      description = "Permite eliminar un archivo multimedia del sistema mediante su ID.")
  @ApiResponses(
      value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "204",
            description = "Archivo multimedia eliminado correctamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Archivo multimedia no encontrado"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor al eliminar el archivo multimedia")
      })
  @PreAuthorize("hasAnyRole('MEDICAL_SECRETARY', 'ADMINISTRATOR')")
  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
    multimediaService.deleteMultimedia(id);
    return ResponseEntity.ok(ApiResponse.success("Multimedia deleted"));
  }
}
