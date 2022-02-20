import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router";
import MovieDetail from "./movie_detail";

const MovieView = () => {
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
  useEffect(() => {
    handlePageMovieList();
  });
  return (
    <>
      {movieInfo !== null ? <MovieDetail movieDetailInfo={movieInfo} /> : null}
    </>
  );
};

export default MovieView;
