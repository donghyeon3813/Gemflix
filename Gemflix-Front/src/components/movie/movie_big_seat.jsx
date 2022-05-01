import { useEffect, useState } from "react";
const MovieBigSeat = (props) => {
  const seatInfo = props.seatInfo;
  const handleSetSelectedSeat = props.handleSetSelectedSeat;
  const selectedSeat = props.selectedSeat;
  const seatColorStatus = props.seatColorStatus;
  let backColor = "";
  if (selectedSeat.indexOf(seatInfo.seId) !== -1) {
    backColor = "red";
  } else if (seatColorStatus === true) {
    backColor = "blue";
  } else if (seatColorStatus === false) {
    backColor = "#E8E8E8";
  }
  if (seatInfo.tkId !== null) {
    backColor = "#000";
  }
  const seatStyle = {
    display: "inline-block",
    width: "30px",
    height: "20px",
    position: "absolute",
    left: seatInfo.seCol * 60 + "px",
    backgroundColor: backColor,
  };

  return (
    <div
      className="seat"
      style={seatStyle}
      onClick={() => handleSetSelectedSeat(seatInfo.seId, seatInfo.tkId)}
    >
      {seatInfo.seCol}
    </div>
  );
};
export default MovieBigSeat;
