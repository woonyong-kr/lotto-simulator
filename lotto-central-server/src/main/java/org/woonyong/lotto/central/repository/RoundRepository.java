package org.woonyong.lotto.central.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.woonyong.lotto.central.entity.Round;
import org.woonyong.lotto.core.domain.RoundStatus;

import java.util.List;
import java.util.Optional;

public interface RoundRepository extends JpaRepository<Round, Long> {

    Optional<Round> findTopByOrderByIdDesc();

    Optional<Round> findByRoundNumber(Integer roundNumber);

    Optional<Round> findByStatus(RoundStatus status);

    List<Round> findAllByStatus(RoundStatus status);

    boolean existsByRoundNumber(Integer roundNumber);
}