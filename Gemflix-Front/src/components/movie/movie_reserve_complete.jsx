import { useEffect, useState } from "react";
import MovieBigSeat from "./movie_big_seat";

const MovieReserveComplete = (props) => {
  const paymentSucInfo = props.paymentSucInfo;

  useEffect(() => {}, []);
  return (
    <>
      <div className="reserve_group_step">
        <div className="group_top">
          <h4 className="title">결제 완료</h4>
        </div>
        <div>결제가 성공적으로 완료되었습니다.</div>
        <div className="inner">
          <div>
            상영일시 <span>{paymentSucInfo.reserveDate}</span>
          </div>
          <div>
            상영관 <span>{paymentSucInfo.location}</span>
          </div>
          <div>
            관람인원 <span>{paymentSucInfo.peopleCnt}</span>
          </div>
          <div>
            좌석 <span>{paymentSucInfo.seats}</span>
          </div>
          <div>예매번호는 마이페이지에서 확인이 가능합니다.</div>
        </div>
      </div>
    </>
  );
};

export default MovieReserveComplete;
