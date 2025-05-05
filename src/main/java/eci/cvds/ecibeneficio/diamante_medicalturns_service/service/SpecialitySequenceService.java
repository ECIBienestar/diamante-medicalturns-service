package eci.cvds.ecibeneficio.diamante_medicalturns_service.service;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.SpecialityEnum;

public interface SpecialitySequenceService {
  int getSequence(SpecialityEnum speciality);

  void incrementSequence(SpecialityEnum speciality);

  void restartSequences();
}
