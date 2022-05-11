import axios from "axios";
const MovieItem = (props) => {
  const movieInfo = props.info;
  return (
    <div key={movieInfo.mvId} className="movie-item">
      <img src={movieInfo.imgUrl} className="movie-poster"></img>
      <div>{movieInfo.title}</div>
      <div>{movieInfo.openDt}</div>
    </div>
  );
};

export default MovieItem;
