package eci.cvds.ecibeneficio.diamante_medicalturns_service.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request.CreateMultimediaRequest;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.response.ApiResponse;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.response.MultimediaResponse;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.service.MultimediaService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.TypeEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

class MultimediaControllerTest {

  @Mock private MultimediaService multimediaService;

  @InjectMocks private MultimediaController multimediaController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void uploadMultimedia_ShouldReturnOk() throws IOException {
    CreateMultimediaRequest request = new CreateMultimediaRequest();
    request.setName("Nombre");
    request.setDuration(30);
    request.setType(TypeEnum.VIDEO);

    ResponseEntity<ApiResponse<Void>> response = multimediaController.uploadMultimedia(request);

    verify(multimediaService).createMultimedia(request);
    assertEquals(200, response.getStatusCode().value());
  }

  @Test
  void getById_ShouldReturnMultimedia() {
    Long id = 1L;
    MultimediaResponse expectedResponse = new MultimediaResponse(id,TypeEnum.VIDEO,"Test","https://url",45);

    when(multimediaService.getMultimedia(id)).thenReturn(expectedResponse);

    ResponseEntity<ApiResponse<MultimediaResponse>> response = multimediaController.getById(id);

    verify(multimediaService).getMultimedia(id);
    assertEquals(200, response.getStatusCode().value());
    assertEquals(expectedResponse, response.getBody().getData());
  }

  @Test
  void getAll_ShouldReturnListOfMultimedia() {
    MultimediaResponse item1 = new MultimediaResponse(1l, TypeEnum.VIDEO,"Archivo 1","https://archivo1",60);

    MultimediaResponse item2 = new MultimediaResponse(2l, TypeEnum.VIDEO,"Archivo 2","https://archivo2",90);

    List<MultimediaResponse> expectedList = Arrays.asList(item1, item2);

    when(multimediaService.getAllMultimedia()).thenReturn(expectedList);

    ResponseEntity<ApiResponse<List<MultimediaResponse>>> response = multimediaController.getAll();

    verify(multimediaService).getAllMultimedia();
    assertEquals(200, response.getStatusCode().value());
    assertEquals(expectedList, response.getBody().getData());
  }

  @Test
  void delete_ShouldReturnNoContent() {
    Long id = 1L;

    ResponseEntity<ApiResponse<Void>> response = multimediaController.delete(id);

    verify(multimediaService).deleteMultimedia(id);
    assertEquals(200, response.getStatusCode().value());
  }
}
