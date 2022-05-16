import { useState } from "react";
import { useSelector, shallowEqual } from "react-redux";
const MovieReviewItem = (props) => {
  const movieServer = props.movieServer;
  const handleGetReviewList = props.handleGetReviewList;
  const handlePageMovieList = props.handlePageMovieList;
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
      content: review,
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
        alert("삭제되었습니다.");
        handleGetReviewList(0);
        handlePageMovieList();
      }
    });
  };

  return (
    <div className="review-item-box">
      <div>
        <span className="review-id">{reviewInfo.id}</span>

        <span>{reviewInfo.score}점</span>
        {user.memberId === reviewInfo.id ? (
          <>
            {reviewStatus === false ? (
              <button
                className="review-btn"
                onClick={() => handleReviewStatus()}
              >
                수정
              </button>
            ) : (
              <button
                className="review-btn"
                onClick={() => handleReviewModify()}
              >
                저장
              </button>
            )}
            {reviewStatus === false ? (
              <button
                className="review-btn"
                onClick={() => handleReviewDelete()}
              >
                삭제
              </button>
            ) : (
              <button
                className="review-btn"
                onClick={() => setReviewStatus(false)}
              >
                취소
              </button>
            )}
          </>
        ) : null}
      </div>
      <div>
        {reviewStatus === false ? (
          <p>{reviewInfo.content}</p>
        ) : (
          <textarea
            className="review-upd-content"
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
