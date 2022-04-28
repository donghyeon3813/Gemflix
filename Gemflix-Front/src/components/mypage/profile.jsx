import React, { useEffect, useState } from 'react';
import { shallowEqual, useSelector } from 'react-redux';
import { useLocation, useNavigate } from 'react-router';

const Profile = ({server, onClickLogout}) => {

    const location = useLocation();
    const navigate = useNavigate();
    const user = useSelector(store => store.userReducer, shallowEqual);

    const [email, setEmail] = useState(null);
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
        navigate('/payments', {
            state: {
                memberInfo: memberInfo
            }
        });
    }

    const onClickDeleteMember = () => {
        if(window.confirm("정말 탈퇴하겠습니까?")){
            server.deleteMember(user.memberId)
            .then(response => {
                const code = response.code;
                switch(code){
                    case 1007: //interceptor에서 accessToken 재발급
                        break;

                    case 1000: //success
                        alert("탈퇴되었습니다.");
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

    return (
        <>
        <div>
            <h1>Profile</h1>
            <label>아이디 : {memberInfo.id}</label><br/>
            <label>핸드폰 번호 : {memberInfo.phone}</label><br/>
            {email == null ? 
                <>
                <label>이메일 : </label>
                <button type='button'>이메일 인증하기</button><br/>
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
        <button type='button'>관람영화</button>
        <button type='button' onClick={onClickDeleteMember}>탈퇴하기</button>
        </>
        )
    };

export default Profile;