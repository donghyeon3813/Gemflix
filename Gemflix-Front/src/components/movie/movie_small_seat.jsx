import { useEffect, useState } from "react";
const MovieSmaillSeat = (props) => {
  const seatInfo = props.seatInfo;
  let backColor = "";
  if (seatInfo.tkId === null) {
    backColor = "#E8E8E8";
  } else {
    backColor = "#000";
  }
  const seatStyle = {
    display: "inline-block",
    width: "5px",
    height: "5px",
    position: "absolute",
    left: seatInfo.seCol * 10 + "px",
    backgroundColor: backColor,
  };

  return <div className="seat" style={seatStyle}></div>;
};
export default MovieSmaillSeat;
