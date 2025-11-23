package org.woonyong.lotto.central.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.woonyong.lotto.central.entity.WinningNumber;

import java.util.Optional;

public interface WinningNumberRepository extends JpaRepository<WinningNumber, Long> {

    Optional<WinningNumber> findByRoundId(Long roundId);
}