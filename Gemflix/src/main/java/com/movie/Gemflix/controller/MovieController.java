package com.movie.Gemflix.controller;

import com.movie.Gemflix.common.CommonResponse;
import com.movie.Gemflix.common.Constant;
import com.movie.Gemflix.common.ErrorType;
import com.movie.Gemflix.dto.movie.*;
import com.movie.Gemflix.entity.Filmography;
import com.movie.Gemflix.security.util.JwtUtil;
import com.movie.Gemflix.service.CommonService;
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
    private final CommonService commonService;


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
                                .message("Success")
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
    public ResponseEntity<?> reviewRegister(@RequestBody ReviewRegisterDto reviewDto, HttpServletRequest request){
        log.info("method :{}","reviewRegister");
        log.info("parameter :{}",reviewDto);
        try {
            CommonResponse response = movieService.reviewRegister(reviewDto, request);
            if(response!= null){
                return CommonResponse.createResponse(response, HttpStatus.BAD_REQUEST);
            }
            return CommonResponse.createResponse(
                    CommonResponse.builder()
                            .code(Constant.Success.SUCCESS_CODE)
                            .message("Success")
                            .build(),HttpStatus.OK
            );

        }catch (Exception e){
            e.printStackTrace();
            return CommonResponse.createResponse(
                    CommonResponse.builder()
                            .code(ErrorType.ETC_FAIL.getErrorCode())
                            .message(ErrorType.ETC_FAIL.getErrorMessage())
                            .build(), HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

    }

    @GetMapping("/reviews")
    public ResponseEntity<?> findReviewList(@RequestParam("mvId") Long mvId, Pageable pageable){
        log.info("method :{} ","findReviewList");
        log.info("parameter :{},{}",mvId,pageable);
        try {

            Page<ReviewListDto> reviewListDto = movieService.findReviewList(mvId,pageable);
            log.info("Result : {}",reviewListDto);
            return CommonResponse.createResponse(
                    CommonResponse.builder()
                            .code(Constant.Success.SUCCESS_CODE)
                            .message("Success")
                            .data(reviewListDto)
                            .build(),HttpStatus.OK
            );
        }catch (Exception e){
            log.info("findReviewList Error ");
            e.printStackTrace();
            return CommonResponse.createResponse(
                    CommonResponse.builder()
                            .code(ErrorType.ETC_FAIL.getErrorCode())
                            .message(ErrorType.ETC_FAIL.getErrorMessage())
                            .build(), HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

    }

    @PutMapping("/review")
    public ResponseEntity<?> reviewModify(@RequestBody ReviewRegisterDto reviewDto, HttpServletRequest request){
        log.info("method :{}","reviewModify");
        log.info("parameter :{}",reviewDto);
        try {
            CommonResponse response = movieService.reviewModify(reviewDto, request);
            if(response!= null){
                return CommonResponse.createResponse(response, HttpStatus.BAD_REQUEST);
            }
            return CommonResponse.createResponse(
                    CommonResponse.builder()
                            .code(Constant.Success.SUCCESS_CODE)
                            .message("Success")
                            .build(),HttpStatus.OK
            );

        }catch (Exception e){
            e.printStackTrace();
            return CommonResponse.createResponse(
                    CommonResponse.builder()
                            .code(ErrorType.ETC_FAIL.getErrorCode())
                            .message(ErrorType.ETC_FAIL.getErrorMessage())
                            .build(), HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

    }
    @DeleteMapping("/review/{rvId}")
    public ResponseEntity<?> reviewDelete(@PathVariable("rvId") Long rvId, HttpServletRequest request){
        log.info("method :{}","reviewDelete");
        log.info("parameter :{}",rvId);
        try {
            CommonResponse response = movieService.reviewDelete(rvId, request);
            if(response!= null){
                return CommonResponse.createResponse(response, HttpStatus.BAD_REQUEST);
            }
            return CommonResponse.createResponse(
                    CommonResponse.builder()
                            .code(Constant.Success.SUCCESS_CODE)
                            .message("Success")
                            .build(),HttpStatus.OK
            );

        }catch (Exception e){
            e.printStackTrace();
            return CommonResponse.createResponse(
                    CommonResponse.builder()
                            .code(ErrorType.ETC_FAIL.getErrorCode())
                            .message(ErrorType.ETC_FAIL.getErrorMessage())
                            .build(), HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

    }
    @GetMapping("/filmographys/{peId}")
    public ResponseEntity<?> findFilmographyList(@PathVariable("peId") Long peId){
        log.info("method :{} ","findFilmographyList");
        log.info("parameter :{}",peId);
        try {

            List<FilmographyList> filmographyList = movieService.findFilmographyList(peId);
            log.info("Result : {}",filmographyList);
            return CommonResponse.createResponse(
                    CommonResponse.builder()
                            .code(Constant.Success.SUCCESS_CODE)
                            .message("Success")
                            .data(filmographyList)
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

}
