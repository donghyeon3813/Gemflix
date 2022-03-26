package com.movie.Gemflix.repository.movie;

import com.movie.Gemflix.dto.movie.MovieListDto;
import com.movie.Gemflix.dto.movie.MovieSearchDto;
import com.movie.Gemflix.dto.movie.ReviewListDto;
import com.movie.Gemflix.entity.QMovie;
import com.movie.Gemflix.entity.QReview;
import com.movie.Gemflix.entity.QScreening;
import com.movie.Gemflix.entity.QTicket;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReviewRepositorySupport {
    private final JPAQueryFactory query;
    private QReview review = QReview.review;
    private QMovie movie= QMovie.movie;
    private QScreening screening = QScreening.screening;
    private QTicket ticket = QTicket.ticket;

    public Page<ReviewListDto> findReviewList(Long mvId, Pageable pageable) throws Exception {
        List<ReviewListDto> reviewList = query
                .select(Projections.fields(ReviewListDto.class,
                        review.rvId,
                        review.content,
                        review.ticket.member.id.as("id"),
                        review.regDate,
                        review.score
                ))
                .from(review)
                .innerJoin(review.ticket, ticket)
                .innerJoin(ticket.screening, screening)
                .innerJoin(screening.movie, movie)
                .where(movie.mvId.eq(mvId),
                        review.delStatus.eq("0"))
                .orderBy(review.regDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        long reviewTotal = query.select(review.rvId.count())
                .from(review)
                .innerJoin(review.ticket, ticket)
                .innerJoin(ticket.screening, screening)
                .innerJoin(screening.movie, movie)
                .where(movie.mvId.eq(mvId))
                .fetchOne();
        return PageableExecutionUtils.getPage(reviewList, pageable, () -> reviewTotal);
    }

    public long reviewModify(Long rvId, String content){
        return query.update(review).where(review.rvId.eq(rvId))
                .set(review.content,content)
                .execute();
    }
    public long reviewDelete(Long rvId){
        return query.update(review)
                .where(review.rvId.eq(rvId))
                .set(review.delStatus,"1")
                .execute();
    }
}
