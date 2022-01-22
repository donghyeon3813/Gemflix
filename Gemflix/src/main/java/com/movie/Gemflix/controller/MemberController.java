package com.movie.Gemflix.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/member")
public class MemberController {

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