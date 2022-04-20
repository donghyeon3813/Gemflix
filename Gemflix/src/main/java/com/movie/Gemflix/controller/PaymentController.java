package com.movie.Gemflix.controller;

import com.alibaba.fastjson.JSONObject;
import com.movie.Gemflix.common.CommonResponse;
import com.movie.Gemflix.common.Constant;
import com.movie.Gemflix.common.ErrorType;
import com.movie.Gemflix.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    //결제 정보 전달받는 API endpoint
    @PostMapping("/payments/complete/{memberId}")
    public ResponseEntity<?> completePayment(@RequestBody JSONObject requestBody,
                                             @PathVariable String memberId){
        log.info("===== completePayment =====");
        log.info("requestBody: {}, memberId: {}", requestBody, memberId);

        try{
            boolean isSuccess = paymentService.completePayment(requestBody, memberId);
            if(isSuccess){
                return CommonResponse.createResponse(CommonResponse.builder()
                        .code(Constant.Success.SUCCESS_CODE)
                        .message("Success Payment")
                        .build(), HttpStatus.OK);
            }else{
                return CommonResponse.createResponse(CommonResponse.builder()
                        .code(ErrorType.PAYMENT_FAIL.getErrorCode())
                        .message(ErrorType.PAYMENT_FAIL.getErrorMessage())
                        .build(), HttpStatus.NO_CONTENT);
            }
        }catch (Exception e){
            return CommonResponse.createResponse(CommonResponse.builder()
                    .code(ErrorType.ETC_FAIL.getErrorCode())
                    .message(ErrorType.ETC_FAIL.getErrorMessage())
                    .build(), HttpStatus.BAD_REQUEST);
        }
    }

    //결제 정보 전달받는 API endpoint
    @PostMapping("/payments/save/{memberId}")
    public ResponseEntity<?> savePayment(@RequestBody JSONObject requestBody,
                                                   @PathVariable String memberId){
        log.info("===== savePayment =====");
        log.info("requestBody: {}, memberId: {}", requestBody, memberId);

        try{
            boolean isSuccess = paymentService.savePaymentData(requestBody, memberId);
            if(isSuccess){
                return CommonResponse.createResponse(CommonResponse.builder()
                        .code(Constant.Success.SUCCESS_CODE)
                        .message("Success Save Payment")
                        .build(), HttpStatus.OK);
            }else{
                return CommonResponse.createResponse(CommonResponse.builder()
                        .code(ErrorType.PAYMENT_FAIL.getErrorCode())
                        .message(ErrorType.PAYMENT_FAIL.getErrorMessage())
                        .build(), HttpStatus.NO_CONTENT);
            }
        }catch (Exception e){
            return CommonResponse.createResponse(CommonResponse.builder()
                    .code(ErrorType.ETC_FAIL.getErrorCode())
                    .message(ErrorType.ETC_FAIL.getErrorMessage())
                    .build(), HttpStatus.BAD_REQUEST);
        }
    }

}