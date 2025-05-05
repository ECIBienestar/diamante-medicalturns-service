package eci.cvds.ecibeneficio.diamante_medicalturns_service.model;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.RoleEnum;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.SpecialityEnum;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "doctor")
@PrimaryKeyJoinColumn(name = "id")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Doctor extends User {
  @Enumerated(EnumType.STRING)
  private SpecialityEnum speciality;

  public Doctor(String id, String name, RoleEnum role, SpecialityEnum speciality) {
    super(id, name, role);
    this.speciality = speciality;
  }
}
