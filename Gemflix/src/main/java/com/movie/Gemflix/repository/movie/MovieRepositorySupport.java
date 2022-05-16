package com.movie.Gemflix.repository.movie;


import com.movie.Gemflix.dto.movie.MovieDetailDto;
import com.movie.Gemflix.dto.movie.MovieListDto;
import com.movie.Gemflix.dto.movie.MovieSearchDto;
import com.movie.Gemflix.entity.*;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
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
    private QGenre genre = QGenre.genre;
    private QTrailer trailer = QTrailer.trailer;
    private QReview review = QReview.review;
    private QTicket ticket = QTicket.ticket;
    private QScreening screening = QScreening.screening;
    private QPeople people = QPeople.people;
    private QFilmography filmography = QFilmography.filmography;



    public Page<MovieListDto> findMovieList(MovieSearchDto movieSearchDto, Pageable pageable) throws Exception{
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
    public MovieDetailDto findMovieDetails(MovieSearchDto movieSearchDto) throws Exception {
        MovieDetailDto movieDetailDto = query
                .select(Projections.fields(MovieDetailDto.class,
                        movie.mvId.as("mvId"),
                        movie.genre.grNm.as("grNm"),
                        movie.title.as("title"),
                        movie.content.as("content"),
                        movie.extent.as("extent"),
                        movie.imgUrl.as("imgUrl"),
                        movie.backImgUrl.as("backImgUrl"),
                        movie.openDt.as("openDt"),
                        ExpressionUtils.as(JPAExpressions.select(review.score.sum().divide(review.rvId.count()))
                                .from(review)
                                .innerJoin(review.ticket, ticket)
                                .innerJoin(ticket.screening, screening)
                                .innerJoin(screening.movie, movie)
                                .where(movie.mvId.eq(movieSearchDto.getMvId()),review.delStatus.eq("0")),"score")
                        ))
                .from(movie)
                .where(movie.mvId.eq(movieSearchDto.getMvId()))
                .fetchOne();

        List<Trailer> trailerList = query
                .select(Projections.fields(Trailer.class,
                        trailer.trLocation.as("trLocation"),
                        trailer.imgLocation.as("imgLocation")))
                .from(trailer)
                .where(trailer.movie.mvId.eq(movieSearchDto.getMvId()))
                .fetch();
        movieDetailDto.setTrailerList(trailerList);

        List<People> peopleList = query
                .select(Projections.fields(People.class,
                        people.name,
                        people.type,
                        people.peId))
                .from(people)
                .innerJoin(filmography).on(people.peId.eq(filmography.people.peId))
                .where(filmography.movie.mvId.eq(movieSearchDto.getMvId()))
                .orderBy(people.type.asc())
                .fetch();
        movieDetailDto.setPeopleList(peopleList);
        return movieDetailDto;
    }

    public List<MovieListDto> findShowingMovieList() throws Exception{
        return query
                .select(Projections.fields(MovieListDto.class,
                        movie.mvId.as("mvId"),
                        movie.title.as("title")
                ))
                .from(movie)
                .where(movie.status.eq("1"))
                .orderBy(movie.title.asc())
                .fetch();
    }

    // 동적쿼리 제목
     BooleanExpression eqTitle(String title){
        if(title == null || title.trim().equals("")){
            return null;
        }
         return movie.title.contains(title);
    }


}
