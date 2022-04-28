import React, { useEffect, useState } from 'react';
import Modal from 'react-modal/lib/components/Modal';
import { shallowEqual, useDispatch, useSelector } from 'react-redux';
import { useLocation, useNavigate } from 'react-router';
import { useScript } from '../../hooks';
import { deleteCart } from '../../store/actions';
import DaumPost from './daum_post';

const Payment = ({server, onClickLogout}) => {

    const location = useLocation();
    const dispatch = useDispatch();
    const navigate = useNavigate();

    const status = useScript("https://code.jquery.com/jquery-1.12.4.min.js");
    const status02 = useScript("https://cdn.iamport.kr/js/iamport.payment-1.1.8.js");
    let IMP;

    const cartName = location.state.cartName;
    const price = location.state.price;
    let memberInfo;

    const user = useSelector(store => store.userReducer, shallowEqual);
    const [modalIsOpen, setModalIsOpen] = useState(false);
    const [name, setName] = useState('');
    const [phone, setPhone] = useState('');
    const [email, setEmail] = useState('');
    const [address, setAddress] = useState('');
    const [detailAddress, setDetailAddress] = useState('');

    const [carts, setCarts] = useState([]); //결제할 상품들
    const [beforeCarts, setBeforeCarts] = useState([]); //결제전 장바구니 상품목록
    const [tickets, setTickets] = useState([]); //결제할 티켓들
    const [delIdList, setDelIdList] = useState([]); //결제할 아이디들

    const [memberPoint, setMemberPoint] = useState(0); //총 보유 포인트
    const [usePoint, setUsePoint] = useState(0); //사용할 포인트
    const [remainPoint, setRemainPoint] = useState(0); //사용후 남을 포인트
    const [limitPoint, setLimitPoint] = useState(0);

    const [proAmount, setProAmount] = useState(price); //상품 원가
    const [disAmount, setDisAmount] = useState(0); //할인할 가격
    const [payAmount, setPayAmount] = useState(price); //최종 가격

    const [payType, setPayType] = useState(1);
    const [disType, setDisType] = useState(null);
    const [disStatus, setDisStatus] = useState('N');

    const pointDisplayType = disStatus === 'P' ? {display:"block"} : {display:"none"};

    useEffect(() => {
        // sdk 초기화하기
        if(status === "ready" && status02 === "ready"){
            IMP = window.IMP;
            IMP.init(process.env.REACT_APP_IMP);
		}
        
        //setLimitPoint
        settingMaxPoint();
    });
    
	useEffect(() => {
        if(location.state.carts){
            setCarts(location.state.carts);
        }
        if(location.state.beforeCarts){
            setBeforeCarts(location.state.beforeCarts);
        }
        if(location.state.tickets){
            setTickets(location.state.tickets);
        }
        if(location.state.delIdList){
            setDelIdList(location.state.delIdList);
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
        amount: payAmount, // 결제금액
        name: cartName, // 주문명
        buyer_name: name, //주문자명
        buyer_tel: phone, // 주문자 전화번호
        buyer_addr: address + " " + detailAddress, //주문자 주소
        buyer_email: email
    }

    const requestPay = () => {
        if(name && phone && address && detailAddress){
            if(payAmount == 0){ //결제할 금액 0원인 경우
                if(0 < disAmount){ //할인금액이 0보다 큰 경우

                    const data = {
                        point: usePoint,
                        pro_amount: proAmount,
                        dis_amount: disAmount,
                        pay_amount: payAmount,
                        pay_type: payType,
                        dis_type: disType,
                        pay_name: name,
                        pay_phone: phone,
                        pay_address: address + " " + detailAddress,
                        carts: carts,
                        tickets: tickets
                    };

                    // axios로 HTTP 요청
                    server.savePayment(data, user.memberId)
                        .then((data) => {
                            // 서버 결제 API 성공시 로직
                            console.log(data); 
                        });
                    alert("결제가 완료되었습니다.");
                    deleteCartPaidCarts(); //장바구니에서 삭제
                    navigate('/payments');

                }else{
                    alert("잘못된 결제 요청 입니다. 메인페이지로 이동합니다.");
                    navigate('/');
                }

            }else{
                // IMP.request_pay(param, callback) 결제창 호출
                IMP.request_pay(data
                    ,res => { // callback
                        if(res.success){
                            // 결제 성공 시 로직
                            console.log("payment requset success");
                            const imp_uid = res.imp_uid;
                            const merchant_uid = res.merchant_uid;

                            if(disAmount !== 0){
                                setDisType(1);
                            }

                            console.log(carts);
                            console.log(tickets);

                            const data = {
                                imp_uid: imp_uid,
                                merchant_uid: merchant_uid,
                                point: usePoint,
                                pro_amount: proAmount,
                                dis_amount: disAmount,
                                pay_amount: payAmount,
                                pay_type: payType,
                                dis_type: disType,
                                pay_name: name,
                                pay_phone: phone,
                                pay_address: address + " " + detailAddress,
                                carts: carts,
                                tickets: tickets
                            };

                            // axios로 HTTP 요청
                            server.completePayment(data, user.memberId)
                                .then((data) => {
                                    // 서버 결제 API 성공시 로직
                                    console.log(data); 
                                });
                                alert("결제가 완료되었습니다.");
                                deleteCartPaidCarts(); //장바구니에서 삭제
                                navigate('/payments');

                        }else{
                            // 결제 실패 시 로직
                            console.log("payment requset fail: " + res.error_msg);
                            alert("결제에 실패하였습니다. 에러 내용: " + res.error_msg);
                        }
                    }
                )
            }

        }else{
            alert("모든 정보를 입력해주세요.");
        }
    }

    const deleteCartPaidCarts = () => {
        //beforeCarts, carts, delIdList
        const memberId = user.memberId;
        let deleteAfterMemberItems;

        beforeCarts.filter(thisMember => {
            if(Object.hasOwn(thisMember, memberId)){
                let oldItems = thisMember[memberId];

                deleteAfterMemberItems = oldItems.map(thisItem => {
                    let oldSelectedCounts = thisItem.selectedCounts;
                    let newSelectedCounts = oldSelectedCounts.map((thisCount) => {
                        if(!delIdList.includes(thisCount.cId)){
                            return thisCount;
                        }
                    });
                    newSelectedCounts = newSelectedCounts.filter((element) => element !== undefined);
                    if(0 < newSelectedCounts.length){
                        thisItem.selectedCounts = newSelectedCounts;
                        return thisItem;
                    }
                });
            }
        });
        deleteAfterMemberItems = deleteAfterMemberItems.filter((element) => element !== undefined);
        console.log(deleteAfterMemberItems);
        dispatch(deleteCart(deleteAfterMemberItems, memberId));
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
        setUsePoint(0);
        setRemainPoint(memberPoint);
        setProAmount(price);
        setDisAmount(0);
        setPayAmount(price);
    };
    
    const changeRadioPoint = (event) => {
        setDisStatus(event.target.value);
    };

    const changeUsePoint = (event) => {
        let tempUsePoint = event.target.value; //사용할 포인트
        let tempRemainPoint = memberPoint - tempUsePoint; //남을 포인트
        let limitUsePoint;

        setUsePoint(tempUsePoint);
        setRemainPoint(memberPoint - tempUsePoint);
        setDisAmount(tempUsePoint);
        settingPayAmount(tempUsePoint);

        if(memberPoint < proAmount){ //보유포인트 < 최종가격
            limitUsePoint = memberPoint;
        }else{ //보유포인트 >= 최종가격
            limitUsePoint = proAmount;
        }

        if(limitUsePoint < tempUsePoint || tempRemainPoint < 0){
            alert("포인트를 최대치로 사용하였습니다.");
            setUsePoint(limitUsePoint);
            setRemainPoint(memberPoint - limitUsePoint);
            setDisAmount(limitUsePoint);
            settingPayAmount(limitUsePoint);
        }
    }

    const useAllPoint = () => {
        if(proAmount < memberPoint){ //상품가격 < 보유포인트
            setUsePoint(proAmount);
            setRemainPoint(memberPoint - proAmount);
            setDisAmount(proAmount);
            settingPayAmount(proAmount);

        }else{ //상품가격 > 보유포인트, 상품가격 == 보유포인트
            setUsePoint(memberPoint);
            setRemainPoint(0);
            setDisAmount(memberPoint);
            settingPayAmount(memberPoint);
            
        }
    }

    const settingMaxPoint = () => {
        if(memberPoint > proAmount){ //보유포인트 > 최종가격
            setLimitPoint(proAmount);
        }else{ //보유포인트 =< 최종가격
            setLimitPoint(memberPoint);
        }
    }
    
    const settingPayAmount = (dis) => {
        setPayAmount(proAmount - dis);

        if(0 < (proAmount - dis)){
            setPayTypeCard();

        }else{ //결제금액 0원(포인트로 모두 결제한 경우)
            setPayTypePoint();
        }
    }

    const setPayTypePoint = () => {
        setDisType(1);
        setPayType(3); //point
    }

    const setPayTypeCard = () => {
        setDisType(null);
        setPayType(1); //card
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
                    <input value={usePoint} type="number" min="0" max={limitPoint} placeholder="사용할 포인트" onChange={(e) => changeUsePoint(e)}/><br/>
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