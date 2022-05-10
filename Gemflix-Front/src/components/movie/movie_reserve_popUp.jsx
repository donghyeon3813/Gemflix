import { useEffect, useState } from "react";
import MovieSmaillSeat from "./movie_small_seat";

const MovieReservePopUp = (props) => {
  const handleClosePop = props.handleClosePop;
  const movieServer = props.movieServer;
  const siId = props.siId;
  const setReserveChildState = props.setReserveChildState;
  const [screenInfo, setScreenInfo] = useState({});
  const [seatRows, setSeatRows] = useState([]);
  const handleGetScreenInfo = (siId) => {
    movieServer.screenInfo(siId).then((response) => {
      console.log(response);
      setScreenInfo(response.data);
      const dupSeatRows = new Set();
      response.data.seatList.map((info) => dupSeatRows.add(info.seRow));
      setSeatRows([...dupSeatRows]);
    });
  };
  const handleRatingSwitch = (rating) => {
    let ratingStr = "";
    switch (rating) {
      case "1":
        ratingStr = "ⓐ 본 영화는 전체 관람가 입니다.";
        break;
      case "2":
        ratingStr = "⑫ 본 영화는 만 12세 관람가 입니다.";
        break;
      case "3":
        ratingStr = "⑮ 본 영화는 만 15세 관람가 입니다.";
        break;
      case "4":
        ratingStr = "🔞 본 영화는 청소년 관람불가 입니다.";
        break;
    }
    return ratingStr;
  };
  useEffect(() => {
    handleGetScreenInfo(siId);
  }, []);
  return (
    <>
      <div className="modal-seat" onClick={() => handleClosePop()}>
        <div className="modal-seat-content">
          <div className="modal-seat-head">
            {screenInfo.startTime + "/" + screenInfo.endTime}
            <div
              className="modal-seat-head-item"
              onClick={() => handleClosePop()}
            >
              ✖
            </div>
          </div>
          <div className="modal-spare-group">
            <div className="modal-spare-seat">
              잔여좌석
              {screenInfo.seatCnt -
                screenInfo.spareSeatCnt +
                "/" +
                screenInfo.seatCnt}
            </div>
            <div className="modal-scrren">S C R E E N</div>
            <div className="modal-small-seat">
              {seatRows.map((row) => (
                <div className="small-seat-rows" key={row}>
                  {screenInfo.seatList
                    .filter((seat) => seat.seRow === row)
                    .map((seatInfo) => (
                      <MovieSmaillSeat
                        key={seatInfo.seId}
                        seatInfo={seatInfo}
                      />
                    ))}
                </div>
              ))}
            </div>

            <div className="guide-text">
              {" "}
              {handleRatingSwitch(screenInfo.rating)}{" "}
            </div>
          </div>
          <div className="btn-group">
            <button className="black-btn" onClick={() => handleClosePop()}>
              취소
            </button>
            <button
              className="purple_btn"
              onClick={() => setReserveChildState(2)}
            >
              인원/좌석 선택
            </button>
          </div>
        </div>
      </div>
    </>
  );
};

export default MovieReservePopUp;
