package eci.cvds.ecibeneficio.diamante_medicalturns_service.repository;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {}
