package com.movie.Gemflix.controller;

import com.alibaba.fastjson.JSONObject;
import com.movie.Gemflix.common.CommonResponse;
import com.movie.Gemflix.common.ErrorType;
import com.movie.Gemflix.dto.payment.PaymentDto;
import com.movie.Gemflix.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    //결제 정보 전달받는 API endpoint
    @PostMapping("/payments/complete")
    public ResponseEntity<?> completePayment(@RequestBody JSONObject body){
        log.info("===== completePayment =====");
        log.info("body: {}", body);

        paymentService.completePayment(body);



        return null;
    }

}