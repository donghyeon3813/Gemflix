import React, { useEffect, useRef, useState } from "react";
const MoveTrailerItem = (props) => {
  const handleSetPop = props.handleSetPop;
  const imgLocation = props.imgLocation;
  const trLocation = props.trLocation;
  const imgRef = useRef(null);
  useEffect(() => {
    if (imgRef.current.height === 90) {
    }
  }, []);

  return (
    <>
      <img
        ref={imgRef}
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
