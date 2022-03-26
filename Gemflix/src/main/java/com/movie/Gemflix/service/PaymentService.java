package com.movie.Gemflix.service;

import com.alibaba.fastjson.JSONObject;
import com.movie.Gemflix.common.CommonResponse;
import com.movie.Gemflix.common.ErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    @Value("${iamport.api.key}")
    private String key;

    @Value("${iamport.api.secret}")
    private String secretKey;

    private final WebClient webClient;

    public Boolean completePayment(JSONObject body) {
        JSONObject params = body.getJSONObject("params");
        String impUid = params.getString("imp_uid");
        String marchantUid = params.getString("merchant_uid");
        int requestPrice = params.getIntValue("price");

        // 액세스 토큰(access token) 발급 받기
        String accessToken = getIamportToken();

        // imp_uid로 아임포트 서버에서 결제 정보 조회
        JSONObject paymentData = getPaymentData(impUid, accessToken);
        int paidPrice = paymentData.getIntValue("amount");
        String status = paymentData.getString("status");

        // 결제금액의 위변조 여부를 검증 (결제 되어야 하는 금액 == 실제 결제된 금액)
        if(requestPrice == paidPrice){
            // 결제 정보를 데이터베이스에 저장
            savePaymentData(paymentData);

            if("paid".equals(status)){ // 결제 완료 (가상계좌는 구현하지 않음)
                return true;
            }else{
                return false;
            }
        }else{
            // 위조된 결제 시도
            return false;
        }
    }

    private void savePaymentData(JSONObject paymentData) {
    }

    public String getIamportToken(){

        Map<String, String> body = new HashMap<>();
        body.put("imp_key", key);
        body.put("imp_secret", secretKey);

        Mono<JSONObject> responseMono = webClient.post()
                .uri(uriBuilder -> uriBuilder.path("https://api.iamport.kr/users/getToken").build())
                .bodyValue(body)
                .headers(headers -> headers.setContentType(MediaType.APPLICATION_JSON))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(RuntimeException::new))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(RuntimeException::new))
                .bodyToMono(JSONObject.class);

        log.info("responseMono: {}", responseMono);
        JSONObject response = responseMono.share().block();
        String accessToken = response.getJSONObject("response").getString("access_token");
        log.info("accessToken: {}", accessToken);
        return accessToken;
    }

    public JSONObject getPaymentData(String impUid, String accessToken) {

        Mono<JSONObject> paymentDataMono = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("https://api.iamport.kr/payments/" + impUid).build())
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(RuntimeException::new))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(RuntimeException::new))
                .bodyToMono(JSONObject.class);

        JSONObject response = paymentDataMono.share().block();
        JSONObject paymentData = response.getJSONObject("response");
        log.info("paymentData: {}", paymentData);
        return paymentData;
    }


}