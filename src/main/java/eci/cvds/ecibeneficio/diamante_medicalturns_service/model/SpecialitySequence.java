package eci.cvds.ecibeneficio.diamante_medicalturns_service.model;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.SpecialityEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "speciality_sequence")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpecialitySequence {
  @Id
  @Enumerated(EnumType.STRING)
  private SpecialityEnum speciality;

  private int sequence;
}
