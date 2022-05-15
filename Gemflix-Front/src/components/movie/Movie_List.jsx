import React, { useEffect, useState } from "react";
import axios from "axios";
import Pagenation from "../pagenation/pagenation";
import MovieItem from "./movie_item";
import MovieSearch from "./movie_search";
import MovieDetail from "./movie_detail";
import { useNavigate } from "react-router";
import "./css/movie_list.css";

const MovieList = ({ movieServer }) => {
  const navigate = useNavigate();

  const [movieListInfo, setMovieListInfo] = useState([]);
  const [limit, setLimit] = useState(20);
  const [page, setPage] = useState(0);
  const [totalMovie, setTotalMovie] = useState(0);
  const [title, setTitle] = useState("");
  const [contentIndex, setContentIndex] = useState(0);

  function handleGetMovieList(page) {
    const data = { page: page, size: limit, title: title };
    movieServer.movies(data).then((response) => {
      setMovieListInfo(response.data.content);
      setTotalMovie(response.data.totalElements);
    });
  }
  const handleGetMovieDetail = (e, mvId, index) => {
    navigate("/movies/view", { state: { mvId: mvId } });
  };
  const handleDetailPosition = (index) => {
    let indexShare = index % 4;
    switch (indexShare) {
      case 0:
        setContentIndex(index + 4);
        break;
      case 1:
        setContentIndex(index + 3);
        break;
      case 2:
        setContentIndex(index + 2);
        break;
      case 3:
        setContentIndex(index + 1);
        break;
    }
  };

  useEffect(() => {
    handleGetMovieList(page);
  }, [page, title]);

  const movieDetailBox = {
    width: "1900px",
  };

  const box = {};

  return (
    <>
      <MovieSearch setTitle={setTitle} />
      <div className="movie-list-box">
        {movieListInfo.map((info, index) => (
          <React.Fragment key={info.mvId}>
            <div
              className="movie-item-group"
              onClick={(e) => handleGetMovieDetail(e, info.mvId, index)}
            >
              <MovieItem info={info} />
            </div>
            {/* {movieDetailInfo && index + 1 === contentIndex ? ( 상세정보 페이지 이동으로 변경
              <div style={movieDetailBox}>
                <MovieDetail style={box} movieDetailInfo={movieDetailInfo} />
              </div>
            ) : null} */}
          </React.Fragment>
        ))}
      </div>
      <Pagenation
        total={totalMovie}
        limit={limit}
        page={page}
        setPage={setPage}
      />
    </>
  );
};

export default MovieList;
