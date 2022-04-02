package com.movie.Gemflix.controller;

import com.alibaba.fastjson.JSONObject;
import com.movie.Gemflix.common.CommonResponse;
import com.movie.Gemflix.common.Constant;
import com.movie.Gemflix.common.ErrorType;
import com.movie.Gemflix.dto.movie.FilmographyList;
import com.movie.Gemflix.dto.movie.MovieListDto;
import com.movie.Gemflix.dto.movie.ReviewListDto;
import com.movie.Gemflix.dto.reservation.ScreenSearchDto;
import com.movie.Gemflix.dto.reservation.ScreeningListDto;
import com.movie.Gemflix.entity.Theater;
import com.movie.Gemflix.repository.movie.MovieRepositorySupport;
import com.movie.Gemflix.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final MovieRepositorySupport movieRepositorySupport;

    @GetMapping("/places")
    public ResponseEntity<?> findPlaceList(){
        log.info("method :{} ","findPlaceList");
        try {
            List<String> findPlaceList = reservationService.findPlaceList();
            log.info("Result : {}",findPlaceList);
            return CommonResponse.createResponse(
                    CommonResponse.builder()
                            .code(Constant.Success.SUCCESS_CODE)
                            .message("Success")
                            .data(findPlaceList)
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

    @GetMapping("/theaters")
    public ResponseEntity<?> findTheaterList(@RequestParam("place") String place){
        log.info("method :{} ","findTheaterList");
        try {
            List<Theater> findTheaterList = reservationService.findTheaterList(place);
            log.info("Result : {}",findTheaterList);
            return CommonResponse.createResponse(
                    CommonResponse.builder()
                            .code(Constant.Success.SUCCESS_CODE)
                            .message("Success")
                            .data(findTheaterList)
                            .build(),HttpStatus.OK
            );
        }catch (Exception e){
            log.info("FindTheaterList Error ");
            e.printStackTrace();
            return CommonResponse.createResponse(
                    CommonResponse.builder()
                            .code(ErrorType.ETC_FAIL.getErrorCode())
                            .message(ErrorType.ETC_FAIL.getErrorMessage())
                            .build(), HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/movies")
    public ResponseEntity<?> findShowingMovieList(){
        log.info("method :{} ","findShowingMovieList");
        try {
            List<MovieListDto> showingMovieList = reservationService.findShowingMovieList();
            log.info("Result : {}",showingMovieList);
            return CommonResponse.createResponse(
                    CommonResponse.builder()
                            .code(Constant.Success.SUCCESS_CODE)
                            .message("Success")
                            .data(showingMovieList)
                            .build(),HttpStatus.OK
            );
        }catch (Exception e){
            log.info("FindShowingMovieList Error ");
            e.printStackTrace();
            return CommonResponse.createResponse(
                    CommonResponse.builder()
                            .code(ErrorType.ETC_FAIL.getErrorCode())
                            .message(ErrorType.ETC_FAIL.getErrorMessage())
                            .build(), HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/screens")
    public ResponseEntity<?> findScreenList(@RequestParam(name = "mvId", required = false) Long mvId, @RequestParam("thId") Long thId){
        log.info("method :{} ","findScreenList");
        log.info("parameter :{},{}",mvId,thId);
        try {
            List<ScreeningListDto> screenList = reservationService.findScreenList();
            log.info("Result : {}",screenList);
            return CommonResponse.createResponse(
                    CommonResponse.builder()
                            .code(Constant.Success.SUCCESS_CODE)
                            .message("Success")
                            .data(screenList)
                            .build(),HttpStatus.OK
            );
        }catch (Exception e){
            log.info("FindShowingMovieList Error ");
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
