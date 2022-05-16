import MoveTrailerItem from "./movie_trailer_Item";
import MoveTrailerPopUp from "./movie_trailer_popUp";
import React, { useEffect, useState } from "react";

import { Swiper, SwiperSlide } from "swiper/react";
import "swiper/swiper.scss";
import "swiper/components/navigation/navigation.scss"; // *
import "swiper/components/pagination/pagination.scss"; // *

import SwiperCore, { Navigation, Pagination, A11y } from "swiper";

SwiperCore.use([Navigation, Pagination, A11y]);

const MoveTrailer = (props) => {
  const trailerList = props.trailerList;
  const [trailerPop, setTrailerPop] = useState(false);
  const [trLocation, setTrLocation] = useState("");

  const handleSetPop = (trLocation) => {
    setTrLocation(trLocation);
    setTrailerPop(true);
  };

  const handleClosePop = () => {
    setTrailerPop(false);
  };

  return (
    <>
      <Swiper
        className="swiper-container"
        slidesOffsetBefore={60}
        slidesOffsetAfter={40}
        spaceBetween={50}
        slidesPerView={3}
        navigation //*
        centeredSlides={true}
        pagination={{ clickable: true }} //*
      >
        {trailerList.map((info) => (
          <SwiperSlide key={info.imgLocation}>
            <MoveTrailerItem
              imgLocation={info.imgLocation}
              trLocation={info.trLocation}
              handleSetPop={handleSetPop}
            />
          </SwiperSlide>
        ))}
      </Swiper>

      {trailerPop && (
        <MoveTrailerPopUp
          trLocation={trLocation}
          handleClosePop={handleClosePop}
        />
      )}
    </>
  );
};

export default MoveTrailer;
