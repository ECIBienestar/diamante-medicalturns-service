package eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.mapper;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.response.MultimediaResponse;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.Multimedia;

public class MultimediaMapper {
  private MultimediaMapper() {}

  public static MultimediaResponse toResponse(Multimedia multimedia) {
    return new MultimediaResponse(
        multimedia.getId(),
        multimedia.getType(),
        multimedia.getName(),
        multimedia.getUrl(),
        multimedia.getDuration());
  }
}
