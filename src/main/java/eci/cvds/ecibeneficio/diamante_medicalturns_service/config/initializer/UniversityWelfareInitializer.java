package eci.cvds.ecibeneficio.diamante_medicalturns_service.config.initializer;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.UniversityWelfare;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.repository.UniversityWelfareRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniversityWelfareInitializer {
  private final UniversityWelfareRepository universityWelfareRepository;

  @PostConstruct
  public void init() {
    universityWelfareRepository
        .findById(1)
        .orElseGet(() -> universityWelfareRepository.save(new UniversityWelfare(1)));
  }
}
