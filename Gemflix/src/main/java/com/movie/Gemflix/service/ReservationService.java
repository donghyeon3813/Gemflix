package com.movie.Gemflix.service;

import com.movie.Gemflix.dto.movie.MovieListDto;
import com.movie.Gemflix.dto.reservation.ScreeningListDto;
import com.movie.Gemflix.entity.Theater;
import com.movie.Gemflix.repository.movie.MovieRepositorySupport;
import com.movie.Gemflix.repository.reservation.ScreeningRepositorySupport;
import com.movie.Gemflix.repository.reservation.TheatherRepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final TheatherRepositorySupport theatherRepositorySupport;
    private final MovieRepositorySupport movieRepositorySupport;
    private final ScreeningRepositorySupport screeningRepositorySupport;

    public List<String> findPlaceList()  throws Exception{
        return theatherRepositorySupport.findPlaceList();
    }

    public List<Theater> findTheaterList(String place) throws Exception{
        return theatherRepositorySupport.findTheaterList(place);
    }

    public List<MovieListDto> findShowingMovieList() throws Exception {
        return movieRepositorySupport.findShowingMovieList();
    }

    public List<ScreeningListDto> findScreenList() {
        return screeningRepositorySupport.findScreenList();
    }
}
