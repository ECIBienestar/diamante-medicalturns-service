package eci.cvds.ecibeneficio.diamante_medicalturns_service.service.impl;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request.CreateTurnRequest;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.dto.request.CreateUserRequest;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.exception.MedicalTurnsException;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.Doctor;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.Turn;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.model.User;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.repository.TurnRepository;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.service.SpecialitySequenceService;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.service.TurnService;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.service.UserService;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.SpecialityEnum;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.StatusEnum;
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
  public Turn createTurn(CreateTurnRequest turn) {
    var timeRange = getTodayTimeRange();
    User user = ensureUserExists(turn.getUser());

    if (turnRepository
        .findUserCurrrentTurn(timeRange.start, timeRange.end, user.getId())
        .isPresent()) {
      throw new MedicalTurnsException(MedicalTurnsException.USER_HAVE_TURN);
    }

    Turn newTurn =
        new Turn(user, createCode(turn.getSpeciality()), turn.getSpeciality(), LocalDateTime.now());
    if (turn.getPriority() != null) newTurn.setPriority(turn.getPriority());

    return turnRepository.save(newTurn);
  }

  @Override
  public List<Turn> getTurns() {
    var timeRange = getTodayTimeRange();

    return turnRepository.findTurnsForToday(timeRange.start, timeRange.end, null);
  }

  @Override
  public List<Turn> getTurns(SpecialityEnum speciality) {
    var timeRange = getTodayTimeRange();

    return turnRepository.findTurnsForToday(timeRange.start, timeRange.end, speciality);
  }

  @Override
  public Turn getTurn(Long id) {
    return turnRepository
        .findById(id)
        .orElseThrow(() -> new MedicalTurnsException(MedicalTurnsException.TURN_NOT_FOUND));
  }

  @Override
  public Optional<Turn> getCurrentTurn(SpecialityEnum speciality) {
    var timeRange = getTodayTimeRange();

    return turnRepository.findCurrentTurn(timeRange.start, timeRange.end, speciality); //
  }

  @Override
  public Optional<Turn> getLastTurn(SpecialityEnum speciality) {
    var timeRange = getTodayTimeRange();
    return turnRepository.findTurnsForToday(timeRange.start, timeRange.end, speciality).stream()
        .findFirst();
  }

  @Override
  public void updateStatus(Long id, StatusEnum status) {
    Turn turn = getTurn(id);
    turn.setStatus(status);
    turnRepository.save(turn);
  }

  @Override
  public void updateLevelAttention(Long id, int levelAttention) {
    Turn turn = getTurn(id);
    turn.setLevelAttention(levelAttention);
    turnRepository.save(turn);
  }

  @Override
  public void updateDoctor(Long turnId, Doctor doctor) {
    Turn turn = getTurn(turnId);
    turn.setDoctor(doctor);
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
}
