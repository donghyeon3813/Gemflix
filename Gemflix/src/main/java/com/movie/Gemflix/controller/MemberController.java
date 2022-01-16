package com.movie.Gemflix.controller;

import com.movie.Gemflix.dto.MemberDTO;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Slf4j
@Controller
public class MemberController {

    @PostMapping("/register")
    public ResponseEntity registerMember(@RequestBody @Valid MemberDTO memberDTO, BindingResult bindingResult){
        log.info("[registerMember] memberDTO: {}", memberDTO);

        if(bindingResult.hasErrors()) {
            StringBuilder builder = new StringBuilder();
            bindingResult.getAllErrors().forEach(objectError -> {
                FieldError field = (FieldError) objectError;
                String message = objectError.getDefaultMessage();

                builder.append("field : "+field.getField());
                builder.append("message : "+message);
            });

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(builder.toString());
        }
        return ResponseEntity.ok(memberDTO);
    }

    @GetMapping("/none")
    public void exNone(){
        log.info("exNone...........");
    }

    @GetMapping("/member")
    public void exMember(){
        log.info("exMember...........");
    }

    @GetMapping("/admin")
    public void exAdmin(){
        log.info("exAdmin...........");
    }

    /*@GetMapping("/login/oauth2/code/kakao")
    public String kakaoLogin(String code) {
        // authorizedCode: 카카오 서버로부터 받은 인가 코드
        System.out.println("code: " + code);
//        userService.kakaoLogin(code);

        return "redirect:/";
    }*/

}
