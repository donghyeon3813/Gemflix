import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router";
import MovieDetail from "./movie_detail";
import MovieService from "../../service/movie_service";
import { useSelector, shallowEqual } from "react-redux";
import MoveTrailer from "./movie_trailer";
import MovieReviewAdd from "./movie_review_add";
import MovieReviewList from "./movie_review_list";
import "./css/movie_view.css";
import "./css/movie_review.css";

const MovieView = ({ movieServer }) => {
  const user = useSelector((store) => store.userReducer, shallowEqual);
  const location = useLocation();
  const navigate = useNavigate();
  const [movieInfo, setMovieInfo] = useState(null);
  const [tebMenu, setTebMenu] = useState([
    { name: "예고편", value: 1, class: "selected-teb-item" },
    { name: "평점 및 관람평", value: 2, class: "" },
  ]);
  const [selectedTeb, setSelectedTeb] = useState(1);

  const handlePageMovieList = () => {
    if (location.state === null) {
      alert("조회된 정보가 없습니다.");
      navigate("/movies");
    } else {
      handleGetMovieDetail(location.state.mvId);
    }
  };

  // 영화정보 조회
  const handleGetMovieDetail = (mvId) => {
    const data = {
      mvId: mvId,
    };
    movieServer.movieDetail(data).then((response) => {
      setMovieInfo(response.data);
    });
  };

  // 리뷰 등록
  const handleRegisterReview = (reviewInfo) => {
    if (user.token === null) {
      alert("로그인이 필요한 기능입니다.");
      navigate("/login");
      return;
    }
    let review = reviewInfo.review;
    let score = reviewInfo.score;
    if (review === "") {
      alert("관람평을 입력해주세요");
      return;
    }
    if (score === 0 || score === "") {
      alert("평점을 입력해주세요");
      return;
    }
    const integerRegex = new RegExp("^([0-9]{1}([0]{1})?(.[0-9]{1})?)$");

    if (!integerRegex.test(score) || score > 10) {
      alert("평점은 10 이하 정수 또는 소수점 첫째자리까지 입력 가능합니다.");
      return;
    }

    const data = {
      mvId: movieInfo.mvId,
      content: review,
      score: score,
    };
    movieServer.reviewRegister(data, user.token).then((response) => {
      if (response.code !== "200") {
        alert(response.message);
      }
    });
  };
  const handleTebMenu = (value) => {
    const defTebMenu = [...tebMenu];
    if (value === 1) {
      defTebMenu[0]["class"] = "selected-teb-item";
      defTebMenu[1]["class"] = "";
    } else {
      defTebMenu[1]["class"] = "selected-teb-item";
      defTebMenu[0]["class"] = "";
    }
    setTebMenu(defTebMenu);
    setSelectedTeb(value);
  };

  useEffect(() => {
    handlePageMovieList();
  }, []);
  return (
    <>
      {movieInfo !== null ? (
        <>
          <MovieDetail movieDetailInfo={movieInfo} movieServer={movieServer} />
          <ul className="teb-menu">
            {tebMenu.map((info) => (
              <li
                key={info.value}
                className={" teb-item " + info.class}
                onClick={() => handleTebMenu(info.value)}
              >
                {info.name}
              </li>
            ))}
          </ul>
          {selectedTeb === 1 ? (
            <MoveTrailer trailerList={movieInfo.trailerList} />
          ) : (
            <div className="review-box">
              <MovieReviewAdd handleRegisterReview={handleRegisterReview} />
              <MovieReviewList
                movieServer={movieServer}
                mvId={movieInfo.mvId}
              />
            </div>
          )}
        </>
      ) : null}
    </>
  );
};

export default MovieView;
