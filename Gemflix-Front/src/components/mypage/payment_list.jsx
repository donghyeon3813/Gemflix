
import React, { useState } from "react";
import { shallowEqual, useSelector } from "react-redux";
import PaymentItem from "./payment_item";
import PaymentTicketItem from "./payment_ticket_item";
import { useLocation } from 'react-router';

const PaymentList = () => {

        const user = useSelector(store => store.userReducer, shallowEqual);
        const location = useLocation();
        const [disStatus, setDisStatus] = useState('T');
        const productDisplayType = disStatus === 'P' ? {display:"block"} : {display:"none"};
        const ticketDisplayType = disStatus === 'T' ? {display:"block"} : {display:"none"};
        const payments = location.state.payments;
        

  const changeDisplay = (event) => {
    setDisStatus(event.target.value);
  };

  if (payments.length == 0) {
    return <div>결제내역이 없습니다.</div>;
  } else {
    return (
      <div>
        <p>결제내역 페이지</p>
        <button type="button" value="T" onClick={changeDisplay}>
          구매한 티켓
        </button>
        <button type="button" value="P" onClick={changeDisplay}>
          구매한 상품
        </button>

        {/* 상품 결제내역 */}
        <div style={productDisplayType}>
          {payments.map((payment) => {
            return <PaymentItem key={payment.pmId} payment={payment} />;
          })}
        </div>

        {/* 티켓 결제내역 */}
        <div style={ticketDisplayType}>
          {payments.map((payment) => {
            return <PaymentTicketItem key={payment.pmId} payment={payment} />;
          })}
        </div>
      </div>
    );
  }
};

export default PaymentList;
