package org.woonyong.lotto.central.repository;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.woonyong.lotto.central.entity.Pos;

@Repository
public interface PosRepository extends JpaRepository<Pos, Long> {

  Optional<Pos> findByPosUid(String posUid);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT p FROM Pos p WHERE p.posUid = :posUid")
  Optional<Pos> findByPosUidForUpdate(@Param("posUid") String posUid);

  @Query("SELECT COALESCE(MAX(p.id), 0) FROM Pos p")
  Long findMaxId();

  List<Pos> findByActiveTrue();

  long countByActiveTrue();
}
