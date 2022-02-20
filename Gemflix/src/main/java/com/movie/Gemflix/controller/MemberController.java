package com.movie.Gemflix.controller;

import com.movie.Gemflix.common.ApiResponseMessage;
import com.movie.Gemflix.common.ErrorType;
import com.movie.Gemflix.dto.MemberDTO;
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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final ModelMapper modelMapper;
    private final CommonService commonService;
    private final MemberRepository memberRepository;

    @Secured("ROLE_NO_PERMISSION")
    @PostMapping("/profile")
    public ResponseEntity<?> getProfile(HttpServletRequest request, HttpServletResponse response){
        log.info("===== profile =====");
        String memberId = commonService.getRequesterId(request);
        Optional<Member> optMember = memberRepository.findById(memberId);

        if(!optMember.isPresent()){
            new ApiResponseMessage(HttpStatus.UNAUTHORIZED.value(), ErrorType.INVALID_MEMBER);
        }
        Member member = optMember.get();
        log.info("member: {}", member);
        MemberDTO memberDto = modelMapper.map(member, MemberDTO.class);
        log.info("memberDto: {}", memberDto);
        memberDto.setPassword(null);
        return ResponseEntity.ok(memberDto);
    }

}