package eci.cvds.ecibeneficio.diamante_medicalturns_service.controller;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.response.ApiResponse;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.repository.projection.AverageLevelByRole;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.repository.projection.AverageLevelBySpeciality;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.repository.projection.CountByRole;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.repository.projection.CountBySpeciality;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.service.TurnService;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.RoleEnum;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.SpecialityEnum;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.StatusEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@PreAuthorize("hasAnyRole('SECRETARIA_MEDICA', 'DOCTOR')")
@RequiredArgsConstructor
@Tag(name = "Reportes", description = "Reportes de turnos y atención")
public class ReportController {
  private final TurnService turnService;

  @Operation(summary = "Obtener promedio del nivel de atención por rol")
  @ApiResponses(
      value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Promedio obtenido correctamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor")
      })
  @GetMapping("/avg-role")
  public ResponseEntity<ApiResponse<List<AverageLevelByRole>>> getAverageLevelAttentionByRole(
      @RequestParam(required = false) RoleEnum role,
      @RequestParam LocalDate start,
      @RequestParam LocalDate end) {
    return ResponseEntity.ok(
        ApiResponse.success(
            "Average obtained", turnService.getAverageLevelAttentionByRole(role, start, end)));
  }

  @Operation(summary = "Obtener cantidad de turnos por rol")
  @ApiResponses(
      value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Cantidad de turnos obtenida correctamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor")
      })
  @GetMapping("/count-role")
  public ResponseEntity<ApiResponse<List<CountByRole>>> getTurnCountByRole(
      @RequestParam(required = false) RoleEnum role,
      @RequestParam LocalDate start,
      @RequestParam LocalDate end,
      @RequestParam(required = false) StatusEnum status) {
    return ResponseEntity.ok(
        ApiResponse.success(
            "Turn count obtained", turnService.getTurnCountByRole(role, start, end, status)));
  }

  @Operation(summary = "Obtener promedio del nivel de atención por especialidad")
  @ApiResponses(
      value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Promedio obtenido correctamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor")
      })
  @GetMapping("/avg-speciality")
  public ResponseEntity<ApiResponse<List<AverageLevelBySpeciality>>>
      getAverageLevelAttentionBySpeciality(
          @RequestParam(required = false) SpecialityEnum speciality,
          @RequestParam LocalDate start,
          @RequestParam LocalDate end) {
    return ResponseEntity.ok(
        ApiResponse.success(
            "Average obtained",
            turnService.getAverageLevelAttentionBySpeciality(speciality, start, end)));
  }

  @Operation(summary = "Obtener cantidad de turnos por especialidad")
  @ApiResponses(
      value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Cantidad de turnos obtenida correctamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor")
      })
  @GetMapping("/count-speciality")
  public ResponseEntity<ApiResponse<List<CountBySpeciality>>> getTurnCountBySpeciality(
      @RequestParam(required = false) SpecialityEnum speciality,
      @RequestParam LocalDate start,
      @RequestParam LocalDate end,
      @RequestParam(required = false) StatusEnum status) {
    return ResponseEntity.ok(
        ApiResponse.success(
            "Turn count obtained",
            turnService.getTurnCountBySpeciality(speciality, start, end, status)));
  }
}
