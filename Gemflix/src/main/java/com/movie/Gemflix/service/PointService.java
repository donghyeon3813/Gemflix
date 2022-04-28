package com.movie.Gemflix.service;

import com.movie.Gemflix.common.Constant;
import com.movie.Gemflix.entity.Member;
import com.movie.Gemflix.entity.PointHistory;
import com.movie.Gemflix.repository.member.MemberRepository;
import com.movie.Gemflix.repository.member.MemberRepositorySupport;
import com.movie.Gemflix.repository.member.PointHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class PointService {

    private final ModelMapper modelMapper;
    private final MemberRepository memberRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final MemberRepositorySupport memberRepositorySupport;

    //유저 포인트 적립
    @Transactional
    public boolean plusPoint(String memberId, int changePoint, String type){
        Optional<Member> optMember = memberRepository.findByIdAndDelStatus(memberId, Constant.BooleanStringValue.FALSE);

        if(optMember.isPresent()){
            Member member = optMember.get();
            int beforePoint = member.getPoint();
            int afterPoint = beforePoint + changePoint;

            if(afterPoint < 0){
                return false;
            }
            return changeMemberPoint(memberId, beforePoint, afterPoint, changePoint, type, member);
        }
        return false;
    }

    //유저 포인트 차감
    @Transactional
    public boolean minusPoint(String memberId, int changePoint, String type){
        Optional<Member> optMember = memberRepository.findByIdAndDelStatus(memberId, Constant.BooleanStringValue.FALSE);

        if(optMember.isPresent()){
            Member member = optMember.get();
            int beforePoint = member.getPoint();
            int afterPoint = beforePoint - changePoint;
            log.info("beforePoint: {}, afterPoint: {}, changePoint: {}", beforePoint, afterPoint, changePoint);

            if(afterPoint < 0){
                return false;
            }
            return changeMemberPoint(memberId, beforePoint, afterPoint, changePoint, type, member);
        }
        return false;
    }

    private boolean changeMemberPoint(String memberId,
                                      int beforePoint,
                                      int afterPoint,
                                      int changePoint,
                                      String type,
                                      Member member) {
        try {
            //member point 수정
            memberRepositorySupport.modifyMemberPoint(memberId, afterPoint);
            //point history 기록
            PointHistory pointHistory = PointHistory.builder()
                    .changePoint(changePoint)
                    .beforePoint(beforePoint)
                    .afterPoint(afterPoint)
                    .type(type)
                    .regDate(LocalDateTime.now())
                    .member(member)
                    .build();
            pointHistoryRepository.save(pointHistory);
            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

}
