package com.movie.Gemflix.repository.member;

import com.movie.Gemflix.entity.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Repository
public class MemberRepositorySupport {

    private final JPAQueryFactory query;
    private QMember member = QMember.member;
    private QPointHistory pointHistory = QPointHistory.pointHistory;

    public void modifyMemberPoint(String memberId, int afterPoint){
        query.update(member)
                .set(member.point, afterPoint)
                .set(member.modDate, LocalDateTime.now())
                .where(member.id.eq(memberId))
                .execute();
    }

}
