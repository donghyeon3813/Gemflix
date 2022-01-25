import React from 'react';
import { useNavigate } from 'react-router-dom';

const Home = (props) => {
    const navigate = useNavigate();
    return (
        <>
            <h1>메인페이지</h1>
            {/* <button onClick={()=> { navigate('/profile'); }}>마이페이지</button><br /> */}
            <button onClick={()=> { navigate('/join'); }}>회원가입</button><br />
            <button onClick={()=> { navigate('/login'); }}>로그인</button>
        </>
        )
    };

export default Home;