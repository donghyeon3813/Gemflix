import React, { useState } from "react";
const MovieReviewAdd = (props) => {
  const handleRegisterReview = props.handleRegisterReview;
  const [reviewInfo, setReview] = useState({ score: "", review: "" });
  const handleSetReview = (e) => {
    const { name, value } = e.target;

    setReview({ ...reviewInfo, [name]: value });
  };

  return (
    <>
      <div>
        <input
          name="score"
          value={reviewInfo.score}
          onChange={(e) => handleSetReview(e)}
        />
        <input
          maxLength={1000}
          name="review"
          onChange={(e) => handleSetReview(e)}
        />
        <button onClick={(e) => handleRegisterReview(reviewInfo)}>등록</button>
      </div>
    </>
  );
};

export default MovieReviewAdd;
