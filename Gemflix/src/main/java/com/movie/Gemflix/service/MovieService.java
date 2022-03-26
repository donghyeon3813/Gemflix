package com.movie.Gemflix.service;

import com.movie.Gemflix.common.CommonResponse;
import com.movie.Gemflix.common.ErrorType;
import com.movie.Gemflix.dto.movie.*;
import com.movie.Gemflix.entity.Member;
import com.movie.Gemflix.entity.Review;
import com.movie.Gemflix.entity.Ticket;
import com.movie.Gemflix.repository.member.MemberRepository;
import com.movie.Gemflix.repository.movie.MovieRepositorySupport;
import com.movie.Gemflix.repository.movie.ReviewRepository;
import com.movie.Gemflix.repository.movie.ReviewRepositorySupport;
import com.movie.Gemflix.repository.movie.TicketRepositorySupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class MovieService {

    private final MovieRepositorySupport movieRepositorySupport;

    private final CommonService commonService;

    private final MemberRepository memberRepository;

    private final ReviewRepository reviewRepository;

    private final TicketRepositorySupport ticketRepositorySupport;

    private final ReviewRepositorySupport reviewRepositorySupport;



    public Page<MovieListDto> findMovieList(MovieSearchDto movieSearchDto, Pageable pageable) throws Exception {

        return movieRepositorySupport.findMovieList(movieSearchDto, pageable);
    }

    public MovieDetailDto findMovieDetails(MovieSearchDto movieSearchDto) throws Exception {

        return movieRepositorySupport.findMovieDetails(movieSearchDto);

    }

    @Transactional
    public CommonResponse reviewRegister( ReviewDto reviewDto, HttpServletRequest request) throws  Exception{
        String id = commonService.getRequesterId(request);
        Optional<Member> member = memberRepository.findById(id);
        System.out.println(member.get().getMId());
        if(member.isPresent()){
            Long mId = member.get().getMId();
            Long mvId = reviewDto.getMvId();
            List<Ticket> ticketList =  ticketRepositorySupport.findByMemberTicket(mId, mvId);
            if(ticketList.isEmpty()){
                return new CommonResponse(ErrorType.MOVIE_REVIEW_NOT_TICKET.getErrorCode(),
                        ErrorType.MOVIE_REVIEW_NOT_TICKET.getErrorMessage());
            }
            LocalDateTime currentDateTime = LocalDateTime.now();
            Review review = Review.builder()
                    .content(reviewDto.getComment())
                    .score(reviewDto.getScore())
                    .ticket(ticketList.get(0))
                    .build();
            log.info("{}",review);
            reviewRepository.save(review);
            ticketRepositorySupport.ticketStateModify(ticketList.get(0).getTkId());

            return null;
        }
        return new CommonResponse(ErrorType.INVALID_MEMBER.getErrorCode(),
                ErrorType.INVALID_MEMBER.getErrorMessage());

    }

    public Page<ReviewListDto> findReviewList(Long mvId, Pageable pageable) throws Exception{
        return reviewRepositorySupport.findReviewList(mvId, pageable);
    }
}