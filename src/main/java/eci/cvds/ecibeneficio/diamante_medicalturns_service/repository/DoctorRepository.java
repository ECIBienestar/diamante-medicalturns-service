package eci.cvds.ecibeneficio.diamante_medicalturns_service.repository;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, String> {}
