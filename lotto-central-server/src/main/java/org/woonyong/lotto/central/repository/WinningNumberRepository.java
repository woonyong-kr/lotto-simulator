package org.woonyong.lotto.central.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.woonyong.lotto.central.entity.WinningNumber;

import java.util.Optional;

@Repository
public interface WinningNumberRepository extends JpaRepository<WinningNumber, Long> {

    Optional<WinningNumber> findByRoundId(Long roundId);
}