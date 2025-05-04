package eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.RoleEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "role",
    visible = true)
@JsonSubTypes({
  @JsonSubTypes.Type(value = CreateDoctorRequest.class, name = "DOCTOR"),
  @JsonSubTypes.Type(value = CreateUserRequest.class, name = "ESTUDIANTE"),
  @JsonSubTypes.Type(value = CreateUserRequest.class, name = "SECRETARIA")
})
@Getter
@Setter
@NoArgsConstructor
public class CreateUserRequest {
  private String id;
  private String name;
  private RoleEnum role;
}
