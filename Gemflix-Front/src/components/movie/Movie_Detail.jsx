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
  // const movieBack = {
  //   height: "100%",
  //   width: "60%",
  //   backgroundImage:
  //     "linear-gradient( rgba(0, 0, 0, 0.6), rgba(0, 0, 0, 0.6) ),url(" +
  //     movieDetailInfo.backImgUrl +
  //     ")",
  //   backgroundRepeat: "no - repeat",
  //   backgroundSize: "cover",

  //   opacity: "0.7",
  // };

  // const movieImgBox = {
  //   display: "flex",
  //   height: "600px",
  //   width: "100%",
  // };

  // const darkBox = {
  //   backgroundColor: "#0f0f0f",
  //   width: "20%",
  //   height: "100%",
  // };

  // const white = {
  //   color: "white",
  // };
  return (
    <>
      <div className="movie-detail-group">
        <div className="movie-info">
          <img className="movie-detail-poster" src={movieDetailInfo.imgUrl} />
          <div className="movie-info-text-group">
            <h2 className="movie-info-title">{movieDetailInfo.title}</h2>
            <div className="movie-margin-text">
              장르{" "}
              <span className="movie-text-gray">{movieDetailInfo.grNm}</span>
            </div>
            <div className="movie-margin-text">
              개봉{" "}
              <span className="movie-text-gray">{movieDetailInfo.openDt}</span>{" "}
              |{" "}
              <span className="movie-text-gray">
                {movieDetailInfo.extent} 분
              </span>
            </div>
            <div className="movie-margin-text">
              평점{" "}
              <span className="movie-text-gray">{movieDetailInfo.score}</span>
            </div>
            <div className="movie-margin-text">
              출연{" "}
              {actors.map((info, index) => (
                <span
                  className="movie-info-people movie-text-gray"
                  key={info.peId}
                  onClick={() => handleSetPop(info.peId, info.name)}
                >
                  {info.name}{" "}
                </span>
              ))}
            </div>
            <div className="movie-margin-text">
              감독{" "}
              {directors.map((info) => (
                <span
                  className="movie-info-people movie-text-gray"
                  key={info.peId}
                  onClick={() => handleSetPop(info.peId, info.name)}
                >
                  {info.name}{" "}
                </span>
              ))}
            </div>
          </div>
        </div>
      </div>
      <p className="movie-info-content">{movieDetailInfo.content}</p>

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
