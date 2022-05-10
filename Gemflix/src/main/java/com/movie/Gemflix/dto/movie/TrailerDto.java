package com.movie.Gemflix.dto.movie;

import com.movie.Gemflix.entity.Movie;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrailerDto {
    private Long trId;
    private MovieDto movie;
    private String trLocation;
    private String imgLocation;
}
