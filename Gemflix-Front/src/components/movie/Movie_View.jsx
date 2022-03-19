import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router";
import MovieDetail from "./movie_detail";
import MovieService from "../../service/movie_service";
import { useSelector, shallowEqual } from "react-redux";
import MoveTrailer from "./movie_trailer";
import MovieReviewAdd from "./movie_review_add";
import MovieReviewList from "./movie_review_list";
const movieService = new MovieService();

const MovieView = () => {
  const user = useSelector((store) => store.userReducer, shallowEqual);
  const location = useLocation();
  const navigate = useNavigate();
  const [movieInfo, setMovieInfo] = useState(null);
  const [reviewInfo, setReview] = useState({ score: 0, review: "" });

  const handlePageMovieList = () => {
    if (location.state === null) {
      alert("조회된 정보가 없습니다.");
      navigate("/movies");
    } else {
      handleGetMovieDetail(location.state.mvId);
    }
  };
  const handleGetMovieDetail = (mvId) => {
    const data = {
      mvId: mvId,
    };
    movieService.movieDetail(data).then((response) => {
      setMovieInfo(response.data);
    });
  };

  const handleSetReview = (e) => {
    const { name, value } = e.target;
    setReview({ ...reviewInfo, [name]: value });
  };

  const handleRegisterReview = () => {
    if (user.token === null) {
      alert("로그인이 필요한 기능입니다.");
      return;
    }
    if (reviewInfo.review === "") {
      alert("관람평을 입력해주세요");
      return;
    }
    if (reviewInfo.score === 0) {
      alert("평점을 입력해주세요");
      return;
    }
    const data = {
      mvId: movieInfo.mvId,
      comment: reviewInfo.review,
      score: reviewInfo.score,
    };
    movieService.reviewRegister(data, user.token).then((response) => {
      if (response.code !== "200") {
        alert(response.message);
      }
    });
  };

  useEffect(() => {
    handlePageMovieList();
  }, []);
  return (
    <>
      {movieInfo !== null ? (
        <>
          <MovieDetail movieDetailInfo={movieInfo} />

          <MoveTrailer trailerList={movieInfo.trailerList} />
          <MovieReviewAdd
            handleRegisterReview={handleRegisterReview}
            handleSetReview={handleSetReview}
          />
          <MovieReviewList movieService={movieService} mvId={movieInfo.mvId} />
        </>
      ) : null}
    </>
  );
};

export default MovieView;
