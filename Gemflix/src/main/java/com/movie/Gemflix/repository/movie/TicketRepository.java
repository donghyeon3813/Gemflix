package com.movie.Gemflix.repository.movie;

import com.movie.Gemflix.entity.Member;
import com.movie.Gemflix.entity.Screening;
import com.movie.Gemflix.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket,Long> {
    List<Ticket> findByMemberAndScreening(Member member, Screening screening);
}
