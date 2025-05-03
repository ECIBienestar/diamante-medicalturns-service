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
  @Id private int id;

  @OneToOne
  @JoinColumn(name = "turn_id")
  private Turn lastTurn;

  private boolean disableTurns = false;

  @ElementCollection(targetClass = SpecialityEnum.class)
  @Enumerated(EnumType.STRING)
  @CollectionTable(
      name = "disable_turns_speciality",
      joinColumns = @JoinColumn(name = "university_welfare_id"))
  @Column(name = "speciality")
  private List<SpecialityEnum> disbaleTurnsBySpeciality = new ArrayList<SpecialityEnum>();

  public UniversityWelfare(int id) {
    this.id = id;
  }

  public void disableTurns(SpecialityEnum speciality) {
    disbaleTurnsBySpeciality.add(speciality);
  }

  public void enableTurns(SpecialityEnum speciality) {
    disbaleTurnsBySpeciality.remove(speciality);
  }
}
