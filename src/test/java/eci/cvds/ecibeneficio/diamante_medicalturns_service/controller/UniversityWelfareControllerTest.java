package eci.cvds.ecibeneficio.diamante_medicalturns_service.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request.CallTurnRequest;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request.CreateTurnRequest;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.response.TurnResponse;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.response.UserResponse;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.service.UniversityWelfareService;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.RoleEnum;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.SpecialityEnum;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UniversityWelfareController.class)
class UniversityWelfareControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private UniversityWelfareService service;

  @Autowired private ObjectMapper objectMapper;

  @Test
  void testAddTurn() throws Exception {
    CreateTurnRequest request = new CreateTurnRequest(); // set campos si es necesario
    UserResponse userResponse = new UserResponse("1", "Juan", RoleEnum.STUDENT);
    TurnResponse mockResponse =
        new TurnResponse(1L, "A01", userResponse, SpecialityEnum.GENERAL_MEDICINE, false);

    when(service.addTurn(any())).thenReturn(mockResponse);

    mockMvc
        .perform(
            post("/api/turns")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.code").value("A01"));
  }

  @Test
  void testGetAllTurns() throws Exception {
    when(service.getTurns()).thenReturn(Collections.emptyList());

    mockMvc
        .perform(get("/api/turns"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").isArray());
  }

  @Test
  void testGetTurnsBySpeciality() throws Exception {
    when(service.getTurns(SpecialityEnum.GENERAL_MEDICINE)).thenReturn(Collections.emptyList());

    mockMvc
        .perform(get("/api/turns/GENERAL_MEDICINE"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").isArray());
  }

  @Test
  void testGetLastTurnFound() throws Exception {
    UserResponse userResponse = new UserResponse("1", "Ana", RoleEnum.STUDENT);
    TurnResponse turn =
        new TurnResponse(1L, "A02", userResponse, SpecialityEnum.GENERAL_MEDICINE, false);
    when(service.getLastCurrentTurn()).thenReturn(Optional.of(turn));

    mockMvc
        .perform(get("/api/turns/current-turn"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.code").value("A02"));
  }

  @Test
  void testGetLastTurnNotFound() throws Exception {
    when(service.getLastCurrentTurn()).thenReturn(Optional.empty());

    mockMvc
        .perform(get("/api/turns/current-turn"))
        .andExpect(status().isNoContent())
        .andExpect(jsonPath("$.success").value(false));
  }

  @Test
  void testCallNextTurn() throws Exception {
    CallTurnRequest request = new CallTurnRequest();
    request.setSpeciality(SpecialityEnum.GENERAL_MEDICINE);
    request.setLevelAttention(1);

    UserResponse userResponse = new UserResponse("1", "Luis", RoleEnum.STUDENT);
    TurnResponse response =
        new TurnResponse(1L, "A03", userResponse, SpecialityEnum.GENERAL_MEDICINE, false);
    when(service.callNextTurn(any(SpecialityEnum.class), anyInt())).thenReturn(response);

    mockMvc
        .perform(
            post("/api/turns/call-next")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.code").value("A03"));
  }

  @Test
  void testEnableTurns() throws Exception {
    mockMvc
        .perform(post("/api/turns/enable"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Turns enabled"));
  }

  @Test
  void testDisableTurns() throws Exception {
    mockMvc
        .perform(post("/api/turns/disable"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Turns disabled"));
  }

  @Test
  void testCallTurn() throws Exception {
    CallTurnRequest request = new CallTurnRequest();
    request.setTurnId(1L);
    request.setSpeciality(SpecialityEnum.GENERAL_MEDICINE);
    request.setLevelAttention(1);

    UserResponse userResponse = new UserResponse("1", "Luis", RoleEnum.STUDENT);
    TurnResponse response =
        new TurnResponse(eq(1L), "A03", userResponse, SpecialityEnum.GENERAL_MEDICINE, false);
    when(service.callNextTurn(1L, any(SpecialityEnum.class), anyInt())).thenReturn(response);

    mockMvc
        .perform(
            post("/api/turns/call")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.code").value("A03"));
  }

  @Test
  void testGetLastTurnBySpeciality() throws Exception {
    UserResponse userResponse = new UserResponse("1", "Ana", RoleEnum.STUDENT);
    TurnResponse turn =
        new TurnResponse(1L, "A02", userResponse, SpecialityEnum.GENERAL_MEDICINE, false);
    when(service.getCurrentTurn(SpecialityEnum.GENERAL_MEDICINE)).thenReturn(Optional.of(turn));

    mockMvc
        .perform(get("/api/turns/current-turn/GENERAL_MEDICINE"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.code").value("A02"));
  }

  @Test
  void testGetLastTurn_noTurnFound_returnsNoContent() throws Exception {
    SpecialityEnum speciality = SpecialityEnum.GENERAL_MEDICINE;

    when(service.getCurrentTurn(speciality)).thenReturn(Optional.empty());

    mockMvc
        .perform(get("/api/turns/current-turn/{speciality}", speciality.name()))
        .andExpect(status().isNoContent())
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.message").value("No turn found"))
        .andExpect(jsonPath("$.data").doesNotExist());
  }

  @Test
  void testSkipTurn() throws Exception {
    Mockito.doNothing().when(service).skipTurn(SpecialityEnum.GENERAL_MEDICINE);

    mockMvc
        .perform(
            post("/api/turns/skip")
                .param("speciality", "GENERAL_MEDICINE")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Successfully skipped turn"));
  }

  @Test
  void testEnableTurnsBySpeciality() throws Exception {
    Mockito.doNothing().when(service).enableTurns(SpecialityEnum.GENERAL_MEDICINE);

    mockMvc
        .perform(post("/api/turns/enable/GENERAL_MEDICINE"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Turns enabled"));
  }

  @Test
  void testDisableTurnsBySpeciality() throws Exception {
    Mockito.doNothing().when(service).disableTurns(SpecialityEnum.GENERAL_MEDICINE);

    mockMvc
        .perform(post("/api/turns/disable/GENERAL_MEDICINE"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Turns disabled"));
  }
}
