import MoveTrailer from "./movie_trailer";
const MovieDetail = (prorps) => {
  const movieDetailInfo = prorps.movieDetailInfo;

  // css 임시 설정
  const movieBack = {
    height: "100%",
    width: "60%",
    backgroundImage:
      "linear-gradient( rgba(0, 0, 0, 0.6), rgba(0, 0, 0, 0.6) ),url(" +
      movieDetailInfo.backImgUrl +
      ")",
    backgroundRepeat: "no - repeat",
    backgroundSize: "cover",

    opacity: "0.7",
  };

  const movieImgBox = {
    display: "flex",
    height: "600px",
    width: "100%",
  };

  const darkBox = {
    backgroundColor: "#0f0f0f",
    width: "20%",
    height: "100%",
  };

  const white = {
    color: "white",
  };
  return (
    <>
      <div style={movieImgBox}>
        <div style={darkBox}></div>
        <div style={movieBack}>
          <div style={white}>{movieDetailInfo.title}</div>
          <div style={white}>평점</div>
        </div>
        <div style={darkBox}></div>
      </div>
      <p>{movieDetailInfo.content}</p>
      <MoveTrailer trailerList={movieDetailInfo.trailerList} />
      <button>리뷰</button>
    </>
  );
};
export default MovieDetail;
