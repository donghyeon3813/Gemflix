import React, { useEffect, useState } from "react";
import Pagenation from "../pagenation/pagenation";
import MovieReviewItem from "./movie_review_item";

const MovieReviewList = (props) => {
  const movieServer = props.movieServer;
  const mvId = props.mvId;

  const [reviewList, setReviewList] = useState([]);
  const [limit, setLimit] = useState(10);
  const [page, setPage] = useState(0);
  const [totalReview, setTotalReview] = useState(0);

  function handleGetReviewList(page) {
    const data = { page: page, size: limit, mvId: mvId };
    movieServer.reviews(data).then((response) => {
      console.log(response.data.content);
      setReviewList(response.data.content);
      setTotalReview(response.data.totalElements);
    });
  }
  useEffect(() => {
    handleGetReviewList(0);
  }, [page]);
  return (
    <>
      <br />
      <div>총{totalReview}건</div>
      <br />
      <hr />
      {reviewList.map((info) => (
        <div key={info.rvId}>
          <MovieReviewItem reviewInfo={info} movieServer={movieServer} />
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
