package com.movie.Gemflix.security.service;

import com.movie.Gemflix.common.CommonResponse;
import com.movie.Gemflix.common.Constant;
import com.movie.Gemflix.common.ErrorType;
import com.movie.Gemflix.dto.member.MemberDto;
import com.movie.Gemflix.dto.member.PointHistoryDto;
import com.movie.Gemflix.entity.Member;
import com.movie.Gemflix.entity.MemberRole;
import com.movie.Gemflix.entity.PointHistory;
import com.movie.Gemflix.entity.QMember;
import com.movie.Gemflix.repository.member.MemberRepository;
import com.movie.Gemflix.repository.member.PointHistoryRepository;
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
import java.util.ArrayList;
import java.util.List;
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
    private final PointHistoryRepository pointHistoryRepository;

    private QMember qMember = QMember.member;

    private static final int REGISTER_POINT = 200;

    @PersistenceContext
    private EntityManager entityManager;

    public CommonResponse registerMember(MemberDto memberDTO) throws Exception{
        //ID 중복 검사
        Optional<Member> optMember = memberRepository.findByIdAndDelStatus(memberDTO.getId(), Constant.BooleanStringValue.FALSE);
        if(optMember.isPresent()){
            return new CommonResponse(ErrorType.DUPLICATED_MEMBER_ID.getErrorCode(),
                    ErrorType.DUPLICATED_MEMBER_ID.getErrorMessage());
        }

        //EMAIL 중복 검사
        Optional<Member> optMember02 = memberRepository.findByEmailAndDelStatus(memberDTO.getEmail(), Constant.BooleanStringValue.FALSE);
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

        //초기값 세팅
        settingMemberDefaultValue(memberDTO);
        PointHistoryDto pointHistoryDto = PointHistoryDto.builder()
                .changePoint(REGISTER_POINT)
                .beforePoint(0)
                .afterPoint(REGISTER_POINT)
                .type(Constant.PointType.REGISTER_POINT)
                .regDate(LocalDateTime.now())
                .member(memberDTO)
                .build();
        log.info("pointHistoryDto: {}", pointHistoryDto);

        Member member = modelMapper.map(memberDTO, Member.class);
        PointHistory pointHistory = modelMapper.map(pointHistoryDto, PointHistory.class);
        log.info("member: {}", member);
        log.info("pointHistory: {}", pointHistory);
        pointHistoryRepository.save(pointHistory);
        return null;
    }

    private void settingMemberDefaultValue(MemberDto memberDTO) {

        memberDTO.setStatus(Constant.BooleanStringValue.TRUE);
        memberDTO.setAuthority(MemberRole.NO_PERMISSION);
        memberDTO.setGrade(Constant.Grade.BRONZE);
        memberDTO.setDelStatus(Constant.BooleanStringValue.FALSE);
        memberDTO.setPoint(REGISTER_POINT);

        log.info("memberDTO: {}", memberDTO);
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