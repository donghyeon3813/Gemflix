import React from "react";

const PaymentTicketItem = (props) => {
  const payment = props.payment;
  const paidProducts = payment.paidProducts;
  const tickets = payment.tickets;

  const inputPriceFormat = (str) => {
    const comma = (str) => {
      str = String(str);
      return str.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, "$1,");
    };
    const uncomma = (str) => {
      str = String(str);
      return str.replace(/[^\d]+/g, "");
    };
    return comma(uncomma(str));
  };
  
  return (
    <div>
      {tickets.length > 0 ? (
        <div className="payment_item">
          <h3>예매번호 : {payment.pmId}</h3>
          <p>결제일시 : {payment.payDate}</p>
          <div>
            상영일시 <span>{tickets[0].screening.startTime}</span>
          </div>
          <div>
            상영관{" "}
            <span>
              {tickets[0].seat.theaterRoom.theater.location}{" "}
              {tickets[0].seat.theaterRoom.name}
            </span>
          </div>
          <div>
            관람인원 : <span>{tickets.length}</span>
          </div>
          <div>
            좌석 :{" "}
            {tickets.map((ticket, index) => {
              return (
                <span key={index}>
                  {index > 0 ? "," : null}
                  {ticket.seat.seRow}-{ticket.seat.seCol}
                </span>
              );
            })}
          </div>

          <p>상품금액 : {inputPriceFormat(payment.proAmount)} 원</p>
          <p>할인금액 : {inputPriceFormat(payment.disAmount)} 원</p>
          <p>결제금액 : {inputPriceFormat(payment.payAmount)} 원</p>
        </div>
      ) 
      : null}
    </div>
  );
};

export default PaymentTicketItem;
