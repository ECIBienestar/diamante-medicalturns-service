package eci.cvds.ecibeneficio.diamante_medicalturns_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request.CreateMultimediaRequest;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.response.MultimediaResponse;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.exception.MedicalTurnsException;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.Multimedia;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.TypeEnum;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.repository.MultimediaRepository;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.service.impl.MultimediaServiceImpl;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

class MultimediaServiceTest {
  @Mock private MultimediaRepository multimediaRepository;
  @Mock private WebClient webClient;
  @Mock private WebClient.RequestBodyUriSpec requestBodyUriSpec;
  @Mock private WebClient.RequestBodySpec requestBodySpec;
  @Mock private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
  @Mock private WebClient.RequestHeadersSpec requestHeadersSpec;
  @Mock private WebClient.ResponseSpec responseSpec;

  @InjectMocks private MultimediaServiceImpl multimediaService;

  Multimedia multimedia1;
  Multimedia multimedia2;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    multimediaService = new MultimediaServiceImpl(multimediaRepository, webClient);

    multimedia1 = new Multimedia(TypeEnum.VIDEO, "Test", "url", 10);
    multimedia2 = new Multimedia(TypeEnum.GIF, "Test", "url", 10);
  }

  @Test
  void testCreateMultimedia() throws IOException {
    MockMultipartFile file =
        new MockMultipartFile("file", "video.mp4", "video/mp4", "dummy content".getBytes());
    CreateMultimediaRequest request =
        new CreateMultimediaRequest("videoName", file, 110, TypeEnum.VIDEO);

    when(webClient.put()).thenReturn(requestBodyUriSpec);
    when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
    when(requestBodySpec.header(anyString(), anyString())).thenReturn(requestBodySpec);
    when(requestBodySpec.bodyValue(any(ByteArrayResource.class))).thenReturn(requestHeadersSpec);
    when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just("ok"));

    multimediaService.createMultimedia(request);

    verify(webClient).put();
    verify(multimediaRepository).save(any(Multimedia.class));
  }

  @Test
  void shouldGetMultimediaById() {
    when(multimediaRepository.findById(1L)).thenReturn(Optional.of(multimedia1));

    MultimediaResponse response = multimediaService.getMultimedia(1L);

    assertEquals(multimedia1.getName(), response.getName());
    assertEquals(multimedia1.getType(), response.getType());
    assertEquals(multimedia1.getUrl(), response.getUrl());
    assertEquals(multimedia1.getDuration(), response.getDuration());
  }

  @Test
  void shouldThrowExceptionWhenMultimediaNotFound() {
    when(multimediaRepository.findById(1L)).thenReturn(Optional.empty());

    MedicalTurnsException exception =
        assertThrows(MedicalTurnsException.class, () -> multimediaService.getMultimedia(1L));

    assertEquals(MedicalTurnsException.MULTIMEDIA_NOT_FOUND, exception.getMessage());
  }

  @Test
  void ShoulReturnAllMultimedia() {
    List<Multimedia> list = List.of(multimedia1, multimedia2);

    when(multimediaRepository.findAll()).thenReturn(list);

    List<MultimediaResponse> response = multimediaService.getAllMultimedia();

    assertEquals(list.size(), response.size());

    for (int i = 0; i < list.size(); i++) {
      Multimedia expected = list.get(i);
      MultimediaResponse actual = response.get(i);

      assertEquals(expected.getName(), actual.getName());
      assertEquals(expected.getType(), actual.getType());
    }
  }

  @Test
  void testDeleteMultimedia() {
    when(multimediaRepository.findById(1L)).thenReturn(java.util.Optional.of(multimedia1));
    when(webClient.delete()).thenReturn(requestHeadersUriSpec);
    when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
    when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    when(responseSpec.bodyToMono(Void.class)).thenReturn(Mono.empty());

    multimediaService.deleteMultimedia(1L);

    verify(webClient).delete();
    verify(multimediaRepository).deleteById(1L);
  }
}
