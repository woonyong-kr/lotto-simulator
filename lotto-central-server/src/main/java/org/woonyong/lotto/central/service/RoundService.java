package org.woonyong.lotto.central.service;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.woonyong.lotto.central.config.RoundSettings;
import org.woonyong.lotto.central.entity.Round;
import org.woonyong.lotto.central.repository.RoundRepository;
import org.woonyong.lotto.core.domain.RoundStatus;

@Service
@Transactional(readOnly = true)
public class RoundService {
  private static final String ERROR_NO_CURRENT_ROUND = "진행 중인 회차가 없습니다";
  private static final String ERROR_DRAWING_NOT_COMPLETED = "집계가 완료되지 않아 시간을 변경할 수 없습니다";

  private final RoundRepository roundRepository;
  private final RoundSettings roundSettings;
  private final DrawingService drawingService;

  public RoundService(
      final RoundRepository roundRepository,
      final RoundSettings roundSettings,
      final DrawingService drawingService) {
    this.roundRepository = roundRepository;
    this.roundSettings = roundSettings;
    this.drawingService = drawingService;
  }

  @Transactional
  public Round startFirstRound() {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime endTime = now.plusSeconds(roundSettings.getOpenDuration());
    Round round = Round.create(1, now, endTime);
    return roundRepository.save(round);
  }

  @Scheduled(fixedRateString = "#{@roundConfig.getCheckInterval()}")
  @Transactional
  public void checkAndTransition() {
    Optional<Round> currentOpt = getCurrentRoundForUpdate();
    if (currentOpt.isEmpty()) {
      return;
    }

    Round current = currentOpt.get();
    LocalDateTime now = LocalDateTime.now();

    if (shouldTransitionToClose(current, now)) {
      transitionToClose(current, now);
      return;
    }

    if (shouldTransitionToOpen(current, now)) {
      transitionToOpen(current, now);
    }
  }

  private Optional<Round> getCurrentRoundForUpdate() {
    Optional<Round> currentOpt = roundRepository.findTopByOrderByIdDesc();
    if (currentOpt.isEmpty()) {
      return Optional.empty();
    }

    Long currentId = currentOpt.get().getId();
    return roundRepository.findByIdForUpdate(currentId);
  }

  @Transactional
  public void updateOpenDuration(final int newDuration) {
    Round current = getCurrentRoundOrThrow();
    roundSettings.setOpenDuration(newDuration);

    if (current.getStatus() != RoundStatus.OPEN) {
      return;
    }

    recalculateOpenEndTime(current, newDuration);
  }

  @Transactional
  public void updateClosedDuration(final int newDuration) {
    Round current = getCurrentRoundOrThrow();
    roundSettings.setClosedDuration(newDuration);

    if (current.getStatus() != RoundStatus.CLOSED) {
      return;
    }

    validateDrawingCompleted(current);
    recalculateClosedEndTime(current, newDuration);
  }

  public Optional<Round> getCurrentRound() {
    return roundRepository.findTopByOrderByIdDesc();
  }

  private Round getCurrentRoundOrThrow() {
    return getCurrentRound().orElseThrow(() -> new IllegalStateException(ERROR_NO_CURRENT_ROUND));
  }

  private boolean shouldTransitionToClose(final Round round, final LocalDateTime now) {
    return round.getStatus() == RoundStatus.OPEN && round.isTimeExpired(now);
  }

  private boolean shouldTransitionToOpen(final Round round, final LocalDateTime now) {
    return round.getStatus() == RoundStatus.CLOSED
        && round.canTransitionToOpen()
        && round.isTimeExpired(now);
  }

  private void transitionToClose(final Round round, final LocalDateTime now) {
    updateClosedEndTime(round, now);
    round.close();
    roundRepository.save(round);
    drawingService.startDrawingAsync(round.getId());
  }

  private void transitionToOpen(final Round round, final LocalDateTime now) {
    Round newRound = createNextRound(round, now);
    roundRepository.save(newRound);
  }

  private void updateClosedEndTime(final Round round, final LocalDateTime now) {
    LocalDateTime closedEndTime = now.plusSeconds(roundSettings.getClosedDuration());
    round.updateEndTime(closedEndTime);
  }

  private Round createNextRound(final Round currentRound, final LocalDateTime now) {
    LocalDateTime openEndTime = now.plusSeconds(roundSettings.getOpenDuration());
    return Round.create(currentRound.getRoundNumber() + 1, now, openEndTime);
  }

  private void recalculateOpenEndTime(final Round round, final int newDuration) {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime newEndTime = round.getStartTime().plusSeconds(newDuration);

    if (isEndTimeExpired(newEndTime, now)) {
      round.updateEndTime(now);
      transitionToClose(round, now);
    } else {
      round.updateEndTime(newEndTime);
      roundRepository.save(round);
    }
  }

  private void recalculateClosedEndTime(final Round round, final int newDuration) {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime newEndTime = round.getStartTime().plusSeconds(newDuration);

    if (isEndTimeExpired(newEndTime, now)) {
      round.updateEndTime(now);
      transitionToOpen(round, now);
    } else {
      round.updateEndTime(newEndTime);
      roundRepository.save(round);
    }
  }

  private boolean isEndTimeExpired(final LocalDateTime endTime, final LocalDateTime now) {
    return endTime.isBefore(now) || endTime.isEqual(now);
  }

  private void validateDrawingCompleted(final Round round) {
    if (!round.getDrawingCompleted()) {
      throw new IllegalStateException(ERROR_DRAWING_NOT_COMPLETED);
    }
  }
}
