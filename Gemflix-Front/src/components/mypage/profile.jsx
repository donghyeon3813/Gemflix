import React, { useEffect, useState } from 'react';
import { shallowEqual, useDispatch, useSelector } from 'react-redux';
import { useLocation, useNavigate } from 'react-router';
import { deleteCartByMember } from '../../store/actions';

const Profile = ({server, onClickLogout}) => {

    const location = useLocation();
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const user = useSelector(store => store.userReducer, shallowEqual);

    const [email, setEmail] = useState(null);
    const [tempEmail, setTempEmail] = useState('');
    const [regDate, setRegDate] = useState(null);
    const memberInfo = location.state.memberInfo;


    useEffect(() => {
        let regDt = memberInfo.regDate;
        setRegDate(String(regDt).replace("T", " "));
        setEmail(memberInfo.email);
    }, []);

    const onClickCartList = () => {
        navigate('/cartList', {
            state: {
                phone: memberInfo.phone
            }
        });
    }

    const onClickPaymentList = () => {
        getPayments();
    }

    const getPayments = () => {
        //server reqeust
        server.payments(user.memberId)
        .then(response => {
        const code = response.code;
        switch(code){
                case 1007: //interceptor에서 accessToken 재발급
                    break;

                case 1000: //success
                    const payments = response.data;
                    navigate('/payments', {
                        state: {
                            memberInfo: memberInfo,
                            payments: payments
                        }
                    });
                    break;

                case 1008: //refreshToken 만료 -> 로그아웃
                onClickLogout(true);
                    break;

                default:
                    const payments02 = [];
                    navigate('/payments', {
                        state: {
                            memberInfo: memberInfo,
                            payments: payments02
                        }
                    });
                    break;
        }
        })
        .catch(ex => {
        console.log("payments requset fail : " + ex);
        })
        .finally(() => {
        console.log("payments request end");
        });
    }

    const onClickDeleteMember = () => {
        const memberId = user.memberId;
        if(window.confirm("정말 탈퇴하겠습니까?")){

            server.deleteMember(memberId)
            .then(response => {
                const code = response.code;
                switch(code){
                    case 1007: //interceptor에서 accessToken 재발급
                        break;

                    case 1000: //success
                        alert("탈퇴되었습니다.");
                        dispatch(deleteCartByMember(memberId));
                        onClickLogout(false);
                        navigate('/');
                        break;

                    default: //fail
                        alert("해당 작업을 수행할 수 없습니다. 잠시 후 다시 시도해주세요.");
                        navigate('/');
                        break;
                }
            })
            .catch(ex => {
                console.log("deleteMember requset fail : " + ex);
            })
            .finally(() => {
                console.log("deleteMember request end");
            });
        }
    }

    const onClickCertifyEmail = () => {
        const memberId = user.memberId;
        const data = { email: tempEmail };

        server.certifyEmail(data, memberId)
            .then(response => {
                const code = response.code;
                switch(code){
                    case 1007: //interceptor에서 accessToken 재발급
                        break;

                    case 1000: //success
                        alert("이메일로 인증번호가 전송되었습니다. 이메일을 확인하여 인증을 완료하세요.");
                        navigate('/');
                        break;

                    default: //fail
                        alert("해당 작업을 수행할 수 없습니다. 잠시 후 다시 시도해주세요.");
                        navigate('/');
                        break;
                }
            })
            .catch(ex => {
                console.log("certifyEmail requset fail : " + ex);
            })
            .finally(() => {
                console.log("certifyEmail request end");
            });
    }

    const changeEmail = (event) => {
        setTempEmail(event.target.value);
    }

    return (
        <>
        <div>
            <h1>Profile</h1>
            <label>아이디 : {memberInfo.id}</label><br/>
            {memberInfo.phone == null ? 
                <>
                </>
            :
                <>
                <label>핸드폰 번호 : {memberInfo.phone}</label><br/>
                </>
                    
            }



            {email == null ? 
                <>
                <label>이메일 : </label>
                <input type="email" placeholder="email" onChange={changeEmail}/>
                <button type='button' onClick={onClickCertifyEmail}>이메일 인증하기</button><br/>
                </>
            :
                <>
                <label>이메일 : {email}</label><br/>
                </>
                    
            }
            <label>포인트 : {memberInfo.point} p</label><br/>
            <label>가입날짜 : {regDate}</label>
        </div>
        <button type='button' onClick={onClickCartList}>장바구니</button>
        <button type='button' onClick={onClickPaymentList}>결제내역</button>
        <button type='button' onClick={onClickDeleteMember}>탈퇴하기</button>
        </>
        )
    };

export default Profile;