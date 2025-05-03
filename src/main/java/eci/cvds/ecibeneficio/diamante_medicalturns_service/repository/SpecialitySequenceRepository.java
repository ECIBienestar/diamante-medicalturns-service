package eci.cvds.ecibeneficio.diamante_medicalturns_service.repository;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.SpecialitySequence;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.SpecialityEnum;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecialitySequenceRepository
    extends JpaRepository<SpecialitySequence, SpecialityEnum> {
  Optional<SpecialitySequence> findBySpeciality(SpecialityEnum speciality);
}
