import React, { useEffect, useState } from "react";
import Pagenation from "../pagenation/pagenation";
import MovieReviewItem from "./movie_review_item";
const MovieReviewList = (props) => {
  const movieService = props.movieService;
  const mvId = props.mvId;

  const [reviewList, setReviewList] = useState([]);
  const [limit, setLimit] = useState(10);
  const [page, setPage] = useState(0);
  const [totalReview, setTotalReview] = useState(0);
  function handleGetReviewList(page) {
    const data = { page: page, size: limit, mvId: mvId };
    movieService.reviews(data).then((response) => {
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
      <div>총{totalReview}건</div>
      <MovieReviewItem reviewList={reviewList} />
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
