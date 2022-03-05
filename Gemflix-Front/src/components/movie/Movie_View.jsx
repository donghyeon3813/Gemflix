import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router";
import MovieDetail from "./movie_detail";
import MovieService from "../../service/movie_service";
import { useSelector, shallowEqual } from "react-redux";
import MoveTrailer from "./movie_trailer";

const movieService = new MovieService();

const MovieView = () => {
  const user = useSelector((store) => store.userReducer, shallowEqual);
  const location = useLocation();
  const navigate = useNavigate();
  const [movieInfo, setMovieInfo] = useState(null);

  const handlePageMovieList = () => {
    if (location.state === null) {
      alert("조회된 정보가 없습니다.");
      navigate("/movies");
    } else {
      setMovieInfo(location.state.movieInfo);
    }
  };

  const handleRegisterReview = () => {
    if (user.token === null) {
      alert("로그인이 필요한 기능입니다.");
      return;
    }
    movieService.reviewRegister("임시", user.token).then((response) => {
      console.log(response);
    });
  };

  useEffect(() => {
    handlePageMovieList();
  });
  return (
    <>
      {movieInfo !== null ? (
        <>
          <MovieDetail movieDetailInfo={movieInfo} />

          <MoveTrailer trailerList={movieInfo.trailerList} />
          <button onClick={() => handleRegisterReview()}>리뷰</button>
        </>
      ) : null}
    </>
  );
};

export default MovieView;
