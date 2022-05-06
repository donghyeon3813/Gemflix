package com.movie.Gemflix.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie.Gemflix.common.CommonResponse;
import com.movie.Gemflix.common.Constant;
import com.movie.Gemflix.common.ErrorType;
import com.movie.Gemflix.dto.movie.*;
import com.movie.Gemflix.entity.Member;
import com.movie.Gemflix.entity.Review;
import com.movie.Gemflix.entity.Ticket;
import com.movie.Gemflix.repository.member.MemberRepository;
import com.movie.Gemflix.repository.movie.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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

    private final FilmographyRepositorySupport filmographyRepositorySupport;

    private final TicketRepositorySupport ticketRepositorySupport;

    private final ReviewRepositorySupport reviewRepositorySupport;

    private final ModelMapper modelMapper;



    public Page<MovieListDto> findMovieList(MovieSearchDto movieSearchDto, Pageable pageable) throws Exception {

        return movieRepositorySupport.findMovieList(movieSearchDto, pageable);
    }

    public MovieDetailDto findMovieDetails(MovieSearchDto movieSearchDto) throws Exception {

        return movieRepositorySupport.findMovieDetails(movieSearchDto);

    }

    @Transactional
    public CommonResponse reviewRegister( ReviewRegisterDto reviewRegisterDto, HttpServletRequest request) throws  Exception{
        String id = commonService.getRequesterId(request);
        Optional<Member> member = memberRepository.findByIdAndDelStatus(id, Constant.BooleanStringValue.FALSE);
        if(member.isPresent()){
            Long mId = member.get().getMId();
            Long mvId = reviewRegisterDto.getMvId();
            List<Ticket> ticketList =  ticketRepositorySupport.findByMemberTicket(mId, mvId);
            if(ticketList.isEmpty()){
                return new CommonResponse(ErrorType.MOVIE_REVIEW_NOT_TICKET.getErrorCode(),
                        ErrorType.MOVIE_REVIEW_NOT_TICKET.getErrorMessage());
            }
            log.info("ticket 정보 :");
            TicketDto ticket = modelMapper.map(ticketList.get(0), TicketDto.class);
            ReviewDto reviewDto = new ReviewDto();
            reviewDto.setContent(reviewRegisterDto.getContent());
            reviewDto.setScore(reviewRegisterDto.getScore());
            reviewDto.setTicket(ticket);
            log.info("ticket 정보 : {}",ticket);
            Review review = modelMapper.map(reviewDto, Review.class);
            log.info("{}",review);
            reviewRepository.save(review);
            ticketRepositorySupport.ticketStateModify(ticketList.get(0).getTkId());

            return null;
        }
        return new CommonResponse(ErrorType.INVALID_MEMBER.getErrorCode(),
                ErrorType.INVALID_MEMBER.getErrorMessage());

    }

    @Transactional
    public CommonResponse reviewModify( ReviewRegisterDto reviewRegisterDto, HttpServletRequest request) throws  Exception{
        String id = commonService.getRequesterId(request);
        Optional<Member> member = memberRepository.findByIdAndDelStatus(id, Constant.BooleanStringValue.FALSE);
        if(member.isPresent()){
            Long mId = member.get().getMId();
            long cnt = reviewRepositorySupport.reviewModify(reviewRegisterDto.getRvId(), reviewRegisterDto.getContent());
            if(cnt == 0){
                return new CommonResponse(ErrorType.INVALID_MEMBER.getErrorCode(),
                        ErrorType.INVALID_MEMBER.getErrorMessage());
            }
            return null;
        }
        return new CommonResponse(ErrorType.INVALID_MEMBER.getErrorCode(),
                ErrorType.INVALID_MEMBER.getErrorMessage());

    }

    @Transactional
    public CommonResponse reviewDelete( Long rvId, HttpServletRequest request) throws  Exception{
        String id = commonService.getRequesterId(request);
        Optional<Member> member = memberRepository.findByIdAndDelStatus(id, Constant.BooleanStringValue.FALSE);
        if(member.isPresent()){
            Long mId = member.get().getMId();
            long cnt = reviewRepositorySupport.reviewDelete(rvId);
            if(cnt == 0){
                return new CommonResponse(ErrorType.INVALID_MEMBER.getErrorCode(),
                        ErrorType.INVALID_MEMBER.getErrorMessage());
            }
            return null;
        }
        return new CommonResponse(ErrorType.INVALID_MEMBER.getErrorCode(),
                ErrorType.INVALID_MEMBER.getErrorMessage());

    }

    public Page<ReviewListDto> findReviewList(Long mvId, Pageable pageable) throws Exception{
        return reviewRepositorySupport.findReviewList(mvId, pageable);
    }

    public List<FilmographyList> findFilmographyList(Long peId) throws Exception {

        return filmographyRepositorySupport.findFilmography(peId);
    }
}
