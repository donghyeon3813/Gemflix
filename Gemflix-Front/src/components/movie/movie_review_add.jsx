import React, { useState } from "react";
const MovieReviewAdd = (props) => {
  const handleRegisterReview = props.handleRegisterReview;
  const handleSetReview = props.handleSetReview;
  const reviewInfo = props.reviewInfo;
  return (
    <>
      <div className="review-add-group">
        <select
          className="review-score"
          name="score"
          value={reviewInfo.score}
          onChange={(e) => handleSetReview(e)}
        >
          <option value={1}>1점</option>
          <option value={2}>2점</option>
          <option value={3}>3점</option>
          <option value={4}>4점</option>
          <option value={5}>5점</option>
          <option value={6}>6점</option>
          <option value={7}>7점</option>
          <option value={8}>8점</option>
          <option value={9}>9점</option>
          <option value={10}>10점</option>
        </select>
        <textarea
          className="review-content"
          maxLength={1000}
          name="review"
          onChange={(e) => handleSetReview(e)}
          value={reviewInfo.review}
        ></textarea>
        <button
          className="review-add-btn"
          onClick={(e) => handleRegisterReview(reviewInfo)}
        >
          등록
        </button>
      </div>
    </>
  );
};

export default MovieReviewAdd;
