
import React, { useState } from "react";
import { shallowEqual, useSelector } from "react-redux";
import PaymentItem from "./payment_item";
import PaymentTicketItem from "./payment_ticket_item";
import { useLocation } from 'react-router';

const PaymentList = () => {

  const user = useSelector(store => store.userReducer, shallowEqual);
  const location = useLocation();
  const [disStatus, setDisStatus] = useState('P');
  const payments = location.state.payments;
  
  const productDisplayType = disStatus === 'P' ? {display:"block"} : {display:"none"};
  const ticketDisplayType = disStatus === 'T' ? {display:"block"} : {display:"none"};
  const productBtnColor = disStatus === 'P' ? "indigo_btn" : "white_btn";
  const ticketBtnColor = disStatus === 'T' ? "indigo_btn" : "white_btn";

  const changeDisplay = (event) => {
    setDisStatus(event.target.value);
  };
  
  return (
    <div className="payment_list_form">
      <button className={productBtnColor} type="button" value="P" onClick={changeDisplay}>
        구매한 상품
      </button>
      <button className={ticketBtnColor} type="button" value="T" onClick={changeDisplay}>
        구매한 티켓
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
};

export default PaymentList;
