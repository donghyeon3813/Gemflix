import { useEffect, useState } from "react";
import MoveFilmographyPopUp from "./movie_filmography_popUp";

const MovieDetail = (prorps) => {
  const movieDetailInfo = prorps.movieDetailInfo;
  const movieServer = prorps.movieServer;
  const [actors, setActors] = useState([]);
  const [directors, setDirectors] = useState([]);
  const [people, setPeople] = useState({});
  const [filmographyPopup, setFilmographyPopup] = useState(false);
  const handlePeopleSet = () => {
    let actorArr = [];
    let directorArr = [];
    console.log(movieDetailInfo.peopleList);
    movieDetailInfo.peopleList.map((info) => {
      info.type === "1"
        ? directorArr.push({ name: info.name, peId: info.peId })
        : actorArr.push({ name: info.name, peId: info.peId });
    });

    setActors([...actorArr]);
    setDirectors([...directorArr]);
  };

  const handleSetPop = (peId, name) => {
    setPeople({ peId: peId, name: name });
    setFilmographyPopup(true);
  };

  const handleClosePop = () => {
    setFilmographyPopup(false);
  };

  useEffect(() => {
    handlePeopleSet();
  }, []);
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
          <div style={white}>평점:{movieDetailInfo.score}</div>
          <div style={white}>
            출연{" "}
            {actors.map((info, index) => (
              <span
                key={info.peId}
                onClick={() => handleSetPop(info.peId, info.name)}
              >
                {info.name}{" "}
              </span>
            ))}
          </div>
          <div style={white}>
            감독{" "}
            {directors.map((info) => (
              <span
                key={info.peId}
                onClick={() => handleSetPop(info.peId, info.name)}
              >
                {info.name}{" "}
              </span>
            ))}
          </div>
        </div>
        <div style={darkBox}></div>
      </div>
      <p>{movieDetailInfo.content}</p>

      {filmographyPopup && (
        <MoveFilmographyPopUp
          people={people}
          handleClosePop={handleClosePop}
          movieServer={movieServer}
        />
      )}
    </>
  );
};
export default MovieDetail;
