import { useState } from "react";
import { useSelector, shallowEqual } from "react-redux";
const MovieReviewItem = (props) => {
  const movieServer = props.movieServer;
  const [reviewInfo, setReviewInfo] = useState(props.reviewInfo);
  const user = useSelector((store) => store.userReducer, shallowEqual);
  const [reviewStatus, setReviewStatus] = useState(false);

  const handleSetReview = (e) => {
    const { name, value } = e.target;
    setReviewInfo({ ...reviewInfo, [name]: value });
  };

  const handleReviewStatus = () => {
    reviewStatus === false ? setReviewStatus(true) : setReviewStatus(false);
  };
  const handleReviewModify = () => {
    let review = reviewInfo.content;
    let rvId = reviewInfo.rvId;
    if (review === "") {
      alert("관람평을 입력해주세요");
      return;
    }
    const data = {
      comment: review,
      rvId: rvId,
    };
    movieServer.reviewModify(data, user.token).then((response) => {
      if (response.code !== "200") {
        handleReviewStatus();
      }
    });
  };
  const handleReviewDelete = () => {
    if (!window.confirm("관람평을 삭제하시겠습니까?")) {
      return;
    }
    let rvId = reviewInfo.rvId;

    movieServer.reviewDelete(rvId, user.token).then((response) => {
      if (response.code !== "200") {
        alert(response.message);
      }
    });
  };

  return (
    <div className="review-item-box">
      <div>
        <span className="review-id">{reviewInfo.id}</span>

        <span>{reviewInfo.score}</span>
        {user.memberId === reviewInfo.id ? (
          <>
            {reviewStatus === false ? (
              <button onClick={() => handleReviewStatus()}>수정</button>
            ) : (
              <button onClick={() => handleReviewModify()}>저장</button>
            )}

            <button onClick={() => handleReviewDelete()}>삭제</button>
          </>
        ) : null}
      </div>
      <div>
        {reviewStatus === false ? (
          <span>{reviewInfo.content}</span>
        ) : (
          <input
            type="text"
            name="content"
            value={reviewInfo.content}
            onChange={(e) => handleSetReview(e)}
          />
        )}
      </div>

      <div>{reviewInfo.regDate}</div>
    </div>
  );
};

export default MovieReviewItem;
