package eci.cvds.ecibeneficio.diamante_medicalturns_service.service.impl;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.exception.MedicalTurnsException;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.SpecialitySequence;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.repository.SpecialitySequenceRepository;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.service.SpecialitySequenceService;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.SpecialityEnum;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SpecialitySequenceServiceImpl implements SpecialitySequenceService {
  private final SpecialitySequenceRepository specialitySequenceRepository;

  @Override
  public int getSequence(SpecialityEnum speciality) {
    return getSpecialitySequence(speciality).getSequence();
  }

  @Override
  public void incrementSequence(SpecialityEnum speciality) {
    SpecialitySequence sequence = getSpecialitySequence(speciality);
    int newSequence = sequence.getSequence() + 1;
    sequence.setSequence(newSequence);
    specialitySequenceRepository.save(sequence);
  }

  @Override
  @Scheduled(cron = "0 0 0 * * *", zone = "America/Bogota")
  public void restartSequences() {
    specialitySequenceRepository.resetSequences();
  }

  private SpecialitySequence getSpecialitySequence(SpecialityEnum speciality) {
    return specialitySequenceRepository
        .findBySpeciality(speciality)
        .orElseThrow(() -> new MedicalTurnsException(MedicalTurnsException.SEQUENCE_NOT_FOUND));
  }
}
