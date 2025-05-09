package eci.cvds.ecibeneficio.diamante_medicalturns_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request.CreateMultimediaRequest;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.response.MultimediaResponse;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.Multimedia;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.enums.TypeEnum;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.repository.MultimediaRepository;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.service.impl.MultimediaServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class MultimediaServiceTest {

    @Mock
    private MultimediaRepository multimediaRepository;

    @InjectMocks
    private MultimediaServiceImpl multimediaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateMultimedia() {
        CreateMultimediaRequest request = new CreateMultimediaRequest();
        request.setName("Video A");
        request.setUrl("http://video.com/a");
        request.setDuration(120);
        request.setType(TypeEnum.VIDEO);

        Multimedia saved = new Multimedia(TypeEnum.VIDEO, "Video A", "http://video.com/a", 120);

        when(multimediaRepository.save(any(Multimedia.class))).thenReturn(saved);

        assertDoesNotThrow(() -> multimediaService.createMultimedia(request));
        verify(multimediaRepository, times(1)).save(any(Multimedia.class));
    }

    @Test
    void testGetMultimediaByIdFound() {
        Multimedia multimedia = new Multimedia(TypeEnum.GIF, "GIF A", "http://gif.com/a", 10);
        multimedia.setId(1L);

        when(multimediaRepository.findById(1L)).thenReturn(Optional.of(multimedia));

        MultimediaResponse response = multimediaService.getMultimedia(1L);

        assertNotNull(response);
        assertEquals("GIF A", response.getName());
        assertEquals(TypeEnum.GIF, response.getType());
    }

    @Test
    void testGetMultimediaByIdNotFound() {
        when(multimediaRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> multimediaService.getMultimedia(2L));
    }

    @Test
    void testGetAllMultimedia() {
        Multimedia multimedia1 = new Multimedia(TypeEnum.VIDEO, "Video A", "url1", 100);
        multimedia1.setId(1L);

        Multimedia multimedia2 = new Multimedia(TypeEnum.GIF, "GIF B", "url2", 5);
        multimedia2.setId(2L);

        List<Multimedia> entities = Arrays.asList(multimedia1, multimedia2);

        when(multimediaRepository.findAll()).thenReturn(entities);

        List<MultimediaResponse> responses = multimediaService.getAllMultimedia();

        assertEquals(2, responses.size());
        assertEquals("Video A", responses.get(0).getName());
        assertEquals(TypeEnum.GIF, responses.get(1).getType());
    }

    @Test
    void testDeleteMultimedia() {
        doNothing().when(multimediaRepository).deleteById(5L);

        multimediaService.deleteMultimedia(5L);

        verify(multimediaRepository, times(1)).deleteById(5L);
    }
}
