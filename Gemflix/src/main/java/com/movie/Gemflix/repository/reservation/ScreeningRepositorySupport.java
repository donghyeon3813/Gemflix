package com.movie.Gemflix.repository.reservation;

import com.movie.Gemflix.dto.reservation.ScreenInfoDto;
import com.movie.Gemflix.dto.reservation.ScreeningListDto;
import com.movie.Gemflix.dto.reservation.SeatListDto;
import com.movie.Gemflix.entity.*;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ScreeningRepositorySupport {
    private final JPAQueryFactory query;
    private QScreening screening = QScreening.screening;
    private QTicket ticket = QTicket.ticket;
    private QSeat seat = QSeat.seat;

    public List<ScreeningListDto> findScreenList(LocalDateTime startDay, LocalDateTime endDay, LocalDateTime now, Long thId, Long mvId) {

        return query.select(Projections.fields(ScreeningListDto.class,
                screening.siId,
                screening.startTime,
                screening.endTime,
                screening.movie.mvId,
                screening.movie.title.as("mvTitle"),
                screening.type,
                screening.theaterRoom.name.as("trName"),
                screening.theaterRoom.seatCnt,
                ExpressionUtils.as(JPAExpressions
                        .select(ticket.tkId.count().intValue())
                        .from(ticket)
                        .where(ticket.screening.siId.eq(screening.siId)),"spareSeatCnt")
                ))
                .from(screening)
                .where(
                        screening.theaterRoom.theater.thId.eq(thId),
                        screening.startTime.between(startDay,endDay),
                        screening.endTime.after(now),
                        eqMvId(mvId)
                )
                .orderBy(screening.movie.title.asc(),screening.startTime.asc())
                .fetch();
    }
    public ScreenInfoDto findScreenInfo(Long siId) {
        ScreenInfoDto screenInfoDto = query
                .select(Projections.fields(ScreenInfoDto.class,
                        screening.siId,
                        screening.startTime,
                        screening.endTime,
                        screening.theaterRoom.seatCnt,
                        ExpressionUtils.as(JPAExpressions
                                .select(ticket.tkId.count().intValue())
                                .from(ticket)
                                .where(ticket.screening.siId.eq(screening.siId)),"spareSeatCnt"),
                        screening.theaterRoom.name.as("trName"),
                        screening.movie.rating,
                        screening.theaterRoom.roomId,
                        screening.type
                        ))
                .from(screening)
                .where(screening.siId.eq(siId))
                .fetchOne();
        List<SeatListDto> seatList = query.select(Projections.fields(SeatListDto.class,
                seat.seId,
                seat.seCol,
                seat.seRow,
                ExpressionUtils.as(JPAExpressions
                        .select(ticket.tkId)
                        .from(ticket)
                        .where(ticket.seat.seId.eq(seat.seId),
                                ticket.screening.siId.eq(siId)),"tkId")
                        ))
                .from(seat)
                .where(seat.theaterRoom.roomId.eq(screenInfoDto.getRoomId()))
                .fetch();
        screenInfoDto.setSeatList(seatList);
        return screenInfoDto;
    }

    // 동적쿼리 제목
    BooleanExpression eqMvId(Long mvId){
        if(mvId == null || screening.movie.mvId.equals("")){
            return null;
        }
        return screening.movie.mvId.eq(mvId);
    }


}
