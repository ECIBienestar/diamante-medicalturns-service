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
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {
  private final TurnService turnService;

  @GetMapping("/avg-role")
  public ResponseEntity<ApiResponse<List<AverageLevelByRole>>> getAverageLevelAttentionByRole(
      @RequestParam(required = false) RoleEnum role,
      @RequestParam LocalDate start,
      @RequestParam LocalDate end) {
    return ResponseEntity.ok(
        ApiResponse.success(
            "Average obtained", turnService.getAverageLevelAttentionByRole(role, start, end)));
  }

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
