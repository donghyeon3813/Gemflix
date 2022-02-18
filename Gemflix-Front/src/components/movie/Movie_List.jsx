import React, { useEffect, useState } from "react";
import axios from "axios";
import Pagenation from "../pagenation/pagenation";
import MovieItem from "./Movie_Item";
import MovieSearch from "./Movie_Search";
import MovieDetail from "./Movie_Detail";
import { useNavigate } from "react-router";

const MovieList = () => {
  const navigate = useNavigate();

  const [movieListInfo, setMovieListInfo] = useState([]);
  const [limit, setLimit] = useState(20);
  const [page, setPage] = useState(0);
  const [totalMovie, setTotalMovie] = useState(0);
  const [title, setTitle] = useState("");
  const [contentIndex, setContentIndex] = useState(0);
  const [movieDetailInfo, setMovieDetailInfo] = useState(null);

  function handleGetMovieList(page) {
    console.log(page);
    const url = "http://localhost:9090/movie/list";
    axios
      .get(url, {
        params: {
          page: page,
          size: limit,
          title: title,
        },
      })
      .then(function (response) {
        console.log(response.data.content);
        setMovieListInfo(response.data.content);
        setTotalMovie(response.data.totalElements);
        setMovieDetailInfo(null);
        console.log("성공");
      })
      .catch(function (error) {
        console.log("실패");
        console.log(error);
      });
  }
  const handleGetMovieDetail = (e, mvId, index) => {
    e.preventDefault();
    const url = "http://localhost:9090/movie/details";
    axios
      .get(url, {
        params: {
          mvId: mvId,
        },
      })
      .then(function (response) {
        console.log(response.data);
        setMovieDetailInfo(response.data);
        navigate("/movies/view", { state: { movieInfo: response.data } });

        console.log("성공");
        // handleDetailPosition(index);
      })
      .catch(function (error) {
        console.log("실패");
        console.log(error);
      });
  };
  const handleDetailPosition = (index) => {
    console.log(index);
    console.log(index % 4);
    let indexShare = index % 4;
    switch (indexShare) {
      case 0:
        setContentIndex(index + 4);
        console.log(index + 4);
        break;
      case 1:
        setContentIndex(index + 3);
        console.log(index + 3);
        break;
      case 2:
        setContentIndex(index + 2);
        console.log(index + 2);
        break;
      case 3:
        setContentIndex(index + 1);
        console.log(index + 1);
        break;
    }
  };

  useEffect(() => {
    handleGetMovieList(page);
  }, [page, title]);

  const listBox = {
    width: "1900px",
    display: "flex",
    flexWrap: "wrap",
  };
  const movieDetailBox = {
    width: "1900px",
  };

  const box = {};

  return (
    <>
      <MovieSearch setTitle={setTitle} />
      <div style={listBox}>
        {movieListInfo.map((info, index) => (
          <React.Fragment key={info.mvId}>
            <div onClick={(e) => handleGetMovieDetail(e, info.mvId, index)}>
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
