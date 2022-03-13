import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router';

const Profile = () => {

    const location = useLocation();
    const navigate = useNavigate();
    const [email, setEmail] = useState(null);
    const [regDate, setRegDate] = useState(null);

    useEffect(() => {
        let regDt = location.state.memberInfo.regDate;
        setRegDate(String(regDt).replace("T", " "));
        setEmail(location.state.memberInfo.email);
    }, []);

    const onClickCartList = () => {
        navigate('/cartList');
    }

    return (
        <>
        <div>
            <h1>Profile</h1>
            <label>아이디 : {location.state.memberInfo.id}</label><br/>
            <label>핸드폰 번호 : {location.state.memberInfo.phone}</label><br/>
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
            <label>포인트 : {location.state.memberInfo.point} p</label><br/>
            <label>가입날짜 : {regDate}</label>
        </div>
        <button type='button' onClick={onClickCartList}>장바구니</button>
        <button type='button'>결제내역</button>
        <button type='button'>관람영화</button>
        <button type='button'>탈퇴하기</button>
        </>
        )
    };

export default Profile;