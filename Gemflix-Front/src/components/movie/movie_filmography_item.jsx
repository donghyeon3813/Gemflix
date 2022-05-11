import axios from "axios";
const MovieFilmographyItem = (props) => {
  const filmography = props.filmography;
  const movieItemStyle = {
    width: "200px",
  };
  const imgStyle = {
    width: "200px",
    height: "280px",
    margin: "auto",
  };

  return (
    <div className="modal-item-style">
      <div className="modal-item-group">
        <img
          className="modal-item-image"
          src={filmography.imgUrl}
          style={imgStyle}
        ></img>
        <div>{filmography.title}</div>
      </div>
    </div>
  );
};

export default MovieFilmographyItem;
