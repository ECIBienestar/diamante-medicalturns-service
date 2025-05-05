package eci.cvds.ecibeneficio.diamante_medicalturns_service.model;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.PriorityEnum;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.SpecialityEnum;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.StatusEnum;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "turn")
@Data
@NoArgsConstructor
public class Turn {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "doctor_id", referencedColumnName = "id")
  private Doctor doctor;

  private String code;
  private SpecialityEnum speciality;
  private PriorityEnum priority;
  private LocalDateTime date;
  private StatusEnum status = StatusEnum.PENDING;
  private int levelAttention;

  public Turn(User user, String code, SpecialityEnum speciality, LocalDateTime date) {
    this.user = user;
    this.code = code;
    this.speciality = speciality;
    this.date = date;
  }
}
