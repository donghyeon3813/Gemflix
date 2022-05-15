import { useEffect, useState } from "react";
import MovieBigSeat from "./movie_big_seat";

const MovieReserveSeat = (props) => {
  const movieServer = props.movieServer;
  const siId = props.siId;
  const setReserveChildState = props.setReserveChildState;
  const setPaymentInfo = props.setPaymentInfo;

  const [screenInfo, setScreenInfo] = useState({});
  const [seatRows, setSeatRows] = useState([]);
  const [personCnt, setPersonCnt] = useState(0);
  const [selectedSeat, setSelectedSeat] = useState([]);
  const [seatColorStatus, setSeatColorStatus] = useState(false);
  const [totalPrice, setTotalPrice] = useState(0);
  const handleGetScreenInfo = (siId) => {
    movieServer.screenInfo(siId).then((response) => {
      setScreenInfo(response.data);
      const dupSeatRows = new Set();
      response.data.seatList.map((info) => dupSeatRows.add(info.seRow));
      setSeatRows([...dupSeatRows]);
    });
  };
  const handleSetSelectedSeat = (seat, tkId) => {
    if (tkId !== null) {
      return;
    }
    let seatStatus = selectedSeat.indexOf(seat);
    if (personCnt === 0) {
      alert("인원을 입력해주세요");
      return;
    } else if (
      seatStatus === -1 &&
      Number(personCnt) < selectedSeat.length + 1
    ) {
      alert("인원을 확인해주세요");
      return;
    }
    let dupSelectedSeat = [...selectedSeat];
    if (seatStatus === -1) {
      dupSelectedSeat.push(seat);
      setSelectedSeat(dupSelectedSeat);
    } else {
      dupSelectedSeat = selectedSeat.filter((info) => info !== seat);
      setSelectedSeat(dupSelectedSeat);
    }

    if (dupSelectedSeat.length === Number(personCnt)) {
      setSeatColorStatus(true);
      let price = 0;
      switch (screenInfo.type) {
        case "1":
          price = 7000;
          break;
        case "2":
          price = 10000;
          break;
        case "3":
          price = 7000;
          break;
      }

      setTotalPrice(dupSelectedSeat.length * price);
    } else {
      setSeatColorStatus(false);
    }
  };
  const handleSetPersonCnt = (e) => {
    setPersonCnt(e.target.value);
    if (selectedSeat.length < e.target.value) {
      setSeatColorStatus(false);
      setPersonCnt(e.target.value);
    } else if (selectedSeat.length === e.target.value) {
      setSeatColorStatus(true);
      setPersonCnt(e.target.value);
    } else {
      setSelectedSeat([]);
      setSeatColorStatus(false);
      setTotalPrice(0);
      return;
    }
  };

  const handleSetPaymentInfo = () => {
    setPaymentInfo({
      screenInfo: screenInfo,
      selectedSeat: selectedSeat,
      totalPrice: totalPrice,
    });
    setReserveChildState(3);
  };

  useEffect(() => {
    handleGetScreenInfo(siId);
  }, []);
  return (
    <>
      <div className="reserve_group_step">
        <div className="group_top">
          <h4 className="title">인원/좌석 선택</h4>
        </div>

        <div className="inner">
          <div className="bg-black">
            인원{" "}
            <input
              type="number"
              value={personCnt}
              onChange={(e) => handleSetPersonCnt(e)}
            />
          </div>
          <div className="bg-black">- 인원을 선택하세요</div>
          <div className="screen-text">S C R E E N</div>
          <div className="big-seat">
            {seatRows.map((row) => (
              <div className="big-seat-rows" key={row}>
                <span className="seat-row-name">{row}</span>
                {screenInfo.seatList
                  .filter((seat) => seat.seRow === row)
                  .map((seatInfo) => (
                    <MovieBigSeat
                      handleSetSelectedSeat={handleSetSelectedSeat}
                      key={seatInfo.seId}
                      seatInfo={seatInfo}
                      selectedSeat={selectedSeat}
                      seatColorStatus={seatColorStatus}
                    />
                  ))}
              </div>
            ))}
          </div>
          <div>
            총 합계 : {totalPrice.toLocaleString("ko-KR")} 원{" "}
            <button
              className="white_btn"
              onClick={() => handleSetPaymentInfo(3)}
            >
              결제하기
            </button>
          </div>
        </div>
      </div>
    </>
  );
};

export default MovieReserveSeat;
