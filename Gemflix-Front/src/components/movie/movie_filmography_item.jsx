import axios from "axios";
const MovieFilmographyItem = (props) => {
  const filmography = props.filmography;
  const movieItemStyle = {
    width: "300px",
    margin: "auto",
  };
  const imgStyle = {
    width: "200px",
    height: "280px",
  };

  return (
    <div style={movieItemStyle}>
      <img src={filmography.imgUrl} style={imgStyle}></img>
      <div>{filmography.title}</div>
    </div>
  );
};

export default MovieFilmographyItem;
