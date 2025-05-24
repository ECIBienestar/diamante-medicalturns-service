package eci.cvds.ecibeneficio.diamante_medicalturns_service.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request.CallTurnRequest;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request.CreateTurnRequest;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.response.TurnResponse;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.response.UserResponse;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.security.JwtAuthenticationFilter;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.security.JwtService;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.service.UniversityWelfareService;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.RoleEnum;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.SpecialityEnum;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


@WebMvcTest(UniversityWelfareController.class)
@Import({JwtService.class, JwtAuthenticationFilter.class})
class UniversityWelfareControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private UniversityWelfareService service;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  @BeforeEach
  void setup() throws Exception {
    doAnswer(invocation -> {
      HttpServletRequest request = invocation.getArgument(0);
      HttpServletResponse response = invocation.getArgument(1);
      FilterChain filterChain = invocation.getArgument(2);

      List<GrantedAuthority> authorities = List.of(
              new SimpleGrantedAuthority("ROLE_MEDICAL_SECRETARY"),
              new SimpleGrantedAuthority("ROLE_ADMINISTRATOR"),
              new SimpleGrantedAuthority("ROLE_MEDICAL_STAFF")
      );

      Authentication auth = new UsernamePasswordAuthenticationToken("mockUser", null, authorities);
      SecurityContextHolder.getContext().setAuthentication(auth);

      filterChain.doFilter(request, response);

      SecurityContextHolder.clearContext();

      return null;
    }).when(jwtAuthenticationFilter).doFilter(any(HttpServletRequest.class), any(HttpServletResponse.class), any(FilterChain.class));
  }

  @Test
  @WithMockUser(roles = {"MEDICAL_SECRETARY","ADMINISTRATOR","MEDICAL_STAFF"})
  void testAddTurn() throws Exception {
    CreateTurnRequest request = new CreateTurnRequest();
    UserResponse userResponse = new UserResponse("1", "Juan", RoleEnum.STUDENT);
    TurnResponse mockResponse =
            new TurnResponse(1L, "A01", userResponse, SpecialityEnum.GENERAL_MEDICINE, false);

    when(service.addTurn(any())).thenReturn(mockResponse);

    mockMvc.perform(post("/api/turns")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)).with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.code").value("A01"));
  }

  @Test
  @WithMockUser(roles = {"MEDICAL_SECRETARY"})
  void testGetAllTurns() throws Exception {
    when(service.getTurns()).thenReturn(Collections.emptyList());

    mockMvc.perform(get("/api/turns"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").isArray());
  }

  @Test
  @WithMockUser(roles = {"MEDICAL_STAFF"})
  void testGetTurnsBySpeciality() throws Exception {
    when(service.getTurns(SpecialityEnum.GENERAL_MEDICINE)).thenReturn(Collections.emptyList());

    mockMvc.perform(get("/api/turns/GENERAL_MEDICINE"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").isArray());
  }

  @Test
  @WithMockUser(roles = {"MEDICAL_SECRETARY"})
  void testGetLastTurnFound() throws Exception {
    UserResponse userResponse = new UserResponse("1", "Ana", RoleEnum.STUDENT);
    TurnResponse turn =
            new TurnResponse(1L, "A02", userResponse, SpecialityEnum.GENERAL_MEDICINE, false);
    when(service.getLastCurrentTurn()).thenReturn(Optional.of(turn));

    mockMvc.perform(get("/api/turns/current-turn"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.code").value("A02"));
  }

  @Test
  @WithMockUser(roles = {"MEDICAL_SECRETARY"})
  void testGetLastTurnNotFound() throws Exception {
    when(service.getLastCurrentTurn()).thenReturn(Optional.empty());

    mockMvc.perform(get("/api/turns/current-turn"))
            .andExpect(status().isNoContent())
            .andExpect(jsonPath("$.success").value(false));
  }

  @Test
  @WithMockUser(roles = {"MEDICAL_STAFF"})
  void testCallNextTurn() throws Exception {
    CallTurnRequest request = new CallTurnRequest();
    request.setSpeciality(SpecialityEnum.GENERAL_MEDICINE);
    request.setLevelAttention(1);

    UserResponse userResponse = new UserResponse("1", "Luis", RoleEnum.STUDENT);
    TurnResponse response =
            new TurnResponse(1L, "A03", userResponse, SpecialityEnum.GENERAL_MEDICINE, false);
    when(service.callNextTurn(any(SpecialityEnum.class), anyInt())).thenReturn(response);

    mockMvc.perform(post("/api/turns/call-next")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)).with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.code").value("A03"));
  }

  @Test
  @WithMockUser(roles = {"MEDICAL_SECRETARY","ADMINISTRATOR"})
  void testEnableTurns() throws Exception {
    Mockito.doNothing().when(service).enableTurns();

    mockMvc.perform(post("/api/turns/enable").with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Turns enabled"));
  }

  @Test
  @WithMockUser(roles = {"MEDICAL_SECRETARY","ADMINISTRATOR"})
  void testDisableTurns() throws Exception {
    Mockito.doNothing().when(service).disableTurns();

    mockMvc.perform(post("/api/turns/disable").with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Turns disabled"));
  }

  @Test
  @WithMockUser(roles = {"MEDICAL_STAFF"})
  void testCallTurn() throws Exception {
    CallTurnRequest request = new CallTurnRequest();
    request.setTurnId(1L);
    request.setSpeciality(SpecialityEnum.GENERAL_MEDICINE);
    request.setLevelAttention(1);

    UserResponse userResponse = new UserResponse("1", "Luis", RoleEnum.STUDENT);
    TurnResponse response =
            new TurnResponse(1L, "A03", userResponse, SpecialityEnum.GENERAL_MEDICINE, false);
    when(service.callNextTurn(eq(1L), any(SpecialityEnum.class), anyInt())).thenReturn(response);

    mockMvc.perform(post("/api/turns/call")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)).with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.code").value("A03"));
  }

  @Test
  @WithMockUser(roles = {"MEDICAL_STAFF"})
  void testGetLastTurnBySpeciality() throws Exception {
    UserResponse userResponse = new UserResponse("1", "Ana", RoleEnum.STUDENT);
    TurnResponse turn =
            new TurnResponse(1L, "A02", userResponse, SpecialityEnum.GENERAL_MEDICINE, false);
    when(service.getCurrentTurn(SpecialityEnum.GENERAL_MEDICINE)).thenReturn(Optional.of(turn));

    mockMvc.perform(get("/api/turns/current-turn/GENERAL_MEDICINE"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.code").value("A02"));
  }

  @Test
  @WithMockUser(roles = {"MEDICAL_STAFF"})
  void testGetLastTurn_noTurnFound_returnsNoContent() throws Exception {
    SpecialityEnum speciality = SpecialityEnum.GENERAL_MEDICINE;

    when(service.getCurrentTurn(speciality)).thenReturn(Optional.empty());

    mockMvc.perform(get("/api/turns/current-turn/{speciality}", speciality.name()))
            .andExpect(status().isNoContent())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.message").value("No turn found"))
            .andExpect(jsonPath("$.data").doesNotExist());
  }

  @Test
  @WithMockUser(roles = {"MEDICAL_STAFF"})
  void testSkipTurn() throws Exception {
    Mockito.doNothing().when(service).skipTurn(SpecialityEnum.GENERAL_MEDICINE);

    mockMvc.perform(post("/api/turns/skip")
                    .param("speciality", "GENERAL_MEDICINE")
                    .contentType(MediaType.APPLICATION_JSON).with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Successfully skipped turn"));
  }

  @Test
  @WithMockUser(roles = {"MEDICAL_SECRETARY","ADMINISTRATOR"})
  void testEnableTurnsBySpeciality() throws Exception {
    Mockito.doNothing().when(service).enableTurns(SpecialityEnum.GENERAL_MEDICINE);

    mockMvc.perform(post("/api/turns/enable/GENERAL_MEDICINE").with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Turns enabled"));
  }

  @Test
  @WithMockUser(roles = {"MEDICAL_SECRETARY","ADMINISTRATOR"})
  void testDisableTurnsBySpeciality() throws Exception {
    Mockito.doNothing().when(service).disableTurns(SpecialityEnum.GENERAL_MEDICINE);

    mockMvc.perform(post("/api/turns/disable/GENERAL_MEDICINE").with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Turns disabled"));
  }


  @Test
  @WithMockUser(roles = {"MEDICAL_SECRETARY","ADMINISTRATOR","MEDICAL_STAFF"})
  void shouldReturnTurnsEnabledStatus() throws Exception {
    when(service.areTurnsEnabled()).thenReturn(true);
    mockMvc.perform(get("/api/turns/are-enabled")
                    .header("Authorization", "Bearer mocktoken"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("Turns enabled status"))
            .andExpect(jsonPath("$.data").value(true));
  }


  @Test
  @WithMockUser(roles = {"MEDICAL_SECRETARY","ADMINISTRATOR","MEDICAL_STAFF"})
  void shouldReturnSpecialtiesDisabled() throws Exception {
    List<SpecialityEnum> disabledSpecialties = List.of(SpecialityEnum.DENTISTRY, SpecialityEnum.GENERAL_MEDICINE);
    when(service.getSpecialtiesDisabled()).thenReturn(disabledSpecialties);
    mockMvc.perform(get("/api/turns/specialties-disabled")
                    .header("Authorization", "Bearer mocktoken"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("Disabled specialties"))
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data[0]").value("DENTISTRY"))
            .andExpect(jsonPath("$.data[1]").value("GENERAL_MEDICINE"));
  }
}
