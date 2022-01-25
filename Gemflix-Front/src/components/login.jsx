import React, { useState, useEffect } from 'react';
import axios from 'axios';

const Login = (props) => {
    const loginFormRef = React.createRef();
    const idInputRef = React.createRef();
    const pwInputRef = React.createRef();

    const onClickLogin = event => {
        event.preventDefault();
        const id = idInputRef.current.value;
        const pw = pwInputRef.current.value;
        console.log("id: " + id, ", pw: " + pw);
        loginFormRef.current.reset();
    }

    return (
        <div>
            <h2>Login</h2>
            <form ref={loginFormRef} className="login-form" onSubmit={onClickLogin}>
                <label>ID : </label>
                <input ref={idInputRef} type="text" placeholder="id"  name='input_id'/><br />
                <label>PW : </label>
                <input ref={pwInputRef} type="text" placeholder="password"  name='input_pw'/>
            </form>
            <button className="login-button" onClick={onClickLogin}>Login</button>
        </div>
        );
    };

export default Login;