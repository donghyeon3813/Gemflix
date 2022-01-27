import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const Login = ({server, loginCallBack}) => {
    const navigate = useNavigate();
    const loginFormRef = React.createRef();
    const idInputRef = React.createRef();
    const passwordInputRef = React.createRef();


    useEffect(() => {
        console.log("로그인 진행중 ... ");
    })

    const onClickLogin = event => {
        event.preventDefault();
        const id = idInputRef.current.value;
        const password = passwordInputRef.current.value;
        console.log("id: " + id, ", password: " + password);

        //server reqeust
        server.authenticate(id, password)
        .then(response => {
            const authToken = response.accessToken.value;
            console.log("authToken : " + authToken);
            //server인스턴스가 생성 된 후 기본값 변경
            axios.defaults.headers.common['Authorization'] = 'Bearer ' + authToken;
            loginCallBack(true);
            navigate('/');
        })
        .catch(ex => {
            console.log("login requset fail : " + ex);
        })
        .finally(() => {console.log("login request end")});
        loginFormRef.current.reset();
    }

    return (
        <div>
            <h1>로그인페이지</h1>
            <form ref={loginFormRef} className="login-form" onSubmit={onClickLogin}>
                <label>아이디 : </label>
                <input ref={idInputRef} type="text" placeholder="id" name='input_id'/><br />
                <label>비밀번호 : </label>
                <input ref={passwordInputRef} type="password" placeholder="password" name='input_password'/><br />
            </form>
            <button type="button" className="login-button" onClick={onClickLogin}>로그인</button>
        </div>
        );
    };

export default Login;