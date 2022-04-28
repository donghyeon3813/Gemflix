package com.movie.Gemflix.controller;

import com.movie.Gemflix.common.CommonResponse;
import com.movie.Gemflix.common.Constant;
import com.movie.Gemflix.common.ErrorType;
import com.movie.Gemflix.dto.member.MemberDto;
import com.movie.Gemflix.entity.Member;
import com.movie.Gemflix.repository.member.MemberRepository;
import com.movie.Gemflix.repository.member.MemberRepositorySupport;
import com.movie.Gemflix.service.CommonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final ModelMapper modelMapper;
    private final CommonService commonService;
    private final MemberRepository memberRepository;
    private final MemberRepositorySupport memberRepositorySupport;

    //회원프로필 조회
    @Secured({"ROLE_NO_PERMISSION", "ROLE_MEMBER", "ROLE_ADMIN"})
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(HttpServletRequest request){
        log.info("[getProfile] request:{}", request);

        String memberId = commonService.getRequesterId(request);
        Optional<Member> optMember = memberRepository.findByIdAndDelStatus(memberId, Constant.BooleanStringValue.FALSE);
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

    //회원탈퇴
    @Transactional
    @Secured({"ROLE_NO_PERMISSION", "ROLE_MEMBER", "ROLE_ADMIN"})
    @DeleteMapping("/{memberId}")
    public ResponseEntity<?> deleteMember(@PathVariable String memberId){
        log.info("[deleteMember] memberId:{}", memberId);
        Optional<Member> optMember = memberRepository.findByIdAndDelStatus(memberId, Constant.BooleanStringValue.FALSE);

        if(optMember.isPresent()){
            memberRepositorySupport.deleteMember(memberId);
            return CommonResponse.createResponse(CommonResponse.builder()
                    .code(Constant.Success.SUCCESS_CODE)
                    .message("Member Delete Success")
                    .build(), HttpStatus.OK);

        }else{
            return CommonResponse.createResponse(CommonResponse.builder()
                    .code(ErrorType.INVALID_MEMBER.getErrorCode())
                    .message(ErrorType.INVALID_MEMBER.getErrorMessage())
                    .build(), HttpStatus.BAD_REQUEST);
        }
    }

}