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
      <div className="modal-filmo" onClick={() => handleClosePop()}>
        <div className="modal-filmo-content">
          <div className="modal-filmo-head">
            {screenInfo.startTime + "/" + screenInfo.endTime}
            <label
              className="modal-filmo-head-item"
              onClick={() => handleClosePop()}
            >
              ✖
            </label>
          </div>
          <div className="modal-filmo-people">
            <div>
              잔여좌석
              {screenInfo.seatCnt -
                screenInfo.spareSeatCnt +
                "/" +
                screenInfo.seatCnt}
            </div>
            <div>
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

            <div> {handleRatingSwitch(screenInfo.rating)} </div>
          </div>
          <div>
            <button onClick={() => handleClosePop()}>취소</button>
            <button onClick={() => setReserveChildState(2)}>
              인원/좌석 선택
            </button>
          </div>
          <div className="modal-filmo-item"></div>
        </div>
      </div>
    </>
  );
};

export default MovieReservePopUp;
