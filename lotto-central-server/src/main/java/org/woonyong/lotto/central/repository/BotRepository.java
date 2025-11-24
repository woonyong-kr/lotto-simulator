package org.woonyong.lotto.central.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.woonyong.lotto.central.entity.Bot;

@Repository
public interface BotRepository extends JpaRepository<Bot, Long> {

  Optional<Bot> findByBotUid(String botUid);

  List<Bot> findByActiveTrue();

  long countByActiveTrue();

  @Query("SELECT COALESCE(MAX(b.id), 0) FROM Bot b")
  Long findMaxId();
}
