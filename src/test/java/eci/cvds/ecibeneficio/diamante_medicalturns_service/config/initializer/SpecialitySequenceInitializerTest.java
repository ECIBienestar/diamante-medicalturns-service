package eci.cvds.ecibeneficio.diamante_medicalturns_service.config.initializer;

import static org.mockito.Mockito.*;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.SpecialitySequence;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.repository.SpecialitySequenceRepository;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.SpecialityEnum;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class SpecialitySequenceInitializerTest {
  private SpecialitySequenceRepository specialitySequenceRepository;
  private SpecialitySequenceInitializer initializer;

  @BeforeEach
  void init() {
    specialitySequenceRepository = mock(SpecialitySequenceRepository.class);
    initializer = new SpecialitySequenceInitializer(specialitySequenceRepository);
  }

  @Test
  void shouldInitializeSequences() {
    when(specialitySequenceRepository.findById(SpecialityEnum.GENERAL_MEDICINE))
        .thenReturn(Optional.of(new SpecialitySequence(SpecialityEnum.GENERAL_MEDICINE, 5)));

    for (SpecialityEnum speciality : SpecialityEnum.values()) {
      if (speciality != SpecialityEnum.GENERAL_MEDICINE) {
        when(specialitySequenceRepository.findById(speciality)).thenReturn(Optional.empty());
      }
    }

    initializer.initializeSequences();

    ArgumentCaptor<SpecialitySequence> captor = ArgumentCaptor.forClass(SpecialitySequence.class);
    verify(specialitySequenceRepository, times(SpecialityEnum.values().length - 1))
        .save(captor.capture());

    Set<SpecialityEnum> savedSpecialities =
        captor.getAllValues().stream()
            .map(SpecialitySequence::getSpeciality)
            .collect(Collectors.toSet());

    for (SpecialityEnum speciality : SpecialityEnum.values()) {
      if (speciality != SpecialityEnum.GENERAL_MEDICINE) {
        assert (savedSpecialities.contains(speciality));
      } else {
        assert (!savedSpecialities.contains(speciality));
      }
    }
  }
}
