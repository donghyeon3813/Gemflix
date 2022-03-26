package com.movie.Gemflix.service.scheduler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.movie.Gemflix.config.ApiProperties;
import com.movie.Gemflix.dto.movie.TheMovie;
import com.movie.Gemflix.entity.*;
import com.movie.Gemflix.repository.movie.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import org.springframework.web.reactive.function.client.WebClient;

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
    private final TrailerRepository trailerRepository;

    public String getapi(){
        String api = "api_key="+apiProperties.getKey();
        String language = "language=ko";
        String apiAndLanguage = api+"&"+language ;
        return apiAndLanguage;
    }



    //장르 insert
    public void theMovieGetGenres() throws Exception{
        String apiAndLanguage = getapi();
        List<Genre> genreList = new ArrayList<>();
        JSONObject genreResult = webClient
                .get()
                .uri("genre/movie/list?"+apiAndLanguage)// 인기순 영화 리스트 가져오기
                .retrieve()
                .bodyToMono(JSONObject.class)
                .block();
        JSONArray tmgGenreList =  genreResult.getJSONArray("genres");
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
    }

    //영화정보 insert
    public void theMovieGetMovie() throws Exception{
        String apiAndLanguage = getapi();
        List<Movie> movieList = new ArrayList<>();
        String imgBaseUrl = "https://image.tmdb.org/t/p/original/";
        int page = 10;// 가져올 데이터 페이지 설정
        //영화 목록정보
        for(int i = 1; i<=page; i++){
            JSONObject movieListResult =
                    webClient
                    .get().uri("movie/popular?"+apiAndLanguage+"&page="+i)// 인기순 영화 리스트 가져오기
                    .retrieve()
                    .bodyToMono(JSONObject.class)
                    .block();
            JSONArray movieJsonArr = movieListResult.getJSONArray("results");
            for (int j = 0; j<movieJsonArr.size(); j++){
                String movieId = movieJsonArr.getJSONObject(j).get("id").toString();
                TheMovie theMovieData = movieJsonArr.getObject(j,TheMovie.class);
                JSONObject movieDetail =
                        webClient
                                .get()
                                .uri("movie/"+movieId+"?"+apiAndLanguage+"&append_to_response=release_dates")// 인기순 영화 리스트 가져오기
                                .retrieve()
                                .bodyToMono(JSONObject.class)
                                .block();
                //무비 상세정보
                JSONArray ratingList = movieDetail
                        .getJSONObject("release_dates")
                        .getJSONArray("results");
                theMovieData.setRuntime(movieDetail.get("runtime").toString());
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
                                        movieDetail
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
            }
        }
    }
    public void theMovieGetPeople() throws Exception{
        String apiAndLanguage = getapi();
        List<Movie> movieList = movieRepository.findAll();

        for (Movie movie : movieList) {
            String apiId = movie.getApiId();

            JSONObject peopleListResult = webClient
                    .get()
                    .uri("https://api.themoviedb.org/3/movie/"+apiId+"/credits?"+apiAndLanguage)
                    .retrieve()
                    .bodyToMono(JSONObject.class)
                    .block();
            JSONArray castList = peopleListResult.getJSONArray("cast");
            JSONArray crewList = peopleListResult.getJSONArray("crew");
            for(int i=0; i<castList.size(); i++){
                String peopleApiId = castList.getJSONObject(i).get("id").toString();
                if(castList.getJSONObject(i).get("known_for_department") != null &&
                        castList.getJSONObject(i).get("known_for_department").toString().equals("Acting")){
                    if(i<5){
                        String type = "2";
                        JSONObject peopleInfo = webClient
                                .get()
                                .uri("https://api.themoviedb.org/3/person/"+peopleApiId+"?"+apiAndLanguage)
                                .retrieve()
                                .bodyToMono(JSONObject.class)
                                .block();
                        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-DD");
                        Date birth = null;
                        String place[];
                        String nationality = null;
                        String name = peopleInfo.get("name").toString();
                        try {

                            if(peopleInfo.get("birthday")!=null && !peopleInfo.get("birthday").toString().equals("")) {
                                birth = format.parse(peopleInfo.get("birthday").toString());
                            }
                            if(peopleInfo.get("place_of_birth")!=null){
                                place = peopleInfo.get("place_of_birth").toString().split(", ");
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
                    }
                }
            }
            int directorCnt = 0;
            for(int i=0; i<crewList.size(); i++) {
                if(directorCnt!=0){
                    continue;
                }
                String peopleApiId = crewList.getJSONObject(i).get("id").toString();
                if (crewList.getJSONObject(i).get("department") != null &&
                        crewList.getJSONObject(i).get("department").toString().equals("Directing")) {
                    String type = "1";
                    JSONObject peopleInfo = webClient
                            .get()
                            .uri("https://api.themoviedb.org/3/person/" + peopleApiId + "?" + apiAndLanguage)
                            .retrieve()
                            .bodyToMono(JSONObject.class)
                            .block();
                    SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-DD");
                    Date birth = null;
                    String place[];
                    String nationality = null;
                    String name = peopleInfo.get("name").toString();
                    try {
                        if (peopleInfo.get("birthday") != null && !peopleInfo.get("birthday").toString().equals("")) {
                            birth = format.parse(peopleInfo.get("birthday").toString());
                        }
                        if (peopleInfo.get("place_of_birth") != null) {
                            place = peopleInfo.get("place_of_birth").toString().split(", ");
                            nationality = place[place.length - 1];
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    boolean peopleDup = peopleRepository.existsByApiId(peopleApiId);
                    if (!peopleDup) {
                        People people = People.builder()
                                .name(name)
                                .type(type)
                                .birth(birth)
                                .nationality(nationality)
                                .apiId(peopleApiId)
                                .build();
                        peopleRepository.save(people);
                        directorCnt++;
                    }
                }
            }
        }
    }
    // 출연작품
    public void theMovieFilmography() throws Exception{
        String apiAndLanguage = getapi();
        List<People> peopleList = peopleRepository.findAll();
        for (People people: peopleList) {
            String peopleApiKey = people.getApiId();
            JSONObject result = webClient
                    .get()
                    .uri("person/"+peopleApiKey+"/movie_credits?"+apiAndLanguage)
                    .retrieve()
                    .bodyToMono(JSONObject.class)
                    .block();
            List<Filmography> saveFilmographyList = new ArrayList<>();
            JSONArray castFilmographyList = result.getJSONArray("cast");
            JSONArray crewFilmographyList = result.getJSONArray("crew");
            for(int i = 0; i<castFilmographyList.size(); i++){
                String movieTitle = castFilmographyList.getJSONObject(i).get("title").toString();
                Optional<Movie> movieInfo = movieRepository.findByTitle(movieTitle);
                if(movieInfo.isPresent()){
                    Optional<Filmography> optFilmography =
                            filmographyRepository.findByMovieAndPeople(movieInfo.get(), people);

                    if(!optFilmography.isPresent()){
                        Filmography filmography = Filmography.builder()
                                .movie(movieInfo.get())
                                .people(people)
                                .build();
                        filmographyRepository.save(filmography);
                        saveFilmographyList.add(filmography);
                    }
                }else{
                    continue;
                }
            }
            for(int i = 0; i<crewFilmographyList.size(); i++){
                String movieTitle = crewFilmographyList.getJSONObject(i).get("title").toString();
                Optional<Movie> movieInfo = movieRepository.findByTitle(movieTitle);
                if(movieInfo.isPresent()){
                    Optional<Filmography> optFilmography =
                            filmographyRepository.findByMovieAndPeople(movieInfo.get(), people);
                    if(!optFilmography.isPresent()){
                        Filmography filmography = Filmography.builder()
                                .movie(movieInfo.get())
                                .people(people)
                                .build();
                        filmographyRepository.save(filmography);
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
        }

    }
    // 예고편
    public void theMovieVideo() throws Exception{
        String apiAndLanguage = getapi();
        List<Trailer> saveTrailerList = new ArrayList<>();
        List<Movie> movieList = movieRepository.findAll();
        for (Movie movie: movieList) {
            String movieApiKey = movie.getApiId();
            JSONObject result = webClient
                    .get()
                    .uri("movie/"+movieApiKey+"/videos?"+apiAndLanguage)
                    .retrieve()
                    .bodyToMono(JSONObject.class)
                    .block();
            JSONArray trailerList = result.getJSONArray("results");
            for(int i = 0; i<trailerList.size(); i++){
                String key = trailerList.getJSONObject(i).get("key").toString();
                String trLocation = "https://www.youtube.com/embed/"+key+"?autoplay=0";
                String imgLocation = "https://img.youtube.com/vi/"+key+"/hqdefault.jpg";
                Optional<Trailer> trailerInfo = trailerRepository.findByTrLocation(trLocation);
                if(!trailerInfo.isPresent()){
                    Trailer trailer = Trailer.builder()
                            .movie(movie)
                            .trLocation(trLocation)
                            .imgLocation(imgLocation)
                            .build();
                    saveTrailerList.add(trailer);
                }else{
                    continue;
                }
            }
            if(saveTrailerList.size()>0){
                log.info("insertTrailerList :{}",saveTrailerList);
                trailerRepository.saveAll(saveTrailerList);
            }
        }

    }

}
