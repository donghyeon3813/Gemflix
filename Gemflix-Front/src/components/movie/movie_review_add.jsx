const MovieReviewAdd = (props) => {
  const handleSetReview = props.handleSetReview;
  const handleRegisterReview = props.handleRegisterReview;

  return (
    <>
      <div>
        <input name="score" onChange={(e) => handleSetReview(e)} />
        <input name="review" onChange={(e) => handleSetReview(e)} />
        <button onClick={(e) => handleRegisterReview()}>등록</button>
      </div>
    </>
  );
};

export default MovieReviewAdd;
