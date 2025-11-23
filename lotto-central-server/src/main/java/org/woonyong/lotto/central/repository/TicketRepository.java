package org.woonyong.lotto.central.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.woonyong.lotto.central.entity.Ticket;
import org.woonyong.lotto.core.domain.TicketStatus;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Optional<Ticket> findByTicketNumber(String ticketNumber);

    List<Ticket> findByRoundId(Long roundId);

    List<Ticket> findByStatus(TicketStatus status);

    List<Ticket> findByRoundIdAndStatus(Long roundId, TicketStatus status);

    long countByRoundId(Long roundId);
}