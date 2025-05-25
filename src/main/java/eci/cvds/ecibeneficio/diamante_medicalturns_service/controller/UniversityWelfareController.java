package eci.cvds.ecibeneficio.diamante_medicalturns_service.controller;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request.CallTurnRequest;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request.CreateTurnRequest;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.response.ApiResponse;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.response.TurnResponse;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.Turn;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.service.UniversityWelfareService;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.SpecialityEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/turns")
@AllArgsConstructor
@Tag(
    name = "Bienestar Universitario",
    description = "Operaciones relacionadas con la gestión de bienestar universitario")
public class UniversityWelfareController {
  private final UniversityWelfareService universityWelfareService;

  @Operation(summary = "Crear turno", description = "Crea un nuevo turno para atención médica.")
  @ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "Turno creado exitosamente",
        content = @Content(mediaType = "application/json")),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "400",
        description = "El usuario ya tiene un turno o los turnos están deshabilitados",
        content = @Content(mediaType = "application/json")),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "500",
        description = "Error en el servidor",
        content = @Content(mediaType = "application/json"))
  })
  @PostMapping()
  public ResponseEntity<ApiResponse<TurnResponse>> addTurn(@RequestBody CreateTurnRequest turn) {
    return ResponseEntity.ok(
        ApiResponse.success("Turn created", universityWelfareService.addTurn(turn)));
  }

  @Operation(
      summary = "Obtener todos los turnos",
      description = "Devuelve todos los turnos existentes.")
  @ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "Turnos obtenidos exitosamente",
        content = @Content(mediaType = "application/json")),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "500",
        description = "Error en el servidor",
        content = @Content(mediaType = "application/json"))
  })
  @PreAuthorize("hasRole('MEDICAL_SECRETARY')")
  @GetMapping()
  public ResponseEntity<ApiResponse<List<TurnResponse>>> getTurns() {
    return ResponseEntity.ok(
        ApiResponse.success("Turns obtained", universityWelfareService.getTurns()));
  }

  @Operation(summary = "Obtener turnos por especialidad")
  @ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "Turnos obtenidos exitosamente",
        content = @Content(mediaType = "application/json")),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "500",
        description = "Error en el servidor",
        content = @Content(mediaType = "application/json"))
  })
  @PreAuthorize("hasRole('MEDICAL_STAFF')")
  @GetMapping("/{speciality}")
  public ResponseEntity<ApiResponse<List<TurnResponse>>> getTurns(
      @PathVariable SpecialityEnum speciality) {
    return ResponseEntity.ok(
        ApiResponse.success("Turns obtained", universityWelfareService.getTurns(speciality)));
  }

  @Operation(summary = "Obtener el ultimo turno llamado")
  @ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "Ultimo turno llamado obtenido exitosamente",
        content = @Content(mediaType = "application/json")),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "500",
        description = "Error en el servidor",
        content = @Content(mediaType = "application/json"))
  })
  @PreAuthorize("hasRole('MEDICAL_SECRETARY')")
  @GetMapping("/current-turn")
  public ResponseEntity<ApiResponse<TurnResponse>> getLastTurn() {
    Optional<TurnResponse> turn = universityWelfareService.getLastCurrentTurn();

    return turn.map(
            turnResponse ->
                ResponseEntity.ok(ApiResponse.success("Latest turn obtained", turnResponse)))
        .orElseGet(
            () ->
                ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(ApiResponse.error("No turn found")));
  }

  @Operation(summary = "Obtener el ultimo turno llamado dad una especialidad")
  @ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "Ultimo turno llamado por especialidad obtenido exitosamente",
        content = @Content(mediaType = "application/json")),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "500",
        description = "Error en el servidor",
        content = @Content(mediaType = "application/json"))
  })
  @PreAuthorize("hasRole('MEDICAL_STAFF')")
  @GetMapping("/current-turn/{speciality}")
  public ResponseEntity<ApiResponse<TurnResponse>> getLastTurn(
      @PathVariable SpecialityEnum speciality) {
    Optional<TurnResponse> turn = universityWelfareService.getCurrentTurn(speciality);

    return turn.map(
            turnResponse ->
                ResponseEntity.ok(ApiResponse.success("Latest turn obtained", turnResponse)))
        .orElseGet(
            () ->
                ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(ApiResponse.error("No turn found")));
  }

  @Operation(summary = "Llamar siguiente turno automáticamente")
  @ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "Turno llamado exitosamente",
        content = @Content(mediaType = "application/json")),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "400",
        description = "El doctor no existe o no hay mas turnos",
        content = @Content(mediaType = "application/json")),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "500",
        description = "Error en el servidor",
        content = @Content(mediaType = "application/json"))
  })
  @PreAuthorize("hasRole('MEDICAL_STAFF')")
  @PostMapping("/call-next")
  public ResponseEntity<ApiResponse<TurnResponse>> callNextTurn(
      @RequestBody CallTurnRequest callNextTurn) {
    return ResponseEntity.ok(
        ApiResponse.success(
            "Successfully called turn",
            universityWelfareService.callNextTurn(
                callNextTurn.getSpeciality(), callNextTurn.getLevelAttention())));
  }

  @Operation(summary = "Llamar turno específico")
  @ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "Turno llamado exitosamente",
        content = @Content(mediaType = "application/json")),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "400",
        description = "El doctor no existe o no hay mas turnos",
        content = @Content(mediaType = "application/json")),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "500",
        description = "Error en el servidor",
        content = @Content(mediaType = "application/json"))
  })
  @PreAuthorize("hasRole('MEDICAL_STAFF')")
  @PostMapping("/call")
  public ResponseEntity<ApiResponse<TurnResponse>> callTurn(
      @RequestBody CallTurnRequest callNextTurn) {
    return ResponseEntity.ok(
        ApiResponse.success(
            "Successfully called turn",
            universityWelfareService.callNextTurn(
                callNextTurn.getTurnId(),
                callNextTurn.getSpeciality(),
                callNextTurn.getLevelAttention())));
  }

  @Operation(summary = "Finaliza un turno por inasistencia del usuario")
  @ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "Turno finalizado exitosamente",
        content = @Content(mediaType = "application/json")),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "400",
        description = "El doctor no existe",
        content = @Content(mediaType = "application/json")),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "500",
        description = "Error en el servidor",
        content = @Content(mediaType = "application/json"))
  })
  @PreAuthorize("hasRole('MEDICAL_STAFF')")
  @PostMapping("/skip")
  public ResponseEntity<ApiResponse<TurnResponse>> skipTurn(
      @RequestParam SpecialityEnum speciality) {
    universityWelfareService.skipTurn(speciality);

    return ResponseEntity.ok(ApiResponse.success("Successfully skipped turn"));
  }

  @Operation(summary = "Habilitar turnos")
  @ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "Turnos habilitados exitosamente",
        content = @Content(mediaType = "application/json")),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "500",
        description = "Error en el servidor",
        content = @Content(mediaType = "application/json"))
  })
  @PreAuthorize("hasAnyRole('MEDICAL_SECRETARY', 'ADMINISTRATOR')")
  @PostMapping("/enable")
  public ResponseEntity<ApiResponse<Void>> enableTurns() {
    universityWelfareService.enableTurns();
    return ResponseEntity.ok(ApiResponse.success("Turns enabled"));
  }

  @Operation(summary = "Deshabilitar turnos")
  @ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "Turnos deshabilitados exitosamente",
        content = @Content(mediaType = "application/json")),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "500",
        description = "Error en el servidor",
        content = @Content(mediaType = "application/json"))
  })
  @PreAuthorize("hasAnyRole('MEDICAL_SECRETARY', 'ADMINISTRATOR')")
  @PostMapping("/disable")
  public ResponseEntity<ApiResponse<Void>> disableTurns() {
    universityWelfareService.disableTurns();
    return ResponseEntity.ok(ApiResponse.success("Turns disabled"));
  }

  @Operation(summary = "Habilitar turnos por especialidad")
  @ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "Turnos habilitados para la especialidad",
        content = @Content(mediaType = "application/json")),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "400",
        description = "Los turnos ya estan deshabilitados",
        content = @Content(mediaType = "application/json")),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "500",
        description = "Error en el servidor",
        content = @Content(mediaType = "application/json"))
  })
  @PreAuthorize("hasAnyRole('MEDICAL_SECRETARY', 'ADMINISTRATOR')")
  @PostMapping("/enable/{speciality}")
  public ResponseEntity<ApiResponse<Void>> enableTurns(@PathVariable SpecialityEnum speciality) {
    universityWelfareService.enableTurns(speciality);
    return ResponseEntity.ok(ApiResponse.success("Turns enabled"));
  }

  @Operation(summary = "Deshabilitar turnos por especialidad")
  @ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "Turnos habilitados para la especialidad",
        content = @Content(mediaType = "application/json")),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "400",
        description = "Los turnos ya estan deshabilitados",
        content = @Content(mediaType = "application/json")),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "500",
        description = "Error en el servidor",
        content = @Content(mediaType = "application/json"))
  })
  @PreAuthorize("hasAnyRole('MEDICAL_SECRETARY', 'ADMINISTRATOR')")
  @PostMapping("/disable/{speciality}")
  public ResponseEntity<ApiResponse<Void>> disableTurns(@PathVariable SpecialityEnum speciality) {
    universityWelfareService.disableTurns(speciality);
    return ResponseEntity.ok(ApiResponse.success("Turns disabled"));
  }

  @Operation(summary = "Verifica el estado de habilitación de los turnos.")
  @ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Estado actual de habilitación actual de los turnos",
            content = @Content(mediaType = "application/json")),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "Error en el servidor",
            content = @Content(mediaType = "application/json"))
  })
  @PreAuthorize("isAuthenticated()")
  @GetMapping("/are-enabled")
  public ResponseEntity<ApiResponse<Boolean>> areTurnsEnabled() {
    return ResponseEntity.ok(ApiResponse.success("Turns enabled status",
            universityWelfareService.areTurnsEnabled()));
  }



  @Operation(summary = "Obtiene las especialidades cuyos turnos estan deshabilitados")
  @ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Especialidades cuyos turnos estan deshabilitados",
            content = @Content(mediaType = "application/json")),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "Error en el servidor",
            content = @Content(mediaType = "application/json"))
  })
  @PreAuthorize("isAuthenticated()")
  @GetMapping("/specialties-disabled")
  public ResponseEntity<ApiResponse<List<SpecialityEnum>>> getSpecialtiesDisabled () {
    return ResponseEntity.ok(ApiResponse.success("Disabled specialties",
            universityWelfareService.getSpecialtiesDisabled()));
  }



}
