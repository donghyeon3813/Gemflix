package com.movie.Gemflix.service;

import com.movie.Gemflix.common.ApiResponseMessage;
import com.movie.Gemflix.common.ErrorType;
import com.movie.Gemflix.dto.MemberDTO;
import com.movie.Gemflix.entity.Member;
import com.movie.Gemflix.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final MemberRepository memberRepository;

    public ApiResponseMessage registerMember(MemberDTO memberDTO) {
        //ID중복 검사
        Optional<Member> isExists = memberRepository.findById(memberDTO.getId());
        if(isExists.isPresent()) return new ApiResponseMessage(HttpStatus.BAD_REQUEST.value(), ErrorType.DUPLICATED_MEMBER_ID);

        //회원 등록
        memberDTO.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
        Member member = modelMapper.map(memberDTO, Member.class);
        Member regMember = memberRepository.save(member);
        log.info("regMember: {}", regMember);
        return new ApiResponseMessage(HttpStatus.OK.value(), "Member Register Success");
    }

}
