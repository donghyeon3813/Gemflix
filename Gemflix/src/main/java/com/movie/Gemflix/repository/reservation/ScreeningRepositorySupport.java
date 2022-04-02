package com.movie.Gemflix.repository.reservation;

import com.movie.Gemflix.dto.reservation.ScreeningListDto;
import com.movie.Gemflix.entity.QMovie;
import com.movie.Gemflix.entity.QScreening;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ScreeningRepositorySupport {
    private final JPAQueryFactory query;
    private QScreening screening = QScreening.screening;

    public List<ScreeningListDto> findScreenList() {

        return query.select(Projections.fields(ScreeningListDto.class,
                screening.siId,
                screening.startTime,
                screening.endTime,
                screening.movie.mvId,
                screening.movie.title.as("mvTitle"),
                screening.type,
                screening.theaterRoom.name.as("trName"),
                screening.theaterRoom.seatCnt
                ))
                .from(screening)
                .orderBy(screening.movie.title.asc())
                .fetch();
    }
}
