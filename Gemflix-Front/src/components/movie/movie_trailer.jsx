import MoveTrailerItem from "./movie_trailer_Item";
import MoveTrailerPopUp from "./movie_trailer_popUp";
import React, { useEffect, useState } from "react";
const MoveTrailer = (props) => {
  const trailerList = props.trailerList;
  const [trailerPop, setTrailerPop] = useState(false);
  const [trLocation, setTrLocation] = useState("");

  const handleSetPop = (trLocation) => {
    setTrLocation(trLocation);
    setTrailerPop(true);
  };

  const carouselBox = {
    width: "1900px",
  };
  const carousel = {
    width: "80%",
    height: "300px",
    overflow: "hidden",
    display: "flex",
  };
  return (
    <>
      <div style={carouselBox}>
        <div style={carousel}>
          {trailerList.map((info) => (
            <MoveTrailerItem
              key={info.imgLocation}
              imgLocation={info.imgLocation}
              trLocation={info.trLocation}
              handleSetPop={handleSetPop}
            />
          ))}
        </div>
      </div>

      {trailerPop && <MoveTrailerPopUp trLocation={trLocation} />}
    </>
  );
};

export default MoveTrailer;
