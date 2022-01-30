package com.movie.Gemflix.controller;

import com.movie.Gemflix.common.ApiResponseMessage;
import com.movie.Gemflix.common.ErrorType;
import com.movie.Gemflix.dto.MemberDTO;
import com.movie.Gemflix.entity.Member;
import com.movie.Gemflix.entity.MemberRole;
import com.movie.Gemflix.security.model.JwtRequest;
import com.movie.Gemflix.security.model.JwtResponse;
import com.movie.Gemflix.security.service.AuthService;
import com.movie.Gemflix.security.service.EmailService;
import com.movie.Gemflix.security.service.UserDetailsServiceImpl;
import com.movie.Gemflix.security.util.CookieUtil;
import com.movie.Gemflix.security.util.JwtUtil;
import com.movie.Gemflix.security.util.RedisUtil;
import com.movie.Gemflix.service.CommonService;
import com.movie.Gemflix.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/auth")
public class NoneController {

    private final CommonService commonService;
    private final AuthService authService;
    private final UserDetailsServiceImpl userDetailsService;

    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final RedisUtil redisUtil;

    private final PasswordEncoder passwordEncoder;

    //회원가입 & 인증메일 발송
    @PostMapping("/register")
    public ResponseEntity<ApiResponseMessage> registerMember(@RequestBody @Valid MemberDTO memberDTO,
                                                             BindingResult bindingResult){

        try {
            log.info("[registerMember] memberDTO: {}", memberDTO);
            ApiResponseMessage apiRm = commonService.checkError(bindingResult);
            if (apiRm != null) return new ResponseEntity<>(apiRm, HttpStatus.valueOf(apiRm.getStatus()));

            //회원 등록
            apiRm = authService.registerMember(memberDTO);
            return new ResponseEntity<>(apiRm, HttpStatus.valueOf(apiRm.getStatus()));
        }catch (Exception e){
            log.error("registerMember Exception!!");
            e.printStackTrace();
        }
        return null;
    }

    //인증메일 확인
    @GetMapping("/verify/{key}")
    public ResponseEntity<ApiResponseMessage> getEmailVerify(@PathVariable String key) {
        try {
            ApiResponseMessage apiRm = authService.verifyEmail(key);
            return new ResponseEntity<>(apiRm, HttpStatus.valueOf(apiRm.getStatus()));
        } catch (Exception e) {
            log.error("registerMember Exception!!");
            e.printStackTrace();
        }
        return null;
    }

    //인증 후 accessToken, refreshToken 발급
    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest jwtRequest,
                                                       HttpServletResponse response){
        try {
            log.info("username: {}, password: {}", jwtRequest.getUsername(), jwtRequest.getPassword());
            if(!checkMemberAuth(jwtRequest)) {
                ApiResponseMessage apiRm = new ApiResponseMessage(HttpStatus.UNAUTHORIZED.value(), ErrorType.INVALID_MEMBER);
                return new ResponseEntity<>(apiRm, HttpStatus.valueOf(apiRm.getStatus()));
            }

            String username = jwtRequest.getUsername();
            String accessToken = jwtUtil.generateToken(username);
            String refreshToken = jwtUtil.generateRefreshToken(username);
            Cookie accessTokenCookie = cookieUtil.createCookie(
                    JwtUtil.ACCESS_TOKEN_NAME, JwtUtil.JWT_ACCESS_TOKEN_EXPIRE, accessToken);
            Cookie refreshTokenCookie = cookieUtil.createCookie(
                    JwtUtil.REFRESH_TOKEN_NAME, JwtUtil.JWT_REFRESH_TOKEN_EXPIRE, refreshToken);
            log.info("accessToken: {}, refreshToken: {}", accessTokenCookie, refreshTokenCookie);

            //Redis에 Refresh Token 저장 후 만료시간 설정
            redisUtil.setStringDataExpire(
                    RedisUtil.PREFIX_REFRESH_TOKEN_KEY + refreshToken, username, JwtUtil.JWT_REFRESH_TOKEN_EXPIRE);

            response.addCookie(accessTokenCookie);
            response.addCookie(refreshTokenCookie);
            return ResponseEntity.ok(new JwtResponse(accessTokenCookie, refreshTokenCookie));

        }catch (Exception e){
            e.printStackTrace();
            ApiResponseMessage apiRm = new ApiResponseMessage(HttpStatus.BAD_REQUEST.value(), ErrorType.INVALID_MEMBER);
            return new ResponseEntity<>(apiRm, HttpStatus.valueOf(apiRm.getStatus()));
        }
    }


    private boolean checkMemberAuth(JwtRequest jwtRequest) {
        String id = jwtRequest.getUsername();
        String password = jwtRequest.getPassword();
        final UserDetails userDetails = userDetailsService.loadUserByUsername(id);
        String realId = userDetails.getUsername();
        String realPassword = userDetails.getPassword();
        boolean isValidMember = id.equals(realId) && passwordEncoder.matches(password, realPassword);

        if(!isValidMember) return false; //유효하지않은 회원
        String authority = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toString();
        return !("ROLE_" + MemberRole.NONE).equals(authority); //비회원은 로그인 불가
    }

}
