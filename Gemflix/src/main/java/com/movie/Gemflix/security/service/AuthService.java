package com.movie.Gemflix.security.service;

import com.movie.Gemflix.common.ApiResponseMessage;
import com.movie.Gemflix.common.ErrorType;
import com.movie.Gemflix.dto.MemberDTO;
import com.movie.Gemflix.entity.Member;
import com.movie.Gemflix.entity.MemberRole;
import com.movie.Gemflix.entity.QMember;
import com.movie.Gemflix.repository.MemberRepository;
import com.movie.Gemflix.security.util.RedisUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final MemberRepository memberRepository;
    private final EmailService emailService;
    private final RedisUtil redisUtil;
    private final JPAQueryFactory queryFactory;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public ApiResponseMessage registerMember(MemberDTO memberDTO) throws Exception{
        //ID중복 검사
        Optional<Member> isExists = memberRepository.findById(memberDTO.getId());
        if(isExists.isPresent()) return new ApiResponseMessage(HttpStatus.BAD_REQUEST.value(), ErrorType.DUPLICATED_MEMBER_ID);

        //Email 인증
        if(!emailService.sendVerificationMail(memberDTO)){
            return new ApiResponseMessage(HttpStatus.BAD_REQUEST.value(), ErrorType.INVALID_MEMBER_EMAIL);
        }
        //회원 등록
        memberDTO.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
        Member member = modelMapper.map(memberDTO, Member.class);
        Member regMember = memberRepository.save(member);
        log.info("regMember: {}", regMember);
        return new ApiResponseMessage(HttpStatus.OK.value(), "Member Register Success");
    }

    @Transactional
    public ApiResponseMessage verifyEmail(String key) throws Exception {
        String memberId = redisUtil.getStringData(key);
        if(memberId == null){
            return new ApiResponseMessage(HttpStatus.BAD_REQUEST.value(), ErrorType.INVALID_MEMBER_ID);
        }else{
            modifyUserRole(memberId);
            redisUtil.deleteData(key);
            return new ApiResponseMessage(HttpStatus.OK.value(), "Member Email Permission Success");
        }
    }

    public void modifyUserRole(String memberId) throws Exception{
        //TODO: modDate 수정
        Instant instant = Instant.ofEpochMilli(System.currentTimeMillis());
        LocalDateTime now = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();

        QMember qMember = QMember.member;
        queryFactory.update(qMember)
                .set(qMember.authority, MemberRole.MEMBER)
                .set(qMember.modDate, now)
                .where(qMember.id.eq(memberId))
                .execute();
        entityManager.flush();
        entityManager.clear();
    }


}
