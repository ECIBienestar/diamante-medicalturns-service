package eci.cvds.ecibeneficio.diamante_medicalturns_service.controller;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request.CallTurnRequest;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request.CreateTurnRequest;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.response.ApiResponse;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.response.TurnResponse;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.service.UniversityWelfareService;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.SpecialityEnum;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/turns")
@AllArgsConstructor
public class UniversityWelfareController {
  private final UniversityWelfareService universityWelfareService;

  @PostMapping()
  public ResponseEntity<ApiResponse<TurnResponse>> addTurn(@RequestBody CreateTurnRequest turn) {
    return ResponseEntity.ok(
        ApiResponse.success("Turn created", universityWelfareService.addTurn(turn)));
  }

  @GetMapping()
  public ResponseEntity<ApiResponse<List<TurnResponse>>> getTurns() {
    return ResponseEntity.ok(
        ApiResponse.success("Turns obtained", universityWelfareService.getTurns()));
  }

  @GetMapping("/{speciality}")
  public ResponseEntity<ApiResponse<List<TurnResponse>>> getTurns(
      @PathVariable SpecialityEnum speciality) {
    return ResponseEntity.ok(
        ApiResponse.success("Turns obtained", universityWelfareService.getTurns(speciality)));
  }

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

  @PostMapping("/call-next")
  public ResponseEntity<ApiResponse<TurnResponse>> callNextTurn(
      @RequestBody CallTurnRequest callNextTurn) {
    return ResponseEntity.ok(
        ApiResponse.success(
            "Successfully called turn",
            universityWelfareService.callNextTurn(
                callNextTurn.getDoctorId(),
                callNextTurn.getSpeciality(),
                callNextTurn.getLevelAttention())));
  }

  @PostMapping("/call")
  public ResponseEntity<ApiResponse<TurnResponse>> callTurn(
      @RequestBody CallTurnRequest callNextTurn) {
    return ResponseEntity.ok(
        ApiResponse.success(
            "Successfully called turn",
            universityWelfareService.callNextTurn(
                callNextTurn.getDoctorId(),
                callNextTurn.getTurnId(),
                callNextTurn.getSpeciality(),
                callNextTurn.getLevelAttention())));
  }

  @PostMapping("/enable")
  public ResponseEntity<ApiResponse<Void>> enableTurns() {
    universityWelfareService.enableTurns();
    return ResponseEntity.ok(ApiResponse.success("Turns enabled"));
  }

  @PostMapping("/disable")
  public ResponseEntity<ApiResponse<Void>> disableTurns() {
    universityWelfareService.disableTurns();
    return ResponseEntity.ok(ApiResponse.success("Turns disabled"));
  }

  @PostMapping("/enable/{speciality}")
  public ResponseEntity<ApiResponse<Void>> enableTurns(@PathVariable SpecialityEnum speciality) {
    universityWelfareService.enableTurns(speciality);
    return ResponseEntity.ok(ApiResponse.success("Turns enabled"));
  }

  @PostMapping("/disable/{speciality}")
  public ResponseEntity<ApiResponse<Void>> disableTurns(@PathVariable SpecialityEnum speciality) {
    universityWelfareService.disableTurns(speciality);
    return ResponseEntity.ok(ApiResponse.success("Turns disabled"));
  }
}
