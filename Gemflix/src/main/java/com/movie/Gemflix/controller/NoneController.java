package com.movie.Gemflix.controller;

import com.movie.Gemflix.common.ApiResponseMessage;
import com.movie.Gemflix.dto.MemberDTO;
import com.movie.Gemflix.entity.Member;
import com.movie.Gemflix.security.service.AuthService;
import com.movie.Gemflix.security.service.EmailService;
import com.movie.Gemflix.service.CommonService;
import com.movie.Gemflix.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@Controller
public class NoneController {

    private final CommonService commonService;
    private final AuthService authService;

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

}
