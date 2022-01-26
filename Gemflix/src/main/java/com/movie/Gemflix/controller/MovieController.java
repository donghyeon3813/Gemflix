package com.movie.Gemflix.controller;

import com.movie.Gemflix.service.MovieService;
import com.movie.Gemflix.service.scheduler.MovieUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movie")
public class MovieController {

    private final MovieService movieService;

    private final MovieUpdateService movieUpdateService;

    @PostMapping("/")
    public void findMovieList(){

    }
}
