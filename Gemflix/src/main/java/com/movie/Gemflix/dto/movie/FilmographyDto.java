package com.movie.Gemflix.dto.movie;

import com.movie.Gemflix.entity.Movie;
import com.movie.Gemflix.entity.People;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilmographyDto {
    private Long pgId;
    private MovieDto movie;
    private PeopleDto people;
}
