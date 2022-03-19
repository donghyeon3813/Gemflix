package com.movie.Gemflix.security.service;

import com.movie.Gemflix.common.CommonResponse;
import com.movie.Gemflix.common.ErrorType;
import com.movie.Gemflix.dto.member.MemberDto;
import com.movie.Gemflix.entity.Member;
import com.movie.Gemflix.entity.MemberRole;
import com.movie.Gemflix.entity.QMember;
import com.movie.Gemflix.repository.member.MemberRepository;
import com.movie.Gemflix.security.util.RedisUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final EmailService emailService;
    private final RedisUtil redisUtil;
    private final JPAQueryFactory queryFactory;
    private final MemberRepository memberRepository;

    private QMember qMember = QMember.member;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public CommonResponse registerMember(MemberDto memberDTO) throws Exception{
        //ID 중복 검사
        Optional<Member> optMember = memberRepository.findById(memberDTO.getId());
        if(optMember.isPresent()){
            return new CommonResponse(ErrorType.DUPLICATED_MEMBER_ID.getErrorCode(),
                    ErrorType.DUPLICATED_MEMBER_ID.getErrorMessage());
        }

        //EMAIL 중복 검사
        Optional<Member> optMember02 = memberRepository.findByEmail(memberDTO.getEmail());
        if(optMember02.isPresent()){
            return new CommonResponse(ErrorType.DUPLICATED_MEMBER_EMAIL.getErrorCode(),
                    ErrorType.DUPLICATED_MEMBER_EMAIL.getErrorMessage());
        }

        //Email 인증
        if(!emailService.sendVerificationMail(memberDTO)){
            return new CommonResponse(ErrorType.INVALID_MEMBER_EMAIL.getErrorCode(),
                    ErrorType.INVALID_MEMBER_EMAIL.getErrorMessage());
        }
        //회원 등록
        memberDTO.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
        Member member = modelMapper.map(memberDTO, Member.class);
        memberRepository.save(member);
        return null;
    }

    @Transactional
    public CommonResponse verifyEmail(String key) throws Exception {
        String memberId = redisUtil.getStringData(RedisUtil.PREFIX_EMAIL_KEY + key);
        if(memberId == null){
            return new CommonResponse(ErrorType.INVALID_MEMBER_ID.getErrorCode(),
                    ErrorType.INVALID_MEMBER_ID.getErrorMessage());
        }else{
            modifyUserRole(memberId);authHeader:
            redisUtil.deleteData(RedisUtil.PREFIX_EMAIL_KEY + key);
            return null;
        }
    }

    public void modifyUserRole(String memberId) throws Exception{
        queryFactory.update(qMember)
                .set(qMember.authority, MemberRole.MEMBER)
                .set(qMember.modDate, LocalDateTime.now())
                .where(qMember.id.eq(memberId))
                .execute();
        entityManager.flush();
        entityManager.clear();
    }


}