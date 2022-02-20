package com.movie.Gemflix.controller;

import com.alibaba.fastjson.JSONObject;
import com.movie.Gemflix.common.ApiResponseMessage;
import com.movie.Gemflix.dto.KakaoProfile;
import com.movie.Gemflix.dto.OAuthToken;
import com.movie.Gemflix.entity.Member;
import com.movie.Gemflix.entity.MemberRole;
import com.movie.Gemflix.repository.MemberRepository;
import com.movie.Gemflix.security.model.JwtRequest;
import com.movie.Gemflix.security.service.KakaoApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class SocialAuthenticationController {

    private final PasswordEncoder passwordEncoder;
    private final JwtAuthenticationController jwtAuthenticationController;
    private final KakaoApiService kaKaoApiService;
    private final MemberRepository memberRepository;

    @PostMapping("/callback/kakao")
    public ResponseEntity<?> kakaoCallback(@RequestBody JSONObject json) {
        String code = json.getString("code");
        log.info("kakaoCode: {}", code);
        try{
            OAuthToken oAuthToken = kaKaoApiService.tokenRequest(code); //토큰 가져오기
            log.info("oAuthToken: {}", oAuthToken);
            KakaoProfile kakaoProfile = kaKaoApiService.userInfoRequest(oAuthToken); //유저정보 가져오기

            if(kakaoProfile != null){
                String id = kakaoProfile.getId().toString();
                String pw = id + "@";
                Optional<Member> optMember = memberRepository.findById(id);

                if(!optMember.isPresent()){ //최초 카카오 로그인 -> 회원 등록
                    Member member = Member.builder()
                            .id(id)
                            .password(passwordEncoder.encode(pw))
                            .authority(MemberRole.NO_PERMISSION)
                            .fromSocial("K")
                            .build();
                    log.info("member: {}", member);
                    memberRepository.save(member);
                }
                //accessToken, refreshToken 발급
                return jwtAuthenticationController.createAuthenticationToken(new JwtRequest(id, pw));
            }
        }catch (Exception e){
            log.error("kakaoCallback Exception!!");
            e.printStackTrace();
        }
        return null;
    }

}