import "./css/movie_filmography.css";
import { useEffect, useState } from "react";
import MovieFilmographyItem from "./movie_filmography_item";
const MoveFilmographyPopUp = (props) => {
  const handleClosePop = props.handleClosePop;
  const movieServer = props.movieServer;
  const people = props.people;
  const [filmographys, setFilmographys] = useState([]);
  const handleGetFilmoGraphy = (peId) => {
    movieServer.filmography(peId).then((response) => {
      console.log(response);
      setFilmographys(response.data);
    });
  };
  useEffect(() => {
    handleGetFilmoGraphy(people.peId);
  }, []);
  return (
    <>
      <div className="modal-filmo" onClick={() => handleClosePop()}>
        <div className="modal-filmo-content">
          <div className="modal-filmo-head">
            <label
              className="modal-filmo-head-item"
              onClick={() => handleClosePop()}
            >
              ✖
            </label>
          </div>
          <div className="modal-filmo-people">
            <div>{people.name}</div>
            <div>필모그래피</div>
          </div>
          <div className="modal-filmo-item">
            {filmographys.map((info) => (
              <div key={info.pgId}>
                <MovieFilmographyItem filmography={info} />
              </div>
            ))}
          </div>
        </div>
      </div>
    </>
  );
};

export default MoveFilmographyPopUp;
