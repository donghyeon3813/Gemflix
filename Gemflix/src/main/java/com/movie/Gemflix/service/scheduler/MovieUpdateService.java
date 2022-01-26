package com.movie.Gemflix.service.scheduler;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie.Gemflix.config.ApiProperties;
import com.movie.Gemflix.dto.movie.TheMovie;
import com.movie.Gemflix.entity.Filmography;
import com.movie.Gemflix.entity.Genre;
import com.movie.Gemflix.entity.Movie;
import com.movie.Gemflix.entity.People;
import com.movie.Gemflix.repository.FilmographyRepository;
import com.movie.Gemflix.repository.GenreRepository;
import com.movie.Gemflix.repository.MovieRepository;
import com.movie.Gemflix.repository.PeopleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Component
public class MovieUpdateService {
    private final GenreRepository genreRepository;
    private final MovieRepository movieRepository;
    private final PeopleRepository peopleRepository;
    private final FilmographyRepository filmographyRepository;
    private final WebClient webClient;
    private final ApiProperties apiProperties;

    public String getapi(){
        String api = "api_key="+apiProperties.getKey();
        String language = "language=ko";
        String apiAndLanguage = api+"&"+language ;
        return apiAndLanguage;
    }



    //장르 insert
    public void theMovieGetGenres(){
        String apiAndLanguage = getapi();
        List<Genre> genreList = new ArrayList<>();
        Mono<JSONObject> result = webClient
                .get()
                .uri("genre/movie/list?"+apiAndLanguage)// 인기순 영화 리스트 가져오기
                .retrieve()
                .bodyToMono(JSONObject.class);
        result.subscribe(genres -> {
            JSONArray tmgGenreList =  genres.getJSONArray("genres");
            for(int i = 0; i<tmgGenreList.size(); i++){
                Optional<Genre> optGenre =
                        genreRepository.findByGrNm(tmgGenreList.getJSONObject(i).get("name").toString());

                if(!optGenre.isPresent()){
                    Genre genre = Genre.builder()
                            .grNm(tmgGenreList.getJSONObject(i).get("name").toString())
                            .build();
                    genreList.add(genre);
                }else {
                    log.info( "Genre Exist {}",tmgGenreList.getJSONObject(i).get("name").toString());
                    continue;
                }
            }
            if(genreList.size()>0){
                log.info("insertGenreList :{}",genreList);
                genreRepository.saveAll(genreList);
            }
        });
    }

    //영화정보 insert
    public void theMovieGetMovie(){
        String apiAndLanguage = getapi();
        List<Movie> movieList = new ArrayList<>();
        String imgBaseUrl = "https://image.tmdb.org/t/p/original/";
        int page = 10;// 가져올 데이터 페이지 설정
        //영화 목록정보
        for(int i = 1; i<=page; i++){
            Mono<JSONObject> movieListResult =
                    webClient
                    .get().uri("movie/popular?"+apiAndLanguage+"&page="+i)// 인기순 영화 리스트 가져오기
                    .retrieve()
                    .bodyToMono(JSONObject.class);
            movieListResult.subscribe(result -> {
                JSONArray movieJsonArr = result.getJSONArray("results");
                for (int j = 0; j<movieJsonArr.size(); j++){
                    String movieId = movieJsonArr.getJSONObject(j).get("id").toString();
                    TheMovie theMovieData = movieJsonArr.getObject(j,TheMovie.class);
                    Mono<JSONObject> movieDetail =
                            webClient
                            .get()
                            .uri("movie/"+movieId+"?"+apiAndLanguage+"&append_to_response=release_dates")// 인기순 영화 리스트 가져오기
                            .retrieve()
                            .bodyToMono(JSONObject.class);
                    //무비 상세정보
                    movieDetail.subscribe(detailResult -> {
                        JSONArray ratingList = detailResult
                                .getJSONObject("release_dates")
                                .getJSONArray("results");
                        theMovieData.setRuntime(detailResult.get("runtime").toString());
                        // 영화 등급정보
                        if(!ratingList.isEmpty()){
                            for (int k=0; k<ratingList.size(); k++){
                                if(ratingList.getJSONObject(k).get("iso_3166_1").toString().equals("KR")){
                                    String theMoveRating = ratingList.getJSONObject(k).getJSONArray("release_dates")
                                            .getJSONObject(0).get("certification").toString();
                                    String rating = "";
                                    switch (theMoveRating){
                                        case "ALL":
                                        case "전체관람가":
                                            rating = "1";
                                        case "12":
                                        case "12세 이상 관람가":
                                            rating = "2";
                                            break;
                                        case "15":
                                            rating = "3";
                                            break;
                                        case "18":
                                        case "19":
                                            rating = "4";
                                            break;
                                    }
                                    theMovieData.setRating(rating);
                                    if(!theMovieData.getRating().equals("")){
                                        theMovieData.setGenreName(
                                                detailResult
                                                .getJSONArray("genres")
                                                .getJSONObject(0)
                                                .get("name").toString()
                                        );
                                        Optional<Movie> optMovie = movieRepository.findByTitle(theMovieData.getTitle());
                                        if(!optMovie.isPresent()){
                                            Optional<Genre> genre = genreRepository.findByGrNm(theMovieData.getGenreName());
                                            SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-DD");
                                            try {
                                                Movie movie = Movie.builder()
                                                        .genre(genre.get())
                                                        .title(theMovieData.getTitle())
                                                        .rating(theMovieData.getRating())
                                                        .openDt(format.parse(theMovieData.getRelease_date()))
                                                        .content(theMovieData.getOverview())
                                                        .extent(Integer.parseInt(theMovieData.getRuntime()))
                                                        .imgUrl(imgBaseUrl+theMovieData.getPoster_path())
                                                        .backImgUrl(imgBaseUrl+theMovieData.getBackdrop_path())
                                                        .apiId(theMovieData.getId())
                                                        .build();
                                                movieRepository.save(movie);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                        }else{
                                            continue;
                                        }
                                    }
                                }
                            }
                        }
                    });
                }
            });
        }
    }
    public void theMovieGetPeople(){
        String apiAndLanguage = getapi();
        List<Movie> movieList = movieRepository.findAll();
        for (Movie movie : movieList) {
            String apiId = movie.getApiId();

            Mono<JSONObject> peopleListResult = webClient
                    .get()
                    .uri("https://api.themoviedb.org/3/movie/"+apiId+"/credits?"+apiAndLanguage)
                    .retrieve()
                    .bodyToMono(JSONObject.class);
            peopleListResult.subscribe(credits -> {
                JSONArray castList = credits.getJSONArray("cast");
                for(int i=0; i<castList.size(); i++){
                    String peopleApiId = castList.getJSONObject(i).get("id").toString();
                    if(castList.getJSONObject(i).get("known_for_department").toString().equals("Acting")){
                        if(i<5){
                            String type = "2";
                            Flux<JSONObject> peopleDetail = webClient
                                    .get()
                                    .uri("https://api.themoviedb.org/3/person/"+peopleApiId+"?"+apiAndLanguage)
                                    .retrieve()
                                    .bodyToFlux(JSONObject.class);
                            peopleDetail.subscribe(info -> {
                                SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-DD");
                                Date birth = null;
                                String place[];
                                String nationality = null;
                                String name = info.get("name").toString();
                                try {

                                    if(info.get("birthday")!=null && !info.get("birthday").toString().equals("")) {
                                        birth = format.parse(info.get("birthday").toString());
                                    }
                                    if(info.get("place_of_birth")!=null){
                                        place = info.get("place_of_birth").toString().split(", ");
                                        nationality = place[place.length-1];
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                boolean peopleDup = peopleRepository.existsByApiId(peopleApiId);
                                if(!peopleDup){
                                    People people = People.builder()
                                            .name(name)
                                            .type(type)
                                            .birth(birth)
                                            .nationality(nationality)
                                            .apiId(peopleApiId)
                                            .build();
                                    peopleRepository.save(people);
                                }
                            });
                        }
                    }else if(castList.getJSONObject(i).get("known_for_department").toString().equals("Directing")){

                            String type = "1";
                            Flux<JSONObject> peopleDetail = webClient
                                    .get()
                                    .uri("https://api.themoviedb.org/3/person/"+peopleApiId+"?"+apiAndLanguage)
                                    .retrieve()
                                    .bodyToFlux(JSONObject.class);
                            peopleDetail.subscribe(info -> {
                                SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-DD");
                                Date birth = null;
                                String place[];
                                String nationality = null;
                                String name = info.get("name").toString();
                                try {
                                    if(info.get("birthday")!=null && !info.get("birthday").toString().equals("")) {
                                        birth = format.parse(info.get("birthday").toString());
                                    }
                                    if(info.get("place_of_birth")!=null){
                                        place = info.get("place_of_birth").toString().split(", ");
                                        nationality = place[place.length-1];
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                boolean peopleDup = peopleRepository.existsByApiId(peopleApiId);
                                if(!peopleDup){
                                    People people = People.builder()
                                            .name(name)
                                            .type(type)
                                            .birth(birth)
                                            .nationality(nationality)
                                            .apiId(peopleApiId)
                                            .build();
                                    peopleRepository.save(people);
                                }
                            });
                    }
                }
            });
        }
    }
    // 출연작품
    public void theMovieFilmography(){
        String apiAndLanguage = getapi();
        List<People> peopleList = peopleRepository.findAll();
        for (People people: peopleList) {
            String peopleApiKey = people.getApiId();
            Mono<JSONObject> result = webClient
                    .get()
                    .uri("person/"+peopleApiKey+"/movie_credits?"+apiAndLanguage)
                    .retrieve()
                    .bodyToMono(JSONObject.class);
            result.subscribe(info -> {
                List<Filmography> saveFilmographyList = new ArrayList<>();
                JSONArray filmographyList = info.getJSONArray("cast");
                for(int i = 0; i<filmographyList.size(); i++){
                    String movieTitle = filmographyList.getJSONObject(i).get("title").toString();
                    Optional<Movie> movieInfo = movieRepository.findByTitle(movieTitle);
                    if(movieInfo.isPresent()){
                        Optional<Filmography> optFilmography =
                                filmographyRepository.findByMovieAndPeople(movieInfo.get(), people);

                        if(!optFilmography.isPresent()){
                            Filmography filmography = Filmography.builder()
                                    .movie(movieInfo.get())
                                    .people(people)
                                    .build();
                            saveFilmographyList.add(filmography);
                        }
                    }else{
                        continue;
                    }
                }
                if(saveFilmographyList.size()>0){
                    log.info("insertGenreList :{}",saveFilmographyList);
                    filmographyRepository.saveAll(saveFilmographyList);
                }
            });
        }

    }




}
