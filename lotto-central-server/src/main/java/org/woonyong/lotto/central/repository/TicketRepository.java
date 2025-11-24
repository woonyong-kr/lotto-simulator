package org.woonyong.lotto.central.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.woonyong.lotto.central.entity.Ticket;
import org.woonyong.lotto.core.domain.TicketStatus;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

  Optional<Ticket> findByTicketNumber(String ticketNumber);

  List<Ticket> findByRoundId(Long roundId);

  List<Ticket> findByStatus(TicketStatus status);

  List<Ticket> findByRoundIdAndStatus(Long roundId, TicketStatus status);

  long countByRoundId(Long roundId);

  long countByRoundIdAndStatus(Long roundId, TicketStatus status);

  long countByRoundIdAndWinningRank(Long roundId, Integer winningRank);

  @Query("SELECT COUNT(t) FROM Ticket t WHERE t.winningRank = 1")
  long countFirstPrizeWinners();
}
