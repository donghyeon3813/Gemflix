package com.movie.Gemflix.controller;

import com.alibaba.fastjson.JSONObject;
import com.movie.Gemflix.common.CommonResponse;
import com.movie.Gemflix.common.Constant;
import com.movie.Gemflix.common.ErrorType;
import com.movie.Gemflix.dto.member.MemberDto;
import com.movie.Gemflix.entity.MemberRole;
import com.movie.Gemflix.security.service.AuthService;
import com.movie.Gemflix.security.service.EmailService;
import com.movie.Gemflix.security.util.CookieUtil;
import com.movie.Gemflix.security.util.JwtUtil;
import com.movie.Gemflix.security.model.JwtRequest;
import com.movie.Gemflix.security.model.JwtResponse;
import com.movie.Gemflix.security.service.UserDetailsServiceImpl;
import com.movie.Gemflix.security.util.RedisUtil;
import com.movie.Gemflix.service.CommonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class JwtAuthenticationController {

    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final RedisUtil redisUtil;

    private final UserDetailsServiceImpl userDetailsService;
    private final CommonService commonService;
    private final AuthService authService;
    private final EmailService emailService;

    private final PasswordEncoder passwordEncoder;

    //회원가입 & 인증메일 발송
    @PostMapping("/register")
    public ResponseEntity<?> registerMember(@RequestBody @Valid MemberDto memberDTO,
                                            BindingResult bindingResult){

        try {
            log.info("[registerMember] memberDTO: {}", memberDTO);
            CommonResponse response = commonService.checkError(bindingResult);
            if (response != null){
                return CommonResponse.createResponse(response, HttpStatus.BAD_REQUEST);
            }

            //회원 등록
            response = authService.registerMember(memberDTO);
            if(response != null){
                return CommonResponse.createResponse(response, HttpStatus.OK);
            }

            return CommonResponse.createResponse(CommonResponse.builder()
                    .code(Constant.Success.SUCCESS_CODE)
                    .message("Member Register Success")
                    .build(), HttpStatus.OK);
        }catch (Exception e){
            log.error("registerMember Exception!!");
            e.printStackTrace();
            return CommonResponse.createResponse(CommonResponse.builder()
                    .code(ErrorType.ETC_FAIL.getErrorCode())
                    .message(ErrorType.ETC_FAIL.getErrorMessage())
                    .build(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/email/{memberId}")
    public ResponseEntity<?> certifyEmail(@RequestBody JSONObject requestBody,
                                          @PathVariable String memberId) {
        log.info("[certifyEmail] requestBody:{}, memberId: {}", requestBody, memberId);
        String email = requestBody.getString("email");
        try {
            if (emailService.sendVerificationMail(memberId, email)) {
                return CommonResponse.createResponse(CommonResponse.builder()
                        .code(Constant.Success.SUCCESS_CODE)
                        .message("Member Email Send Success")
                        .build(), HttpStatus.OK);
            } else {
                return CommonResponse.createResponse(CommonResponse.builder()
                        .code(ErrorType.ETC_FAIL.getErrorCode())
                        .message(ErrorType.ETC_FAIL.getErrorMessage())
                        .build(), HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
        log.error("certifyEmail Exception!!");
        e.printStackTrace();
        return CommonResponse.createResponse(CommonResponse.builder()
                .code(ErrorType.ETC_FAIL.getErrorCode())
                .message(ErrorType.ETC_FAIL.getErrorMessage())
                .build(), HttpStatus.BAD_REQUEST);
        }
    }

    //인증메일 확인
    @GetMapping("/verify/{key}")
    public ResponseEntity<?> getEmailVerify(@PathVariable String key) {
        log.info("[getEmailVerify] key:{}", key);
        try {
            CommonResponse response = authService.verifyEmail(key);
            if(response != null){
                return CommonResponse.createResponse(response, HttpStatus.BAD_REQUEST);
            }
            return CommonResponse.createResponse(CommonResponse.builder()
                    .code(Constant.Success.SUCCESS_CODE)
                    .message("Member Email Permission Success")
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("registerMember Exception!!");
            e.printStackTrace();
            return CommonResponse.createResponse(CommonResponse.builder()
                    .code(ErrorType.ETC_FAIL.getErrorCode())
                    .message(ErrorType.ETC_FAIL.getErrorMessage())
                    .build(), HttpStatus.BAD_REQUEST);
        }
    }

    //인증 후 accessToken, refreshToken 발급
    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest jwtRequest){
        log.info("[createAuthenticationToken] jwtRequest:{}", jwtRequest);
        try {
            String authority = checkMemberAuth(jwtRequest);
            if(authority == null){
                return CommonResponse.createResponse(CommonResponse.builder()
                        .code(ErrorType.INVALID_MEMBER.getErrorCode())
                        .message(ErrorType.INVALID_MEMBER.getErrorMessage())
                        .build(), HttpStatus.UNAUTHORIZED);
            }

            String username = jwtRequest.getUsername();
            String accessToken = jwtUtil.generateToken(username);
            String refreshToken = jwtUtil.generateRefreshToken(username);

            //Redis에 Refresh Token 저장 후 만료시간 설정
            redisUtil.setStringDataExpire(RedisUtil.PREFIX_REFRESH_TOKEN_KEY + refreshToken,
                    username, JwtUtil.JWT_REFRESH_TOKEN_EXPIRE/1000);

            return CommonResponse.createResponse(CommonResponse.builder()
                    .code(Constant.Success.SUCCESS_CODE)
                    .message("Member Login Success")
                    .data(new JwtResponse(accessToken, refreshToken, username, authority,
                            JwtUtil.JWT_REFRESH_TOKEN_EXPIRE/1000))
                    .build(), HttpStatus.OK);

        }catch (Exception e){
            log.error("Member Login Exception!!");
            e.printStackTrace();
            return CommonResponse.createResponse(CommonResponse.builder()
                    .code(ErrorType.INVALID_MEMBER.getErrorCode())
                    .message(ErrorType.INVALID_MEMBER.getErrorMessage())
                    .build(), HttpStatus.UNAUTHORIZED);
        }
    }

    //refreshToken 으로 accessToken 재발급
    @PostMapping("/refresh")
    public ResponseEntity<?> checkAuthenticationToken(HttpServletRequest request){
        log.info("[checkAuthenticationToken] request:{}", request);
        Cookie refreshTokenCookie = cookieUtil.getCookie(request, JwtUtil.REFRESH_TOKEN_NAME);
        String refreshToken = null;
        String refreshUsername = null;

        if(refreshTokenCookie != null){
            refreshToken  = refreshTokenCookie.getValue();
        }

        //refreshToken 유효성 검사
        if(refreshToken == null){
            return CommonResponse.createResponse(CommonResponse.builder()
                    .code(ErrorType.REFRESH_IS_NULL.getErrorCode())
                    .message(ErrorType.REFRESH_IS_NULL.getErrorMessage())
                    .build(), HttpStatus.BAD_REQUEST);
        }
        refreshUsername = redisUtil.getStringData(RedisUtil.PREFIX_REFRESH_TOKEN_KEY + refreshToken);
        if(refreshUsername == null){ //refreshToken 유효시간 만료
            return CommonResponse.createResponse(CommonResponse.builder()
                    .code(ErrorType.REFRESH_TOKEN_EXPIRED.getErrorCode())
                    .message(ErrorType.REFRESH_TOKEN_EXPIRED.getErrorMessage())
                    .build(), HttpStatus.UNAUTHORIZED);

        }else{
            if(refreshUsername.equals(jwtUtil.getUsernameFromToken(refreshToken))) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(refreshUsername);
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                //getAuthority
                String username = userDetails.getUsername();
                String authority = null;
                Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
                for (GrantedAuthority auth : authorities) {
                    authority = auth.toString().replace("ROLE_","");
                }

                //새로운 accessToken 발급
                String newAccessToken = jwtUtil.generateToken(refreshUsername);
                return CommonResponse.createResponse(CommonResponse.builder()
                        .code(Constant.Success.SUCCESS_CODE)
                        .message("AccessToken Refresh Success")
                        .data(new JwtResponse(newAccessToken, refreshToken, username, authority,
                                JwtUtil.JWT_REFRESH_TOKEN_EXPIRE / 1000))
                        .build(), HttpStatus.OK);
            }else{
                return CommonResponse.createResponse(CommonResponse.builder()
                        .code(ErrorType.INVALID_MEMBER.getErrorCode())
                        .message(ErrorType.INVALID_MEMBER.getErrorMessage())
                        .build(), HttpStatus.UNAUTHORIZED);
            }
        }
    }

    private String checkMemberAuth(JwtRequest jwtRequest) {
        String authority = null;
        String id = jwtRequest.getUsername();
        String password = jwtRequest.getPassword();
        final UserDetails userDetails = userDetailsService.loadUserByUsername(id);
        String realId = userDetails.getUsername();
        String realPassword = userDetails.getPassword();
        boolean isValidMember = id.equals(realId) && passwordEncoder.matches(password, realPassword);

        if(!isValidMember){
            return null; //유효하지않은 회원
        }
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        for (GrantedAuthority auth : authorities) {
            authority = auth.toString().replace("ROLE_","");
        }
        if(String.valueOf(MemberRole.NONE).equals(authority)){ //비회원은 로그인 불가
            return null;
        }else{
            return authority;
        }
    }

}