package eci.cvds.ecibeneficio.diamante_medicalturns_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request.CallTurnRequest;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request.CreateTurnRequest;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.response.TurnResponse;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.service.UniversityWelfareService;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.SpecialityEnum;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UniversityWelfareController.class)
public class UniversityWelfareControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UniversityWelfareService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAddTurn() throws Exception {
        CreateTurnRequest request = new CreateTurnRequest(); // set campos si es necesario
        TurnResponse mockResponse = new TurnResponse("A01", "Juan", SpecialityEnum.MEDICINA_GENERAL, LocalDateTime.now());

        Mockito.when(service.addTurn(any())).thenReturn(mockResponse);

        mockMvc.perform(post("/api/turns")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.code").value("A01"));
    }

    @Test
    void testGetAllTurns() throws Exception {
        Mockito.when(service.getTurns()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/turns"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void testGetTurnsBySpeciality() throws Exception {
        Mockito.when(service.getTurns(SpecialityEnum.MEDICINA_GENERAL)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/turns/MEDICINA_GENERAL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void testGetLastTurnFound() throws Exception {
        TurnResponse turn = new TurnResponse("A02", "Ana", SpecialityEnum.MEDICINA_GENERAL, LocalDateTime.now());
        Mockito.when(service.getLastCurrentTurn()).thenReturn(Optional.of(turn));

        mockMvc.perform(get("/api/turns/current-turn"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.code").value("A02"));
    }

    @Test
    void testGetLastTurnNotFound() throws Exception {
        Mockito.when(service.getLastCurrentTurn()).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/turns/current-turn"))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void testCallNextTurn() throws Exception {
        CallTurnRequest request = new CallTurnRequest();
        request.setDoctorId("doc123");
        request.setSpeciality(SpecialityEnum.MEDICINA_GENERAL);
        request.setLevelAttention(1);

        TurnResponse response = new TurnResponse("A03", "Luis", SpecialityEnum.MEDICINA_GENERAL, LocalDateTime.now());
        Mockito.when(service.callNextTurn(anyString(), any(SpecialityEnum.class), anyInt()))
                .thenReturn(response);


        mockMvc.perform(post("/api/turns/call-next")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.code").value("A03"));
    }

    @Test
    void testEnableTurns() throws Exception {
        mockMvc.perform(post("/api/turns/enable"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Turns enabled"));
    }

    @Test
    void testDisableTurns() throws Exception {
        mockMvc.perform(post("/api/turns/disable"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Turns disabled"));
    }
}
