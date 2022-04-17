import React, { useEffect, useState } from "react";
import { Swiper, SwiperSlide } from "swiper/react";
import "swiper/swiper.scss";
import "swiper/components/navigation/navigation.scss"; // *
import "swiper/components/pagination/pagination.scss"; // *

import "./css/movie_reserve.css";
import SwiperCore, { Navigation, Pagination, A11y } from "swiper";
import MovieReservePopUp from "./movie_reserve_popUp";
import MovieReserveSeat from "./movie_reserve_seat";

SwiperCore.use([Navigation, Pagination, A11y]);
const MovieReserve = ({ movieServer }) => {
  const [places, setPlaces] = useState([]);
  const [theaters, setTheaters] = useState([]);
  const [movies, setMovies] = useState([]);
  const [dates, setDates] = useState([]);
  const [screens, setScreens] = useState([]);
  const [mvTitles, setMvTitles] = useState([]);
  const [searchData, setSearchData] = useState({
    thId: "",
    mvId: "",
    date: "",
  });
  const [siId, setSiId] = useState();
  const [reservePopUp, setReservePopup] = useState(false);
  const [reserveChildState, setReserveChildState] = useState(1);
  const handleGetPlaceList = () => {
    movieServer.place().then((response) => {
      console.log(response);
      setPlaces(response.data);
      handleGetTheaterList(response.data[0]);
    });
  };

  const handleGetTheaterList = (place) => {
    const data = { place: place };
    movieServer.theater(data).then((response) => {
      console.log(response);
      setTheaters(response.data);
    });
  };
  const handleGetShowingMovieList = () => {
    movieServer.showingMovie().then((response) => {
      console.log(response);
      setMovies(response.data);
    });
  };
  const handleGetScreenList = () => {
    movieServer.screens(searchData).then((response) => {
      console.log(response);
      let screenArr = response.data;
      setScreens(screenArr);
      const dupMvtitle = new Set();
      screenArr.map((info) => dupMvtitle.add(info.mvTitle));
      setMvTitles([...dupMvtitle]);
    });
  };

  const handleSetSearchData = (e, text) => {
    console.log(e.target.id);
    const name = e.target.id;

    if (name === "thId") {
      let json = { [name]: text, mvId: "" };
      setSearchData({ ...searchData, ...json });
    } else {
      setSearchData({ ...searchData, [name]: text });
    }
  };

  const handleGetDate = () => {
    let nowDate = new Date();
    console.log(nowDate.getDate());
    let finalDate = new Date(nowDate);
    finalDate.setDate(nowDate.getDate() + 13);
    let result = [];
    console.log(nowDate);
    console.log(finalDate);
    // nowDate.setHours(nowDate.getHours() + 9);
    while (nowDate <= finalDate) {
      let date = nowDate.getDate();
      let formetDate = nowDate.toISOString().split("T")[0];
      result.push({ formetDate: formetDate, date: date });
      nowDate.setDate(nowDate.getDate() + 1);
    }
    setDates([...result]);
  };
  const handleReservePopupOn = (siId) => {
    setSiId(siId);
    setReservePopup(true);
  };
  const handleClosePop = () => {
    setReservePopup(false);
  };

  useEffect(() => {
    handleGetPlaceList();
    handleGetShowingMovieList();
    handleGetDate();
  }, []);

  useEffect(() => {
    if (searchData.date !== "" && searchData.thId !== "") {
      handleGetScreenList();
    }
  }, [searchData]);

  return (
    <div className="reserve_box">
      <div className="reserve_step">
        <ul>
          <li>상영시간</li>
          <li>인원/좌석</li>
          <li>결제</li>
          <li>결제완료</li>
        </ul>
      </div>
      {reserveChildState === 1 ? (
        <div className="reserve_group">
          <div className="reserve_group_step">
            <div className="group_top">
              <h4 className="title">영화관</h4>
            </div>
            <div className="inner">
              {places.map((info) => (
                <div key={info} onClick={() => handleGetTheaterList(info)}>
                  {info}
                </div>
              ))}
              {theaters.map((info) => (
                <div
                  id="thId"
                  key={info.thId}
                  onClick={(e) => handleSetSearchData(e, info.thId)}
                >
                  {info.location}{" "}
                </div>
              ))}
            </div>
          </div>
          <div className="reserve_group_step">
            <div className="group_top">
              <h4 className="title">영화선택</h4>
            </div>
            <div className="inner">
              {movies.map((info) => (
                <div
                  id="mvId"
                  onClick={(e) => handleSetSearchData(e, info.mvId)}
                  key={info.mvId}
                >
                  {info.title}
                </div>
              ))}
            </div>
          </div>
          <div className="reserve_group_step">
            <div className="group_top">
              <h4 className="title">날짜</h4>
            </div>
            <div className="inner">
              <Swiper
                className="swiper-container"
                slidesOffsetBefore={1}
                slidesOffsetAfter={1}
                spaceBetween={50}
                slidesPerView={7}
                //*
                onSwiper={(swiper) => console.log(swiper)}
                onSlideChange={() => console.log("slide change")}
              >
                {dates.map((info) => (
                  <SwiperSlide key={info.date}>
                    <span
                      id="date"
                      onClick={(e) => handleSetSearchData(e, info.formetDate)}
                    >
                      {info.date}
                    </span>
                  </SwiperSlide>
                ))}
              </Swiper>
              <div className="theater-room-all">
                {mvTitles.map((title) => (
                  <React.Fragment key={title}>
                    <div>{title}</div>
                    <div className="theater-room-group">
                      {screens
                        .filter((screen) => screen.mvTitle === title)
                        .map((screen) => (
                          <div
                            className="theater-room"
                            key={screen.siId}
                            onClick={() => handleReservePopupOn(screen.siId)}
                          >
                            <div>
                              {
                                new Date(screen.startTime)
                                  .toTimeString()
                                  .split(" ")[0]
                              }
                            </div>
                            <div>
                              <span>
                                {screen.spareSeatCnt}/{screen.seatCnt}
                              </span>
                              <span>{screen.trName}</span>
                            </div>
                          </div>
                        ))}
                    </div>
                  </React.Fragment>
                ))}
              </div>
            </div>
          </div>
        </div>
      ) : (
        <div className="reserve_group">
          <MovieReserveSeat siId={siId} movieServer={movieServer} />
        </div>
      )}

      {reservePopUp && (
        <MovieReservePopUp
          movieServer={movieServer}
          handleClosePop={handleClosePop}
          setReserveChildState={setReserveChildState}
          siId={siId}
        />
      )}
    </div>
  );
};

export default MovieReserve;
