package eci.cvds.ecibeneficio.diamante_medicalturns_service.repository.projection;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.SpecialityEnum;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.StatusEnum;

public interface CountBySpeciality {
    SpecialityEnum getSpeciality();

    StatusEnum getStatus();

    Long getCount();
}
