package eci.cvds.ecibeneficio.diamante_medicalturns_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class DiamanteMedicalturnsServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(DiamanteMedicalturnsServiceApplication.class, args);
  }
}
