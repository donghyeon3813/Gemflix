package com.movie.Gemflix.controller;

import com.movie.Gemflix.common.CommonResponse;
import com.movie.Gemflix.common.Constant;
import com.movie.Gemflix.common.ErrorType;
import com.movie.Gemflix.dto.member.MemberDto;
import com.movie.Gemflix.entity.Member;
import com.movie.Gemflix.repository.MemberRepository;
import com.movie.Gemflix.service.CommonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final ModelMapper modelMapper;
    private final CommonService commonService;
    private final MemberRepository memberRepository;

    @Secured("ROLE_NO_PERMISSION")
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(HttpServletRequest request, HttpServletResponse response){
        String memberId = commonService.getRequesterId(request);
        Optional<Member> optMember = memberRepository.findById(memberId);
        log.info("optMember: {}", optMember);

        if(!optMember.isPresent()){
            return CommonResponse.createResponse(CommonResponse.builder()
                    .code(ErrorType.INVALID_MEMBER.getErrorCode())
                    .message(ErrorType.INVALID_MEMBER.getErrorMessage())
                    .build(), HttpStatus.UNAUTHORIZED);
        }

        Member member = optMember.get();
        log.info("member: {}", member);
        MemberDto memberDto = modelMapper.map(member, MemberDto.class);
        log.info("memberDto: {}", memberDto);
        memberDto.setPassword(null);

        return CommonResponse.createResponse(CommonResponse.builder()
                .code(Constant.Success.SUCCESS_CODE)
                .message("Member Profile Success")
                .data(memberDto)
                .build(), HttpStatus.OK);
    }

}