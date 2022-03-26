import axios from 'axios';
import React, { useEffect, useState } from 'react';
import Modal from 'react-modal/lib/components/Modal';
import { useLocation, useNavigate, useResolvedPath } from 'react-router';
import { useScript } from '../../hooks';
import DaumPost from './daum_post';

const Payment = ({server, onClickLogout}) => {

    const location = useLocation();
    const navigate = useNavigate();
    const status = useScript("https://code.jquery.com/jquery-1.12.4.min.js");
    const status02 = useScript("https://cdn.iamport.kr/js/iamport.payment-1.1.8.js");
    let IMP;

    const cartName = location.state.cartName;
    const price = location.state.price;
    let memberInfo;
    let carts;
    let tickets;

    const [modalIsOpen, setModalIsOpen] = useState(false);
    const [name, setName] = useState('');
    const [phone, setPhone] = useState('');
    const [email, setEmail] = useState('');
    const [address, setAddress] = useState('');
    const [detailAddress, setDetailAddress] = useState('');

    const [memberPoint, setMemberPoint] = useState(0); //총 보유 포인트
    const [usePoint, setUsePoint] = useState(''); //사용할 포인트
    const [remainPoint, setRemainPoint] = useState(0); //사용후 남을 포인트

    const [proAmount, setProAmount] = useState(price); //상품 원가
    const [disAmount, setDisAmount] = useState(0); //할인할 가격
    const [payAmount, setPayAmount] = useState(price); //최종 가격

    const [payType, setPayType] = useState(1);
    const [disType, setDisType] = useState(null);
    const [disStatus, setDisStatus] = useState('N');

    const pointDisplayType = disStatus === 'P' ? {display:"block"} : {display:"none"};
    
	useEffect(() => {
        if(status === "ready" && status02 === "ready"){
            // sdk 초기화하기
            IMP = window.IMP;
            IMP.init(process.env.REACT_APP_IMP);
		}
        if(location.state.carts){
            carts = location.state.carts;
        }
        if(location.state.tickets){
            tickets = location.state.tickets;
        }
        console.log(carts);
        console.log(tickets);
	}, [carts, tickets])

    useEffect(() => {
        //server reqeust
        server.profile()
        .then(response => {
            const code = response.code;
            switch(code){
                case 1007: //interceptor에서 accessToken 재발급
                    break;

                case 1000: //success
                    memberInfo = response.data;
                    setMemberPoint(memberInfo.point);
                    setRemainPoint(memberInfo.point);
                    setEmail(memberInfo.email);
                    break;

                case 1008: //refreshToken 만료 -> 로그아웃
                    onClickLogout(true);
                    break;

                default: //fail
                    alert("해당 작업을 수행할 수 없습니다. 잠시 후 다시 시도해주세요.");
                    navigate('/');
                    break;
            }
        })
        .catch(ex => {
            console.log("profile requset fail : " + ex);
        })
        .finally(() => {
            console.log("profile request end");
        });
    }, []);
    
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
    
    const data = {
        pg: 'html5_inicis', // PG사
        pay_method: 'card', // 결제수단
        merchant_uid: `mid_${new Date().getTime()}`, // 주문번호
        amount: price, // 결제금액
        name: cartName, // 주문명
        buyer_name: name, //주문자명
        buyer_tel: phone, // 주문자 전화번호
        buyer_addr: address + " " + detailAddress, //주문자 주소
        buyer_email: email
    }

    const requestPay = () => {
        if(name && phone && address && detailAddress){
            console.log(data);
            // IMP.request_pay(param, callback) 결제창 호출
            IMP.request_pay(data
                ,res => { // callback
                    if(res.success){
                        // 결제 성공 시 로직
                        console.log("payment requset success");
                        const imp_uid = res.imp_uid;
                        const merchant_uid = res.merchant_uid;

                        // const formData = new FormData();
                        // formData.append("imp_uid", imp_uid);
                        // formData.append("merchant_uid", merchant_uid);
                        // formData.append("point", usePoint); //사용 포인트
                        // formData.append("pro_amount", proAmount); //원가 상품 금액
                        // formData.append("dis_amount", disAmount); //할인 금액
                        // formData.append("pay_amount", payAmount); //최종 금액
                        // formData.append("pay_type", payType); //결제방식
                        // formData.append("dis_type", disType); //할인방식
                        // formData.append("pay_name", name); //주문자명
                        // formData.append("pay_phone", phone); //주문자 전화번호
                        // formData.append("pay_address", address + " " + detailAddress); //주문자 주소

                        // // axios로 HTTP 요청
                        // server.completePayment(formData)
                        //     .then((data) => {
                        //         // 서버 결제 API 성공시 로직
                        //         console.log(data);
                                
                        //     })

                    }else{
                        // 결제 실패 시 로직
                        console.log("payment requset fail: " + res.error_msg);
                        alert("결제에 실패하였습니다. 에러 내용: " + res.error_msg);
                    }
                }
            )

        }else{
            alert("모든 정보를 입력해주세요.");
        }
    }

    const changeName = (event) => {
        setName(event.target.value);
    }

    const changePhone = (event) => {
        setPhone(event.target.value);
    }

    const changeDetailAddress = (event) => {
        setDetailAddress(event.target.value);
    }
    
    const returnFullAddress = (address) => {
        setAddress(address);
        setModalIsOpen(false);
    }

    const changeRadioNone = (event) => {
        setDisStatus(event.target.value);
    };
    
    const changeRadioPoint = (event) => {
        setDisStatus(event.target.value);
    };

    const changeRadioTicket = (event) => {
        setDisStatus(event.target.value);
    };

    const changeUsePoint = (event) => {
        const newUsePoint = event.target.value;
        const newRemainPoint = memberPoint - newUsePoint;
        setUsePoint(event.target.value);
        setRemainPoint(memberPoint - newUsePoint);
        console.log(usePoint+":"+remainPoint);
    }

    const useAllPoint = () => {
        if(payAmount < memberPoint){ //상품가격 < 보유포인트
            setUsePoint(payAmount);
            setRemainPoint(memberPoint - payAmount);

        }else{ //상품가격 > 보유포인트, 상품가격 == 보유포인트
            setUsePoint(memberPoint);
            setRemainPoint(0);
        }
    }

    return (
        <div>
            <h3>배송 정보를 입력해주세요.</h3>
            <br/>
            <div>
                <h4>상품 정보</h4>
                <p>{cartName}</p>
                <br/>

                <h4>할인적용</h4>
                <label><input type="radio" value='N' checked={disStatus === "N" ? true : false} onChange={changeRadioNone}/>없음</label>
                <label><input type="radio" value='P' checked={disStatus === "P" ? true : false} onChange={changeRadioPoint}/>포인트 사용</label>
                
                <div style={pointDisplayType}>
                    <label>사용할 포인트</label>
                    <input value={usePoint} type="text" placeholder="사용할 포인트" onChange={(e) => changeUsePoint(e)}/><br/>
                    <p>잔여 포인트 : {remainPoint}</p>
                    <button type='button' onClick={useAllPoint}>전액사용</button>
                </div>

                <br/>
                <br/>
                <h2>총 {inputPriceFormat(payAmount)}원</h2>
            </div>
            <br/>
            <div>
                <h4>주문자 정보</h4>
                <label>이름: </label><br/>
                <input value={name} type="text" placeholder="이름" onChange={changeName}/><br/>
                <label>핸드폰 번호: </label><br/>
                <input value={phone} type="tel" placeholder="핸드폰 번호" onChange={changePhone}/><br/>
                <label>주소: </label>

                <button onClick={() => setModalIsOpen(true)}>우편번호 찾기</button><br/>
                    <Modal isOpen={modalIsOpen} onRequestClose={() => setModalIsOpen(false)} ariaHideApp={false}>
                        <div className='address_modal'>
                            <DaumPost returnFullAddress={returnFullAddress}/>
                        </div>
                    </Modal>

                <input value={address} type="text" readOnly placeholder="주소" /><br/>
                <input value={detailAddress} type="text" placeholder="상세주소" onChange={changeDetailAddress}/><br/>
            </div>
            <button onClick={requestPay}>결제하기</button>
        </div>
      );
    
};

export default Payment;