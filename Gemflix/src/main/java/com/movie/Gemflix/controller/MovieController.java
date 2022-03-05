package com.movie.Gemflix.controller;

import com.movie.Gemflix.common.CommonResponse;
import com.movie.Gemflix.common.Constant;
import com.movie.Gemflix.common.ErrorType;
import com.movie.Gemflix.dto.movie.MovieDetailDto;
import com.movie.Gemflix.dto.movie.MovieListDto;
import com.movie.Gemflix.dto.movie.MovieSearchDto;
import com.movie.Gemflix.security.util.JwtUtil;
import com.movie.Gemflix.service.MovieService;
import com.movie.Gemflix.service.scheduler.MovieUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/movie")
public class MovieController {

    private final MovieService movieService;
    private final JwtUtil jwtUtil;


    @GetMapping("/list")
    public ResponseEntity<?> findMovieList(MovieSearchDto movieSearchDto, Pageable pageable){
        log.info("method :{} parameter :{},{}","findMovieList",movieSearchDto,pageable);
        log.info("parameter :{},{}",movieSearchDto,pageable);
        try {

            Page<MovieListDto> movieListDto = movieService.findMovieList(movieSearchDto,pageable);
            log.info("Result : {}",movieListDto);
            return CommonResponse.createResponse(
                    CommonResponse.builder()
                            .code(Constant.Success.SUCCESS_CODE)
                            .message("Success")
                            .data(movieListDto)
                            .build(),HttpStatus.OK
            );
        }catch (Exception e){
            log.info("FindMovieList Error ");
            e.printStackTrace();
            return CommonResponse.createResponse(
                    CommonResponse.builder()
                            .code(ErrorType.ETC_FAIL.getErrorCode())
                            .message(ErrorType.ETC_FAIL.getErrorMessage())
                            .build(), HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

    }

    @GetMapping("/details")
    public ResponseEntity<?> findMovieDetails(MovieSearchDto movieSearchDto) {
        log.info("method :{}","findMovieDetails");
        log.info("parameter :{}",movieSearchDto);

        try {
            MovieDetailDto movieDetailDto = movieService.findMovieDetails(movieSearchDto);
            log.info("Result : {}",movieDetailDto);

            if(movieDetailDto != null){
                return CommonResponse.createResponse(
                        CommonResponse.builder()
                                .code(Constant.Success.SUCCESS_CODE)
                                .message("성공")
                                .data(movieDetailDto)
                                .build(), HttpStatus.OK
                );
            }else{
                return CommonResponse.createResponse(
                        CommonResponse.builder()
                                .code(ErrorType.MOVIE_DETAIL_NOT_FOUND.getErrorCode())
                                .message(ErrorType.MOVIE_DETAIL_NOT_FOUND.getErrorMessage())
                                .build(), HttpStatus.NOT_FOUND
                );
            }

        } catch (Exception e){
            log.info("FindMovieDetails Error ");
            e.printStackTrace();
            return CommonResponse.createResponse(
                    CommonResponse.builder()
                            .code(ErrorType.ETC_FAIL.getErrorCode())
                            .message(ErrorType.ETC_FAIL.getErrorMessage())
                            .build(), HttpStatus.INTERNAL_SERVER_ERROR
            );
        }


    }

    @PostMapping("/review")
    public ResponseEntity<?> reviewRegister(String msg, HttpServletRequest request){
        try {
            String authHeader = request.getHeader("Authorization");
            String accessToken = authHeader.substring(7);
            log.info("accessToken: {}", accessToken);
            jwtUtil.getUsernameFromToken(accessToken);

        }catch (Exception e){
            return CommonResponse.createResponse(
                    CommonResponse.builder()
                            .code(1201)
                            .message("Fail")
                            .build(),HttpStatus.UNAUTHORIZED
            );
        }


        return CommonResponse.createResponse(
                CommonResponse.builder()
                        .code(Constant.Success.SUCCESS_CODE)
                        .message("Success")
                        .build(),HttpStatus.OK
        );
    }
}
