const MovieReviewItem = (props) => {
  const reviewList = props.reviewList;
  return (
    <>
      {reviewList.map((info) => (
        <div>
          <div>{info.id}</div>
          <div>{info.content}</div>
        </div>
      ))}
    </>
  );
};

export default MovieReviewItem;
