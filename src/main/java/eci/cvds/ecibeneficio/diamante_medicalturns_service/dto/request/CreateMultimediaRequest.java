package eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.enums.TypeEnum;
import lombok.Data;

@Data
public class CreateMultimediaRequest {
    private String name;
    private String url;
    private int duration;
    private TypeEnum type;
}
