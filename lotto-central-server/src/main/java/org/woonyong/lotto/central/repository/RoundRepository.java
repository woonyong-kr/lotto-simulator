package org.woonyong.lotto.central.repository;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.woonyong.lotto.central.entity.Round;

public interface RoundRepository extends JpaRepository<Round, Long> {

  Optional<Round> findTopByOrderByIdDesc();

  List<Round> findTop5ByOrderByIdDesc();

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT r FROM Round r WHERE r.id = :id")
  Optional<Round> findByIdForUpdate(@Param("id") Long id);
}
