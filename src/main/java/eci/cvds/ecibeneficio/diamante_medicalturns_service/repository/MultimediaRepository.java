package eci.cvds.ecibeneficio.diamante_medicalturns_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.Multimedia;

public interface MultimediaRepository extends JpaRepository<Multimedia, Long> {
    
}

