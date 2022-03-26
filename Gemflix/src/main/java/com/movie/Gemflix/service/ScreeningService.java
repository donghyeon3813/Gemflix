package com.movie.Gemflix.service;

import com.movie.Gemflix.dto.movie.MovieDto;
import com.movie.Gemflix.dto.movie.ScreeningDto;
import com.movie.Gemflix.dto.movie.TheaterRoomDto;
import com.movie.Gemflix.entity.Movie;
import com.movie.Gemflix.entity.Screening;
import com.movie.Gemflix.entity.TheaterRoom;
import com.movie.Gemflix.repository.movie.MovieRepository;
import com.movie.Gemflix.repository.movie.ScreeningRepository;
import com.movie.Gemflix.repository.movie.TheaterRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScreeningService {

    private final MovieRepository movieRepository;
    private final TheaterRoomRepository theaterRoomRepository;
    private final ScreeningRepository screeningRepository;
    private final ModelMapper modelMapper;

    private static final List<LocalDateTime> START_TIME_LIST = new ArrayList<>();
    private static final String TYPE_DEFAULT = "1";
    private static final String TYPE_JOJO = "2";
    private static final String TYPE_SIMYA = "3";
    private static final String STATUS_TRUE = "1"; //상영중

    public void settingMovieScreening() {
        settingStartTime();
        LocalDateTime after7Day = LocalDateTime.now().plusDays(7);
        Optional<List<Movie>> optMovies = movieRepository.findByStatus(STATUS_TRUE);

        if(!optMovies.isPresent()){
            return;
        }else{
            Random random = new Random();
            List<Screening> screenings = new ArrayList<>();

            List<Movie> movies = optMovies.get();
            List<TheaterRoom> rooms = theaterRoomRepository.findAll();

            for (TheaterRoom room : rooms) {
                for (int i = 0; i < START_TIME_LIST.size(); i++) {
                    Movie thisMovie = movies.get(random.nextInt(movies.size()));
                    LocalDateTime thisTime = START_TIME_LIST.get(i);

                    TheaterRoomDto theaterRoomDto = modelMapper.map(room, TheaterRoomDto.class);
                    MovieDto movieDto = modelMapper.map(thisMovie, MovieDto.class);

                    ScreeningDto screeningDto = ScreeningDto.builder()
                            .theaterRoom(theaterRoomDto)
                            .movie(movieDto)
                            .startTime(thisTime)
                            .endTime(thisTime.plusMinutes((long) thisMovie.getExtent()))
                            .screeningDate(after7Day)
                            .type(getType(i, thisTime))
                            .build();
                    log.info("screeningDto: {}", screeningDto);
                    Screening screening = modelMapper.map(screeningDto, Screening.class);
                    log.info("screening: {}", screening);
                    screenings.add(screening);
                }
            }
            screeningRepository.saveAll(screenings);
        }
    }

    private String getType(int i, LocalDateTime thisTime) {
        if(i == 0){
            return TYPE_JOJO;
        }else if(i == 4){
            return TYPE_SIMYA;
        }else{
            return TYPE_DEFAULT;
        }
    }

    private void settingStartTime(){
        LocalDateTime after7Day = LocalDateTime.now().plusDays(7);
        //직접 변경
        LocalDateTime hour11 = after7Day.withHour(11).withMinute(0).withSecond(0);
        LocalDateTime hour14 = after7Day.withHour(14).withMinute(0).withSecond(0);
        LocalDateTime hour17 = after7Day.withHour(17).withMinute(0).withSecond(0);
        LocalDateTime hour20 = after7Day.withHour(20).withMinute(0).withSecond(0);
        LocalDateTime hour23 = after7Day.withHour(23).withMinute(0).withSecond(0);
        START_TIME_LIST.add(hour11);
        START_TIME_LIST.add(hour14);
        START_TIME_LIST.add(hour17);
        START_TIME_LIST.add(hour20);
        START_TIME_LIST.add(hour23);
        log.info("START_TIME_LIST: {}", START_TIME_LIST);
    }

}