package eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.TypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateMultimediaRequest {
  private String name;
  private MultipartFile file;
  private int duration;
  private TypeEnum type;
}
