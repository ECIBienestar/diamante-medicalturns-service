package eci.cvds.ecibeneficio.diamante_medicalturns_service.service;

import java.util.List;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request.CreateMultimediaRequest;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.response.MultimediaResponse;

public interface MultimediaService {
    void createMultimedia(CreateMultimediaRequest request);
    MultimediaResponse getMultimedia(Long id);
    List<MultimediaResponse> getAllMultimedia();
    void deleteMultimedia(Long id);
}