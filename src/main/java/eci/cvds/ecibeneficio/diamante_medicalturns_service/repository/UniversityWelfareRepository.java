package eci.cvds.ecibeneficio.diamante_medicalturns_service.repository;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.UniversityWelfare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UniversityWelfareRepository extends JpaRepository<UniversityWelfare, Integer> {
    @Query("SELECT u FROM UniversityWelfare u WHERE u.id = 1")
    UniversityWelfare getUniversityWelfare();
}
