package eci.cvds.ecibeneficio.diamante_medicalturns_service.config.initializer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.UniversityWelfare;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.repository.UniversityWelfareRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

public class UniversityWelfareInitializerTest {
  private UniversityWelfareRepository universityWelfareRepository;
  private UniversityWelfareInitializer initializer;

  @BeforeEach
  public void init() {
    universityWelfareRepository = mock(UniversityWelfareRepository.class);
    initializer = new UniversityWelfareInitializer(universityWelfareRepository);
  }

  @Test
  public void shouldNotSaveIfUniversityWelfareExists() {
    when(universityWelfareRepository.findById(1)).thenReturn(Optional.of(new UniversityWelfare(1)));

    initializer.init();

    verify(universityWelfareRepository, never()).save(any());
  }

  @Test
  public void shouldSaveIfUniversityWelfareDoesNotExist() {
    when(universityWelfareRepository.findById(1)).thenReturn(Optional.empty());

    initializer.init();

    ArgumentCaptor<UniversityWelfare> captor = ArgumentCaptor.forClass(UniversityWelfare.class);
    verify(universityWelfareRepository).save(captor.capture());

    UniversityWelfare saved = captor.getValue();
    assertEquals(1, saved.getId());
  }
}
