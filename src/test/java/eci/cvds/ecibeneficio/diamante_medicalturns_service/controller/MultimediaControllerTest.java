package eci.cvds.ecibeneficio.diamante_medicalturns_service.controller;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request.CreateMultimediaRequest;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.response.MultimediaResponse;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.enums.TypeEnum;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.service.MultimediaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MultimediaControllerTest {

    @Mock
    private MultimediaService multimediaService;

    @InjectMocks
    private MultimediaController multimediaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void uploadMultimedia_ShouldReturnOk() {
        CreateMultimediaRequest request = new CreateMultimediaRequest();
        request.setName("Nombre");
        request.setUrl("https://url");
        request.setDuration(30);
        request.setType(TypeEnum.VIDEO);

        ResponseEntity<Void> response = multimediaController.uploadMultimedia(request);

        verify(multimediaService).createMultimedia(request);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void getById_ShouldReturnMultimedia() {
        Long id = 1L;
        MultimediaResponse expectedResponse = new MultimediaResponse();
        expectedResponse.setId(id);
        expectedResponse.setName("Test");
        expectedResponse.setUrl("https://url");
        expectedResponse.setDuration(45);
        expectedResponse.setType(TypeEnum.VIDEO);

        when(multimediaService.getMultimedia(id)).thenReturn(expectedResponse);

        ResponseEntity<MultimediaResponse> response = multimediaController.getById(id);

        verify(multimediaService).getMultimedia(id);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void getAll_ShouldReturnListOfMultimedia() {
        MultimediaResponse item1 = new MultimediaResponse();
        item1.setId(1L);
        item1.setName("Archivo 1");
        item1.setUrl("https://archivo1");
        item1.setDuration(60);
        item1.setType(TypeEnum.VIDEO);

        MultimediaResponse item2 = new MultimediaResponse();
        item2.setId(2L);
        item2.setName("Archivo 2");
        item2.setUrl("https://archivo2");
        item2.setDuration(90);
        item2.setType(TypeEnum.VIDEO);

        List<MultimediaResponse> expectedList = Arrays.asList(item1, item2);

        when(multimediaService.getAllMultimedia()).thenReturn(expectedList);

        ResponseEntity<List<MultimediaResponse>> response = multimediaController.getAll();

        verify(multimediaService).getAllMultimedia();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedList, response.getBody());
    }

    @Test
    void delete_ShouldReturnNoContent() {
        Long id = 1L;

        ResponseEntity<Void> response = multimediaController.delete(id);

        verify(multimediaService).deleteMultimedia(id);
        assertEquals(204, response.getStatusCodeValue());
    }
}
