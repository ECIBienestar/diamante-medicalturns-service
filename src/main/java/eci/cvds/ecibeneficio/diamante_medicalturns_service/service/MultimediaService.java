package eci.cvds.ecibeneficio.diamante_medicalturns_service.service;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request.CreateMultimediaRequest;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.response.MultimediaResponse;
import java.io.IOException;
import java.util.List;

public interface MultimediaService {
  void createMultimedia(CreateMultimediaRequest request) throws IOException;

  MultimediaResponse getMultimedia(Long id);

  List<MultimediaResponse> getAllMultimedia();

  void deleteMultimedia(Long id);
}
