package eci.cvds.ecibeneficio.diamante_medicalturns_service.repository;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.SpecialitySequence;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.SpecialityEnum;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SpecialitySequenceRepository
    extends JpaRepository<SpecialitySequence, SpecialityEnum> {
  Optional<SpecialitySequence> findBySpeciality(SpecialityEnum speciality);

  @Modifying
  @Query("UPDATE SpecialitySequence seq SET seq.sequence = 0")
  void resetSequences();
}
