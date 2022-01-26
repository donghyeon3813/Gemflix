package com.movie.Gemflix.scheduler;


import com.movie.Gemflix.service.scheduler.MovieUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
public class MovieDataScheduler {

    private final MovieUpdateService movieUpdateService;

//    @Scheduled(cron = "0 0 10 * * *") //매일10시 설정
    @Scheduled(fixedDelay = 1000000) // 최초 실행후 주석처리
    private void TheMovieDateUpdate() throws InterruptedException {
        long delayedTime = 5; // 정보를 순서대로 호출하기위한 딜레이시간 설정
        movieUpdateService.theMovieGetGenres();
        TimeUnit.SECONDS.sleep(delayedTime);
        movieUpdateService.theMovieGetMovie();
        TimeUnit.SECONDS.sleep(delayedTime);
        movieUpdateService.theMovieGetPeople();
        TimeUnit.SECONDS.sleep(delayedTime);
        movieUpdateService.theMovieFilmography();
    }
}
