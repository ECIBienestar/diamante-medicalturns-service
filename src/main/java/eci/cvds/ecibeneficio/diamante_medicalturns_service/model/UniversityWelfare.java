package eci.cvds.ecibeneficio.diamante_medicalturns_service.model;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.SpecialityEnum;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "university_welfare")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UniversityWelfare {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private boolean disableTurns;

  @ElementCollection(targetClass = SpecialityEnum.class)
  @Enumerated(EnumType.STRING)
  @CollectionTable(
      name = "disable_turns_speciality",
      joinColumns = @JoinColumn(name = "university_welfare_id"))
  @Column(name = "speciality")
  private List<SpecialityEnum> disbaleTurnsBySpeciality = new ArrayList<SpecialityEnum>();

  public void disableTurns(SpecialityEnum speciality) {
    disbaleTurnsBySpeciality.add(speciality);
  }

  public void enableTurns(SpecialityEnum speciality) {
    disbaleTurnsBySpeciality.remove(speciality);
  }
}
