import React, { useState } from 'react';

const PaymentList = (props) => {

        const [disStatus, setDisStatus] = useState('T');
        const productDisplayType = disStatus === 'P' ? {display:"block"} : {display:"none"};
        const ticketDisplayType = disStatus === 'T' ? {display:"block"} : {display:"none"};

        const changeRadio = (event) => {
                setDisStatus(event.target.value);
        };

        return (
                <div>
                        <p>결제내역 페이지</p>
                        <label><input type="radio" value='T' checked={disStatus === "T" ? true : false} onChange={changeRadio}/>구매한 티켓</label>
                        <label><input type="radio" value='P' checked={disStatus === "P" ? true : false} onChange={changeRadio}/>구매한 상품</label>
                        
                        {/* 상품 결제내역 */}
                        <div style={productDisplayType}>
                                구매한 상품들
                        </div>

                        {/* 티켓 결제내역 */}
                        <div style={ticketDisplayType}>
                                구매한 티켓들
                        </div>

                </div>
        );
};

export default PaymentList;