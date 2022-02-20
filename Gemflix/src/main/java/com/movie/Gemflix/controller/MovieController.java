package com.movie.Gemflix.controller;

import com.movie.Gemflix.common.CommonResponse;
import com.movie.Gemflix.dto.movie.MovieDetailDto;
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
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/list")
    public ResponseEntity<?> findMovieList(MovieSearchDto movieSearchDto, Pageable pageable){
        log.info("method :{} parameter :{},{}","findMovieList",movieSearchDto,pageable);
        log.info("parameter :{},{}",movieSearchDto,pageable);
        try {

            Page<MovieListDto> movieListDtos = movieService.findMovieList(movieSearchDto,pageable);
            log.info("Result : {}",movieListDtos);
            return CommonResponse.createResponse(
                    CommonResponse.builder()
                            .code("100")
                            .message("성공")
                            .data(movieListDtos)
                            .build(),HttpStatus.OK
            );
        }catch (Exception e){
            log.info("FindMovieList Error ");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("등록된 영화정보가 없습니다.");
        }

    }

    @GetMapping("/details")
    public ResponseEntity<?> findMovieDetails(MovieSearchDto movieSearchDto) {
        log.info("method :{}","findMovieDetails");
        log.info("parameter :{}",movieSearchDto);

        try {
            MovieDetailDto movieDetailDto = movieService.findMovieDetails(movieSearchDto);
            log.info("Result : {}",movieDetailDto);
            return CommonResponse.createResponse(
                    CommonResponse.builder()
                    .code("100")
                    .message("성공")
                    .data(movieDetailDto)
                    .build(),HttpStatus.OK
                    );

        } catch (Exception e){
            log.info("FindMovieDetails Error ");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("등록된 영화정보가 없습니다.");
        }

    }
}
