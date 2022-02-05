import React, { useEffect, useState } from "react";
import axios from "axios";
import Pagenation from "../pagenation/pagenation";
import MovieItem from "./movieItem";
import MovieSearch from "./movieSearch";

const MovieList = () => {
  const [movieListInfo, setMovieListInfo] = useState([]);
  const [limit, setLimit] = useState(20);
  const [page, setPage] = useState(0);
  const [totalMovie, setTotalMovie] = useState(0);
  const [title, setTitle] = useState("");

  function getMovieList(page) {
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
        console.log("성공");
      })
      .catch(function (error) {
        console.log("실패");
        console.log(error);
      });
  }
  useEffect(() => {
    getMovieList(page);
  }, [page, title]);

  const listBox = {
    width: "1900px",
    display: "flex",
    flexWrap: "wrap",
  };

  return (
    <>
      <MovieSearch setTitle={setTitle} />
      <div style={listBox}>
        {movieListInfo.map((info) => (
          <MovieItem key={info.mvId} info={info} />
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
