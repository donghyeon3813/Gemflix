import React, { useEffect, useState } from "react";
import { useScript } from "../../hooks";
import { shallowEqual, useSelector } from "react-redux";
const MoviePayment = (props) => {
  const movieServer = props.movieServer;
  const paymentInfo = props.paymentInfo;
  const userInfo = props.userInfo;
  const setReserveChildState = props.setReserveChildState;
  const setPaymentSucInfo = props.setPaymentSucInfo;

  let IMP;
  const status = useScript("https://code.jquery.com/jquery-1.12.4.min.js");
  const status02 = useScript(
    "https://cdn.iamport.kr/js/iamport.payment-1.1.8.js"
  );
  const user = useSelector((store) => store.userReducer, shallowEqual);
  const photoTicketPrice = 1000;

  const [payType, setPayType] = useState(1);
  const [disType, setDisType] = useState(null);
  const [carts, setCarts] = useState([]);
  const [photoTicketCnt, setPhotoTicekCnt] = useState(0);

  const requestPay = () => {
    const data = {
      pg: "html5_inicis", // PG사
      pay_method: "card", // 결제수단
      merchant_uid: `mid_${new Date().getTime()}`, // 주문번호
      amount: paymentInfo.totalPrice + photoTicketCnt * photoTicketPrice, // 결제금액
      name: "영화 예매", // 주문명
      buyer_name: userInfo.id, //주문자명
      buyer_tel: userInfo.phone, // 주문자 전화번호
      buyer_addr: "주소정보", //주문자 주소
      buyer_email: userInfo.email,
    };
    paymentInfo.selectedSeat.map((selectSeat) => {
      paymentInfo.screenInfo.seatList
        .filter((seat) => seat.seId == selectSeat)
        .map((seat) => console.log(seat.seRow + "-" + seat.seCol));
    });
    IMP.request_pay(data, (res) => {
      if (res.success) {
        // 결제 성공 시 로직
        console.log("payment requset success");
        const imp_uid = res.imp_uid;
        const merchant_uid = res.merchant_uid;
        const reserveInfo = {
          siId: paymentInfo.screenInfo.siId,
          seIds: paymentInfo.selectedSeat,
        };
        const data = {
          imp_uid: imp_uid,
          merchant_uid: merchant_uid,
          point: 0,
          pro_amount:
            paymentInfo.totalPrice + photoTicketCnt * photoTicketPrice,
          dis_amount: 0,
          pay_amount:
            paymentInfo.totalPrice + photoTicketCnt * photoTicketPrice,
          pay_type: payType,
          dis_type: disType,
          pay_name: userInfo.id,
          pay_phone: userInfo.phone,
          pay_address: "주소정보",
          carts: carts,
          reserveInfo: reserveInfo,
          photoTicketCnt: photoTicketCnt,
        };
        // axios로 HTTP 요청
        movieServer.completePayment(data, user.memberId).then((data) => {
          // 서버 결제 API 성공시 로직
          console.log(data);
          let reserveDate =
            paymentInfo.screenInfo.startDate +
            " " +
            paymentInfo.screenInfo.startTime +
            " ~ " +
            paymentInfo.screenInfo.endTime;
          let location =
            paymentInfo.screenInfo.location +
            " " +
            paymentInfo.screenInfo.trName;
          let peopleCnt = paymentInfo.selectedSeat.length;
          let seats = [];
          paymentInfo.selectedSeat.map((selectSeat) => {
            paymentInfo.screenInfo.seatList
              .filter((seat) => seat.seId == selectSeat)
              .map((seat) => seats.push(seat.seRow + "-" + seat.seCol));
          });

          setPaymentSucInfo({
            reserveDate: reserveDate,
            location: location,
            peopleCnt: peopleCnt,
            seats: seats,
          });
          setReserveChildState(4);
        });
        alert("결제가 완료되었습니다.");
      } else {
        // 결제 실패 시 로직
        console.log("payment requset fail: " + res.error_msg);
        alert("결제에 실패하였습니다. 에러 내용: " + res.error_msg);
      }
    });
  };

  //포토티켓 카운팅
  const handlePhotoTicektPlus = () => {
    if (photoTicketCnt < 5) {
      setPhotoTicekCnt(photoTicketCnt + 1);
    } else {
      alert("포토티켓은 5개까지 구매 가능합니다.");
      return;
    }
  };
  const handlePhotoTicektMinus = () => {
    if (photoTicketCnt > 0) {
      setPhotoTicekCnt(photoTicketCnt - 1);
    }
  };

  useEffect(() => {
    // sdk 초기화하기
    if (status === "ready" && status02 === "ready") {
      IMP = window.IMP;
      IMP.init(process.env.REACT_APP_IMP);
    }

    //setLimitPoint
  });

  return (
    <>
      <div className="reserve_group">
        <div className="reserve_group_step">
          <div className="group_top">
            <h4 className="title">예매정보</h4>
          </div>
          <div className="inner">
            <div className="movie-payment-info">
              <img width={200} src={paymentInfo.screenInfo.imageUrl} />
              <div>{paymentInfo.screenInfo.mvTitle}</div>
              <div>
                일시{" "}
                <span>
                  {paymentInfo.screenInfo.startDate}{" "}
                  {paymentInfo.screenInfo.startTime}~
                  {paymentInfo.screenInfo.endTime}
                </span>
              </div>
              <div>
                영화관{" "}
                <span>
                  {paymentInfo.screenInfo.location}{" "}
                  {paymentInfo.screenInfo.trName}
                </span>
              </div>
              <div>
                인원 <span>{paymentInfo.selectedSeat.length}</span>
              </div>
              <div>
                좌석{" "}
                {paymentInfo.selectedSeat.map((selectSeat, index) =>
                  paymentInfo.screenInfo.seatList
                    .filter((seat) => seat.seId == selectSeat)
                    .map((seat) => (
                      <span key={seat.seId}>
                        {index !== 0 ? ", " : null}
                        {seat.seRow}-{seat.seCol}
                      </span>
                    ))
                )}
              </div>
              <br />
              <div>추가상품 구매</div>
              <br />
              <div>
                포토 티켓{" "}
                <button
                  className="white_btn"
                  onClick={() => handlePhotoTicektMinus()}
                >
                  -
                </button>
                &nbsp;{photoTicketCnt}&nbsp;
                <button
                  className="white_btn"
                  onClick={() => handlePhotoTicektPlus()}
                >
                  +
                </button>
                <br />
                <div>
                  {(photoTicketCnt * photoTicketPrice).toLocaleString("ko-KR")}{" "}
                  원
                </div>
              </div>
            </div>
          </div>
        </div>
        <div className="reserve_group_step">
          <div className="group_top">
            <h4 className="title">결제하기</h4>
          </div>
          <div className="inner">
            <div className="price-group">
              <div>
                상품금액
                <span>
                  {" "}
                  {paymentInfo.totalPrice.toLocaleString("ko-KR")} 원
                </span>
              </div>
              <div>
                추가상품
                <span>
                  {" "}
                  +{" "}
                  {(photoTicketCnt * photoTicketPrice).toLocaleString(
                    "ko-KR"
                  )}{" "}
                  원
                </span>
              </div>
              <div>
                결제금액
                <span>
                  {" "}
                  총{" "}
                  {(
                    paymentInfo.totalPrice +
                    photoTicketCnt * photoTicketPrice
                  ).toLocaleString("ko-KR")}{" "}
                  원
                </span>
              </div>
              <button className="white_btn" onClick={requestPay}>
                결제하기
              </button>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default MoviePayment;
