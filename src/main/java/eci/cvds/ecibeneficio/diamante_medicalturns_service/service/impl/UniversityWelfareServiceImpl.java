package eci.cvds.ecibeneficio.diamante_medicalturns_service.service.impl;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request.CreateTurnRequest;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.response.TurnResponse;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.exception.MedicalTurnsException;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.Turn;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.UniversityWelfare;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.repository.UniversityWelfareRepository;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.service.TurnService;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.service.UniversityWelfareService;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.SpecialityEnum;
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
  public Optional<TurnResponse> getCurrentTurn(SpecialityEnum speciality) {
    return turnService.getCurrentTurn(speciality).map(TurnMapper::toResponse);
  }

  @Override
  public Optional<TurnResponse> getLastCurrentTurn() {
    return turnService.getLastCurrentTurn().map(TurnMapper::toResponse);
  }

  @Override
  public TurnResponse callNextTurn(SpecialityEnum speciality, int levelAttention) {
    turnService.finishTurn(speciality, levelAttention);
    Turn nextTurn = turnService.startNextTurn(speciality);

    return TurnMapper.toResponse(nextTurn);
  }

  @Override
  public TurnResponse callNextTurn(Long nextTurnId, SpecialityEnum speciality, int levelAttention) {
    turnService.finishTurn(speciality, levelAttention);
    Turn nextTurn = turnService.startTurn(turnService.getTurn(nextTurnId));

    return TurnMapper.toResponse(nextTurn);
  }

  @Override
  public void skipTurn(SpecialityEnum speciality) {
    turnService.skipTurn(speciality);
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

  @Override
  public boolean areTurnsEnabled() {
    if (universityWelfareRepository.getUniversityWelfare().isDisableTurns()) {
      return false;
    }
    return true;
  }

  @Override
  public List<SpecialityEnum> getSpecialtiesDisabled() {
    UniversityWelfare universityWelfare = universityWelfareRepository.getUniversityWelfare();
    return universityWelfare.getDisbaleTurnsBySpeciality();
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

}
