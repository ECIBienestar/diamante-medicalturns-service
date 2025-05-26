package eci.cvds.ecibeneficio.diamante_medicalturns_service.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.repository.projection.*;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.security.JwtAuthenticationFilter;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.security.JwtService;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.service.TurnService;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.RoleEnum;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.SpecialityEnum;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.StatusEnum;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = ReportController.class)
@Import({JwtAuthenticationFilter.class})
class ReportControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private TurnService turnService;

  @MockitoBean private JwtService jwtService;

  @Test
  @WithMockUser(roles = {"MEDICAL_SECRETARY", "ADMINISTRATOR", "MEDICAL_STAFF"})
  void testGetAverageLevelAttentionByRole() throws Exception {
    AverageLevelByRole avgRole = new AverageLevelByRoleImpl(RoleEnum.MEDICAL_STAFF, 4.7);
    when(turnService.getAverageLevelAttentionByRole(eq(RoleEnum.MEDICAL_STAFF), any(), any()))
        .thenReturn(List.of(avgRole));

    mockMvc
        .perform(
            get("/api/reports/avg-role")
                .param("role", "MEDICAL_STAFF")
                .param("start", "2025-01-01")
                .param("end", "2025-05-23")
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Average obtained"))
        .andExpect(jsonPath("$.data").isArray());
  }

  @Test
  @WithMockUser(roles = {"MEDICAL_SECRETARY", "ADMINISTRATOR", "MEDICAL_STAFF"})
  void testGetTurnCountByRole() throws Exception {
    CountByRole countRole = Mockito.mock(CountByRole.class);
    when(turnService.getTurnCountByRole(
            eq(RoleEnum.ADMINISTRATOR), any(), any(), eq(StatusEnum.COMPLETED)))
        .thenReturn(List.of(countRole));

    mockMvc
        .perform(
            get("/api/reports/count-role")
                .param("role", RoleEnum.STUDENT.name())
                .param("start", "2025-01-01")
                .param("end", "2025-05-23")
                .param("status", StatusEnum.COMPLETED.name())
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Turn count obtained"))
        .andExpect(jsonPath("$.data").isArray());
  }

  @Test
  @WithMockUser(roles = {"MEDICAL_SECRETARY", "ADMINISTRATOR", "MEDICAL_STAFF"})
  void testGetAverageLevelAttentionBySpeciality() throws Exception {
    AverageLevelBySpeciality avgSpeciality =
        new AverageLevelBySpecialityImpl(SpecialityEnum.DENTISTRY, 4.5);
    when(turnService.getAverageLevelAttentionBySpeciality(
            eq(SpecialityEnum.DENTISTRY), any(), any()))
        .thenReturn(List.of(avgSpeciality));

    mockMvc
        .perform(
            get("/api/reports/avg-speciality")
                .param("speciality", SpecialityEnum.DENTISTRY.name())
                .param("start", "2025-01-01")
                .param("end", "2025-05-23")
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Average obtained"))
        .andExpect(jsonPath("$.data").isArray());
  }

  @Test
  @WithMockUser(roles = {"MEDICAL_SECRETARY", "ADMINISTRATOR", "MEDICAL_STAFF"})
  void testGetTurnCountBySpeciality() throws Exception {
    CountBySpeciality countSpeciality = new CountBySpecialityImpl(SpecialityEnum.PSYCHOLOGY, 42L);

    when(turnService.getTurnCountBySpeciality(
            eq(SpecialityEnum.PSYCHOLOGY), any(), any(), eq(StatusEnum.PENDING)))
        .thenReturn(List.of(countSpeciality));

    mockMvc
        .perform(
            get("/api/reports/count-speciality")
                .param("speciality", SpecialityEnum.PSYCHOLOGY.name())
                .param("start", "2025-01-01")
                .param("end", "2025-05-23")
                .param("status", StatusEnum.PENDING.name())
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Turn count obtained"))
        .andExpect(jsonPath("$.data").isArray());
  }

  public class AverageLevelBySpecialityImpl implements AverageLevelBySpeciality {
    private SpecialityEnum speciality;
    private Double averageLevel;

    public AverageLevelBySpecialityImpl(SpecialityEnum speciality, Double averageLevel) {
      this.speciality = speciality;
      this.averageLevel = averageLevel;
    }

    @Override
    public SpecialityEnum getSpeciality() {
      return speciality;
    }

    @Override
    public Double getAverageLevel() {
      return averageLevel;
    }
  }

  public class AverageLevelByRoleImpl implements AverageLevelByRole {
    private RoleEnum role;
    private Double averageLevel;

    public AverageLevelByRoleImpl(RoleEnum role, Double averageLevel) {
      this.role = role;
      this.averageLevel = averageLevel;
    }

    @Override
    public RoleEnum getRole() {
      return role;
    }

    @Override
    public Double getAverageLevel() {
      return averageLevel;
    }
  }

  public class CountBySpecialityImpl implements CountBySpeciality {
    private SpecialityEnum speciality;
    private Long count;

    public CountBySpecialityImpl(SpecialityEnum speciality, Long count) {
      this.speciality = speciality;
      this.count = count;
    }

    @Override
    public SpecialityEnum getSpeciality() {
      return speciality;
    }

    @Override
    public StatusEnum getStatus() {
      return null;
    }

    @Override
    public Long getCount() {
      return count;
    }
  }
}
