package com.movie.Gemflix.security.service;

import com.movie.Gemflix.dto.member.KakaoProfileDto;
import com.movie.Gemflix.dto.member.OAuthToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoApiService {

    @Value("${kakao.api.key}")
    private String kakaoKey;

    public OAuthToken tokenRequest(String code) {
        //POST방식으로 데이터 요청
        //TODO: to webClient
        RestTemplate restTemplate = new RestTemplate();

        //HttpHeader
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //HttpBody
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoKey); //clientId 는 프로퍼티에 정의해놨음
        body.add("redirect_uri", "http://localhost:4200/auth/callback/kakao");
        body.add("code", code);

        //HttpHeader와 HttpBody 담기기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);
        return restTemplate.exchange("https://kauth.kakao.com/oauth/token",
                HttpMethod.POST, kakaoTokenRequest, OAuthToken.class).getBody();
    }

    public KakaoProfileDto userInfoRequest(OAuthToken oAuthToken) {
        ///유저정보 요청
        //TODO: to webClient
        RestTemplate restTemplate = new RestTemplate();

        //HttpHeader
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + oAuthToken.getAccess_token());
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //HttpHeader와 HttpBody 담기기
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers);
        return restTemplate.exchange("https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST, kakaoProfileRequest, KakaoProfileDto.class).getBody();
    }
}