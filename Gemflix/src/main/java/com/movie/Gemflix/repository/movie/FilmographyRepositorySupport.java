package com.movie.Gemflix.repository.movie;

import com.movie.Gemflix.dto.movie.FilmographyList;
import com.movie.Gemflix.entity.QFilmography;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FilmographyRepositorySupport {
    private final JPAQueryFactory query;
    private QFilmography filmography = QFilmography.filmography;
    public List<FilmographyList> findFilmography(Long peId){

        return query
                .select(Projections.fields(FilmographyList.class,
                filmography.pgId,
                filmography.movie.imgUrl,
                filmography.movie.title
                ))
                .from(filmography)
                .where(filmography.people.peId.eq(peId))
                .fetch();
    }

}
