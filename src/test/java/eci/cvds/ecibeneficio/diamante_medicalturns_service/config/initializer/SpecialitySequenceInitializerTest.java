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

public class SpecialitySequenceInitializerTest {
  private SpecialitySequenceRepository specialitySequenceRepository;
  private SpecialitySequenceInitializer initializer;

  @BeforeEach
  public void init() {
    specialitySequenceRepository = mock(SpecialitySequenceRepository.class);
    initializer = new SpecialitySequenceInitializer(specialitySequenceRepository);
  }

  @Test
  public void shouldInitializeSequences() {
    when(specialitySequenceRepository.findById(SpecialityEnum.MEDICINA_GENERAL))
        .thenReturn(Optional.of(new SpecialitySequence(SpecialityEnum.MEDICINA_GENERAL, 5)));

    for (SpecialityEnum speciality : SpecialityEnum.values()) {
      if (speciality != SpecialityEnum.MEDICINA_GENERAL) {
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
      if (speciality != SpecialityEnum.MEDICINA_GENERAL) {
        assert (savedSpecialities.contains(speciality));
      } else {
        assert (!savedSpecialities.contains(speciality));
      }
    }
  }
}
