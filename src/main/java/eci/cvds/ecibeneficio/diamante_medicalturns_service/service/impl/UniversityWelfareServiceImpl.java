package eci.cvds.ecibeneficio.diamante_medicalturns_service.service.impl;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request.CreateTurnRequest;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.response.TurnResponse;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.exception.MedicalTurnsException;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.Doctor;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.Turn;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.UniversityWelfare;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.repository.UniversityWelfareRepository;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.service.TurnService;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.service.UniversityWelfareService;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.service.UserService;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.SpecialityEnum;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.StatusEnum;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.mapper.TurnMapper;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UniversityWelfareServiceImpl implements UniversityWelfareService {
  private final UniversityWelfareRepository universityWelfareRepository;
  private final TurnService turnService;
  private final UserService userService;

  @Override
  public TurnResponse addTurn(CreateTurnRequest turn) {
    if (universityWelfareRepository.getUniversityWelfare().isDisableTurns()) {
      throw new MedicalTurnsException(MedicalTurnsException.TURNS_DISABLED);
    }

    if (turnsAreDisabled(turn.getSpeciality())) {
      throw new MedicalTurnsException(MedicalTurnsException.TURNS_DISABLED);
    }

    return TurnMapper.toResponse(turnService.createTurn(turn));
  }

  @Override
  public List<TurnResponse> getTurns() {
    return mapToResponse(turnService.getTurns());
  }

  @Override
  public List<TurnResponse> getTurns(SpecialityEnum speciality) {
    return mapToResponse(turnService.getTurns(speciality));
  }

  @Override
  public Optional<TurnResponse> getCurrentTurn() {
    return Optional.ofNullable(universityWelfareRepository.getUniversityWelfare().getLastTurn())
        .map(TurnMapper::toResponse);
  }

  @Override
  public Optional<TurnResponse> getCurrentTurn(SpecialityEnum speciality) {
    return turnService.getCurrentTurn(speciality).map(TurnMapper::toResponse);
  }

  @Override
  public TurnResponse callNextTurn(String doctorId, SpecialityEnum speciality, int levelAttention) {
    Doctor doctor = getDoctor(doctorId);

    turnService.finishTurn(speciality, levelAttention, doctor);

    Turn nextTurn =
        turnService
            .getLastTurn(speciality)
            .orElseThrow(() -> new MedicalTurnsException(MedicalTurnsException.LAST_TURN));

    startTurn(nextTurn);
    return TurnMapper.toResponse(nextTurn);
  }

  @Override
  public TurnResponse callNextTurn(
      String doctorId, Long nextTurnId, SpecialityEnum speciality, int levelAttention) {
    Doctor doctor = getDoctor(doctorId);

    turnService.finishTurn(speciality, levelAttention, doctor);

    Turn nextTurn = turnService.getTurn(nextTurnId);

    if (!nextTurn.getStatus().equals(StatusEnum.PENDING))
      throw new MedicalTurnsException(MedicalTurnsException.TURN_COMPLETED);

    startTurn(nextTurn);

    return TurnMapper.toResponse(nextTurn);
  }

  @Override
  public void disableTurns() {
    UniversityWelfare universityWelfare = universityWelfareRepository.getUniversityWelfare();
    universityWelfare.setDisableTurns(true);
    universityWelfareRepository.save(universityWelfare);
  }

  @Override
  public void enableTurns() {
    UniversityWelfare universityWelfare = universityWelfareRepository.getUniversityWelfare();
    universityWelfare.setDisableTurns(false);
    universityWelfareRepository.save(universityWelfare);
  }

  @Override
  public void disableTurns(SpecialityEnum speciality) {
    if (universityWelfareRepository.getUniversityWelfare().isDisableTurns()) {
      throw new MedicalTurnsException(MedicalTurnsException.TURNS_ALREADY_DISABLED);
    }

    UniversityWelfare universityWelfare = universityWelfareRepository.getUniversityWelfare();
    universityWelfare.disableTurns(speciality);
    universityWelfareRepository.save(universityWelfare);
  }

  @Override
  public void enableTurns(SpecialityEnum speciality) {
    if (universityWelfareRepository.getUniversityWelfare().isDisableTurns()) {
      throw new MedicalTurnsException(MedicalTurnsException.TURNS_ALREADY_DISABLED);
    }

    UniversityWelfare universityWelfare = universityWelfareRepository.getUniversityWelfare();
    universityWelfare.enableTurns(speciality);
    universityWelfareRepository.save(universityWelfare);
  }

  private boolean turnsAreDisabled(SpecialityEnum speciality) {
    return universityWelfareRepository
        .getUniversityWelfare()
        .getDisbaleTurnsBySpeciality()
        .contains(speciality);
  }

  private List<TurnResponse> mapToResponse(List<Turn> turns) {
    return turns.stream().map(TurnMapper::toResponse).toList();
  }

  private Doctor getDoctor(String id) {
    return (Doctor)
        userService
            .getUser(id)
            .orElseThrow(() -> new MedicalTurnsException(MedicalTurnsException.USER_NOT_FOUND));
  }

  private void startTurn(Turn turn) {
    turnService.startTurn(turn);

    UniversityWelfare universityWelfare = universityWelfareRepository.getUniversityWelfare();
    universityWelfare.setLastTurn(turn);
    universityWelfareRepository.save(universityWelfare);
  }
}
