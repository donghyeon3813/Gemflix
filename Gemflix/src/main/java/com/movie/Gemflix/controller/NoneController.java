package com.movie.Gemflix.controller;

import com.movie.Gemflix.common.ApiResponseMessage;
import com.movie.Gemflix.dto.MemberDTO;
import com.movie.Gemflix.entity.Member;
import com.movie.Gemflix.repository.MemberRepository;
import com.movie.Gemflix.service.CommonService;
import com.movie.Gemflix.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@Controller
public class NoneController {

    private final CommonService commonService;
    private final MemberService memberService;


    @PostMapping("/register")
    public ResponseEntity<ApiResponseMessage> registerMember(@RequestBody @Valid MemberDTO memberDTO,
                                                             BindingResult bindingResult){

        try {
            log.info("[registerMember] memberDTO: {}", memberDTO);
            ApiResponseMessage apiRm = commonService.checkError(bindingResult);
            if (apiRm != null) return new ResponseEntity<>(apiRm, HttpStatus.valueOf(apiRm.getStatus()));

            apiRm = memberService.registerMember(memberDTO);
            return new ResponseEntity<>(apiRm, HttpStatus.valueOf(apiRm.getStatus()));
        }catch (Exception e){
            log.error("registerMember Exception!!");
            e.printStackTrace();
        }
        return null;
    }

}
