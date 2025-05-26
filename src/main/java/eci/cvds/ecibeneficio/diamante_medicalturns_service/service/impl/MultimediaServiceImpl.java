package eci.cvds.ecibeneficio.diamante_medicalturns_service.service.impl;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request.CreateMultimediaRequest;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.response.MultimediaResponse;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.exception.MedicalTurnsException;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.Multimedia;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.repository.MultimediaRepository;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.service.MultimediaService;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.mapper.MultimediaMapper;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class MultimediaServiceImpl implements MultimediaService {
  private final MultimediaRepository multimediaRepository;
  private final WebClient webClient;

  @Value("${PROD_SUPABASE_PROJECT_ID}")
  private String projectId;

  @Value("${PROD_SUPABASE_BUCKET_NAME}")
  private String bucket;

  @Override
  public void createMultimedia(CreateMultimediaRequest request) throws IOException {
    MultipartFile file = request.getFile();
    ByteArrayResource resource = new ByteArrayResource(file.getBytes());
    String url =
        String.format(
            "https://%s.supabase.co/storage/v1/object/%s/%s",
            projectId, bucket, file.getOriginalFilename());

    webClient
        .put()
        .uri(url)
        .header(HttpHeaders.CONTENT_TYPE, file.getContentType())
        .bodyValue(resource)
        .retrieve()
        .bodyToMono(String.class)
        .block();

    Multimedia multimedia =
        new Multimedia(request.getType(), request.getName(), url, request.getDuration());
    multimediaRepository.save(multimedia);
  }

  @Override
  public MultimediaResponse getMultimedia(Long id) {
    return MultimediaMapper.toResponse(findMultimedia(id));
  }

  @Override
  public List<MultimediaResponse> getAllMultimedia() {
    return multimediaRepository.findAll().stream().map(MultimediaMapper::toResponse).toList();
  }

  @Override
  public void deleteMultimedia(Long id) {
    Multimedia multimedia = findMultimedia(id);
    String url = multimedia.getUrl();

    webClient.delete().uri(url).retrieve().bodyToMono(Void.class).block();

    multimediaRepository.deleteById(id);
  }

  private Multimedia findMultimedia(Long id) {
    return multimediaRepository
        .findById(id)
        .orElseThrow(() -> new MedicalTurnsException(MedicalTurnsException.MULTIMEDIA_NOT_FOUND));
  }
}
