package com.movie.Gemflix.scheduler;


import com.movie.Gemflix.service.scheduler.MovieUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class MovieDataScheduler {

    private final MovieUpdateService movieUpdateService;

//    @Scheduled(cron = "0 0 10 * * *") //매일10시 설정
//    @Scheduled(fixedDelay = 1000000) // 최초 실행후 주석처리
    private void TheMovieDateUpdate() {
        try {
            movieUpdateService.theMovieGetGenres();
            movieUpdateService.theMovieGetMovie();
            movieUpdateService.theMovieGetPeople();
            movieUpdateService.theMovieFilmography();
            movieUpdateService.theMovieVideo();
        }catch (Exception e){
            log.info("Scheduler update Error {}",e);
            e.printStackTrace();
        }

    }
}
