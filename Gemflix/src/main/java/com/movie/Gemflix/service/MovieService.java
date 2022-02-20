package com.movie.Gemflix.service;

import com.movie.Gemflix.dto.movie.MovieDetailDto;
import com.movie.Gemflix.dto.movie.MovieListDto;
import com.movie.Gemflix.dto.movie.MovieSearchDto;
import com.movie.Gemflix.repository.MovieRepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MovieService {

    private final MovieRepositorySupport movieRepositorySupport;

    public Page<MovieListDto> findMovieList(MovieSearchDto movieSearchDto, Pageable pageable) throws Exception {

        return movieRepositorySupport.findMovieList(movieSearchDto, pageable);
    }

    public MovieDetailDto findMovieDetails(MovieSearchDto movieSearchDto) throws Exception {

        return movieRepositorySupport.findMovieDetails(movieSearchDto);

    }
}
