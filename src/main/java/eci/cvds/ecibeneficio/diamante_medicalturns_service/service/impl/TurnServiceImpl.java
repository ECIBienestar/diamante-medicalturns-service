package eci.cvds.ecibeneficio.diamante_medicalturns_service.service.impl;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request.CreateTurnRequest;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request.CreateUserRequest;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.response.TurnResponse;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.exception.MedicalTurnsException;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.Turn;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.User;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.repository.TurnRepository;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.service.SpecialitySequenceService;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.service.TurnService;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.service.UserService;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.SpecialityEnum;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.StatusEnum;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.mapper.TurnMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TurnServiceImpl implements TurnService {
  private final TurnRepository turnRepository;
  private final UserService userService;
  private final SpecialitySequenceService specialitySequenceService;

  @Override
  public TurnResponse createTurn(CreateTurnRequest turn) {
    User user = ensureUserExists(turn.getUser());

    Turn newTurn =
        new Turn(user, createCode(turn.getSpeciality()), turn.getSpeciality(), LocalDateTime.now());
    if (turn.getPriority() != null) newTurn.setPriority(turn.getPriority());

    turnRepository.save(newTurn);

    return TurnMapper.toResponse(newTurn);
  }

  @Override
  public List<TurnResponse> getTurns() {
    var timeRange = getTodayTimeRange();
    List<Turn> turns = turnRepository.findTurnsForToday(timeRange.start, timeRange.end, null);

    return mapToResponse(turns);
  }

  @Override
  public List<TurnResponse> getTurns(SpecialityEnum speciality) {
    var timeRange = getTodayTimeRange();
    List<Turn> turns = turnRepository.findTurnsForToday(timeRange.start, timeRange.end, speciality);

    return mapToResponse(turns);
  }

  @Override
  public TurnResponse getTurn(Long id) {
    return TurnMapper.toResponse(findTurn(id));
  }

  @Override
  public TurnResponse getCurrentTurn(SpecialityEnum speciality) {
    Optional<Turn> turn = turnRepository.findCurrentTurn(StatusEnum.CURRENT, speciality);

    if (turn.isEmpty()) throw new MedicalTurnsException(MedicalTurnsException.TURN_NOT_FOUND);

    return TurnMapper.toResponse(turn.get());
  }

  @Override
  public void setStatus(Long id, StatusEnum status) {
    Turn turn = findTurn(id);
    turn.setStatus(status);
    turnRepository.save(turn);
  }

  @Override
  public void setLevelAttention(Long id, int levelAttention) {
    Turn turn = findTurn(id);
    turn.setLevelAttention(levelAttention);
    turnRepository.save(turn);
  }

  private User ensureUserExists(CreateUserRequest user) {
    return userService
        .getUser(user.getId())
        .orElseGet(
            () -> {
              userService.createUser(user);
              return userService
                  .getUser(user.getId())
                  .orElseThrow(
                      () -> new MedicalTurnsException(MedicalTurnsException.ERROR_CREATING_USER));
            });
  }

  private String createCode(SpecialityEnum speciality) {
    int sequence = specialitySequenceService.getSequence(speciality);
    specialitySequenceService.incrementSequence(speciality);

    return String.format("%s-%d", speciality.name().charAt(0), sequence);
  }

  private record TimeRange(LocalDateTime start, LocalDateTime end) {}

  private TimeRange getTodayTimeRange() {
    LocalDate today = LocalDate.now();

    return new TimeRange(today.atStartOfDay(), today.atTime(23, 59, 59));
  }

  private List<TurnResponse> mapToResponse(List<Turn> turns) {
    return turns.stream().map(TurnMapper::toResponse).toList();
  }

  private Turn findTurn(Long id) {
    return turnRepository
        .findById(id)
        .orElseThrow(() -> new MedicalTurnsException(MedicalTurnsException.TURN_NOT_FOUND));
  }
}
