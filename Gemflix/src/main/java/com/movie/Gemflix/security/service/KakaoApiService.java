package com.movie.Gemflix.security.service;

import com.alibaba.fastjson.JSONObject;
import com.movie.Gemflix.dto.member.KakaoProfileDto;
import com.movie.Gemflix.dto.member.OAuthToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoApiService {

    private final WebClient webClient;

    @Value("${kakao.api.key}")
    private String kakaoKey;

    public OAuthToken tokenRequest(String code) {

        //HttpBody
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoKey); //clientId 는 프로퍼티에 정의해놨음
        body.add("redirect_uri", "http://localhost:4200/auth/callback/kakao");
        body.add("code", code);

        Mono<OAuthToken> responseMono = webClient.post()
                .uri("https://kauth.kakao.com/oauth/token")
                .bodyValue(body)
                .headers(headers -> headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(RuntimeException::new))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(RuntimeException::new))
                .bodyToMono(OAuthToken.class)
                ;

        return responseMono.share().block();
    }

    public KakaoProfileDto userInfoRequest(OAuthToken oAuthToken) {
        String oauthToken = oAuthToken.getAccess_token();

        //유저정보 요청
        Mono<KakaoProfileDto> responseMono = webClient.post()
                .uri("https://kapi.kakao.com/v2/user/me")
                .headers(headers -> {
                    headers.setBearerAuth(oauthToken);
                    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                })
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(RuntimeException::new))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(RuntimeException::new))
                .bodyToMono(KakaoProfileDto.class)
                ;

        return responseMono.share().block();
    }
}