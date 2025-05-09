package eci.cvds.ecibeneficio.diamante_medicalturns_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.exception.MedicalTurnsException;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.SpecialitySequence;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.repository.SpecialitySequenceRepository;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.service.impl.SpecialitySequenceServiceImpl;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.SpecialityEnum;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

public class SpecialitySequenceServiceImplTest {
  private SpecialitySequenceRepository repository;
  private SpecialitySequenceServiceImpl service;

  @BeforeEach
  public void setUp() {
    repository = mock(SpecialitySequenceRepository.class);
    service = new SpecialitySequenceServiceImpl(repository);
  }

  @Test
  void shouldReturnSequenceForExistingSpeicality() {
    SpecialitySequence sequence = new SpecialitySequence();
    sequence.setSequence(5);
    when(repository.findBySpeciality(SpecialityEnum.MEDICINA_GENERAL))
        .thenReturn(Optional.of(sequence));

    int result = service.getSequence(SpecialityEnum.MEDICINA_GENERAL);

    assertEquals(5, result);
    verify(repository).findBySpeciality(SpecialityEnum.MEDICINA_GENERAL);
  }

  @Test
  void shouldThrowExceptionWhenSpecialityNotFound() {
    when(repository.findBySpeciality(SpecialityEnum.PSICOLOGIA)).thenReturn(Optional.empty());

    MedicalTurnsException e =
        assertThrows(
            MedicalTurnsException.class, () -> service.getSequence(SpecialityEnum.PSICOLOGIA));

    assertEquals(MedicalTurnsException.SEQUENCE_NOT_FOUND, e.getMessage());
  }

  @Test
  void shouldIncrementSequenceCorrectly() {
    SpecialitySequence sequence = new SpecialitySequence();
    sequence.setSequence(5);
    when(repository.findBySpeciality(SpecialityEnum.ODONTOLOGIA)).thenReturn(Optional.of(sequence));

    service.incrementSequence(SpecialityEnum.ODONTOLOGIA);

    ArgumentCaptor<SpecialitySequence> captor = ArgumentCaptor.forClass(SpecialitySequence.class);
    verify(repository).save(captor.capture());

    assertEquals(6, captor.getValue().getSequence());
  }

  @Test
  void shouldRestartSequence() {
    service.restartSequences();
    verify(repository).resetSequences();
  }
}
