package eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.response;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.TypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MultimediaResponse {
    private Long id;
    private TypeEnum type;
    private String name;
    private String url;
    private int duration;
}
