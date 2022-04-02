package com.movie.Gemflix.repository.reservation;

import com.movie.Gemflix.entity.QTheater;
import com.movie.Gemflix.entity.Theater;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TheatherRepositorySupport {
    private final JPAQueryFactory query;
    private QTheater theater = QTheater.theater;



    public List<String> findPlaceList()  throws Exception{
        return query.select(theater.place)
                .from(theater)
                .groupBy(theater.place)
                .orderBy(theater.place.asc())
                .fetch();
    }

    public List<Theater> findTheaterList(String place) throws Exception{
        return query.select(Projections.fields(Theater.class,
                theater.thId,
                theater.location))
                .from(theater)
                .where(theater.place.eq(place))
                .orderBy(theater.location.asc())
                .fetch();
    }


}
