package com.movie.Gemflix.repository;

import com.movie.Gemflix.entity.QTicket;
import com.movie.Gemflix.entity.Ticket;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TicketRepositorySupport {
    private final JPAQueryFactory query;
    private QTicket ticket = QTicket.ticket;

    public List<Ticket> findByMemberTicket(Long mId, Long mvId){
        return query
                .select(Projections.fields(Ticket.class,
                        ticket.tkId))
                .from(ticket)
                .where(
                        ticket.member.mId.eq(mId),
                        ticket.screening.movie.mvId.eq(mvId),
                        ticket.rvUseState.eq("0")
                        )
                .fetch();
    }

    public Long ticketStateModify(Long rvId){
        return query.update(ticket).where(ticket.tkId.eq(rvId))
                .set(ticket.rvUseState,"1")
                .execute();

    }



}
