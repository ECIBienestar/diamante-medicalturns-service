package eci.cvds.ecibeneficio.diamante_medicalturns_service.model;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "app_user")
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
  @Id private String id;
  private String name;

  @Enumerated(EnumType.STRING)
  private RoleEnum role;
}
