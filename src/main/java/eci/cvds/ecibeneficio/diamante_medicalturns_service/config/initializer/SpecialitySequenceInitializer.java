package eci.cvds.ecibeneficio.diamante_medicalturns_service.config.initializer;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.SpecialitySequence;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.repository.SpecialitySequenceRepository;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.SpecialityEnum;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpecialitySequenceInitializer {
  private final SpecialitySequenceRepository specialitySequenceRepository;

  @PostConstruct
  public void initializeSequences() {
    for (SpecialityEnum speciality : SpecialityEnum.values()) {
      specialitySequenceRepository
          .findById(speciality)
          .orElseGet(
              () -> specialitySequenceRepository.save(new SpecialitySequence(speciality, 0)));
    }
  }
}
