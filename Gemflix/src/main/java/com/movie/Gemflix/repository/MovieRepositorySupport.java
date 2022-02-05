package com.movie.Gemflix.repository;


import com.movie.Gemflix.dto.movie.MovieListDto;
import com.movie.Gemflix.dto.movie.MovieSearchDto;
import com.movie.Gemflix.entity.QMovie;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class MovieRepositorySupport {

    private final JPAQueryFactory query;
    private QMovie movie = QMovie.movie;

    public Page<MovieListDto> findMovieList(MovieSearchDto movieSearchDto, Pageable pageable){
        List<MovieListDto> movieList =  query
                .select(Projections.fields(MovieListDto.class,
                    movie.mvId.as("mvId"),
                    movie.imgUrl.as("imgUrl"),
                    movie.title.as("title"),
                    movie.openDt.as("openDt")
                ))
                .from(movie)
                .where(eqTitle(movieSearchDto.getTitle()))
                .orderBy(movie.mvId.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        long movieTotal = query.select(movie.count())
                .from(movie)
                .where(eqTitle(movieSearchDto.getTitle()))
                .fetchOne();
        return PageableExecutionUtils.getPage(movieList,pageable, ()-> movieTotal );

    }

    // 동적쿼리 제목
     BooleanExpression eqTitle(String title){
        if(title == null && title.trim().equals("")){
            return null;
        }

         return movie.title.contains(title);
    }

}
