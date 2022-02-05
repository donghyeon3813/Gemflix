package com.movie.Gemflix.controller;

import com.movie.Gemflix.dto.movie.MovieListDto;
import com.movie.Gemflix.dto.movie.MovieSearchDto;
import com.movie.Gemflix.service.MovieService;
import com.movie.Gemflix.service.scheduler.MovieUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/movie")
@CrossOrigin("*")
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/list")
    public ResponseEntity<?> findMovieList(MovieSearchDto movieSearchDto, Pageable pageable){
        log.info("method :{}","findMovieList");
        Page<MovieListDto> movieListDtos = movieService.findMovieList(movieSearchDto,pageable);
        return ResponseEntity.ok(movieListDtos);
    }
}
