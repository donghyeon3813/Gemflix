import React, { useEffect, useState } from "react";
const MoveTrailerItem = (props) => {
  const handleSetPop = props.handleSetPop;
  const imgLocation = props.imgLocation;
  const trLocation = props.trLocation;
  return (
    <>
      <img
        src={imgLocation}
        alt=""
        onClick={() => {
          handleSetPop(trLocation);
        }}
      />
    </>
  );
};

export default MoveTrailerItem;
