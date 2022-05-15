import React, { useEffect, useState } from "react";
import Pagenation from "../pagenation/pagenation";
import MovieReviewItem from "./movie_review_item";

const MovieReviewList = (props) => {
  const movieServer = props.movieServer;
  const mvId = props.mvId;
  const movieInfo = props.movieInfo;
  const handlePageMovieList = props.handlePageMovieList;

  const [reviewList, setReviewList] = useState([]);
  const [limit, setLimit] = useState(10);
  const [page, setPage] = useState(0);
  const [totalReview, setTotalReview] = useState(0);

  function handleGetReviewList(page) {
    const data = { page: page, size: limit, mvId: mvId };
    movieServer.reviews(data).then((response) => {
      setReviewList(response.data.content);
      setTotalReview(response.data.totalElements);
    });
  }
  useEffect(() => {
    handleGetReviewList(0);
  }, [page, movieInfo]);
  return (
    <>
      <br />
      <div>총{totalReview}건</div>
      <br />
      <hr />
      {reviewList.map((info) => (
        <div key={info.rvId}>
          <MovieReviewItem
            handleGetReviewList={handleGetReviewList}
            reviewInfo={info}
            movieServer={movieServer}
            handlePageMovieList={handlePageMovieList}
          />
          <hr />
        </div>
      ))}

      <Pagenation
        total={totalReview}
        limit={limit}
        page={page}
        setPage={setPage}
      />
    </>
  );
};
export default MovieReviewList;
