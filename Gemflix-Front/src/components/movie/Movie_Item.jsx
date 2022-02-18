import axios from "axios";
const MovieItem = (props) => {
  const movieInfo = props.info;
  const movieItemStyle = {
    width: "400px",
  };
  const imgStyle = {
    width: "200px",
    height: "280px",
  };

  return (
    <div key={movieInfo.mvId} style={movieItemStyle}>
      <img src={movieInfo.imgUrl} style={imgStyle}></img>
      <div>{movieInfo.title}</div>
      <div>{movieInfo.openDt}</div>
    </div>
  );
};

export default MovieItem;
