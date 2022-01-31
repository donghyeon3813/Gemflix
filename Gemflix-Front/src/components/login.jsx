import { React, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { useDispatch } from 'react-redux';
import { userLogin } from '../store/actions';


const Login = ({server}) => {
    const navigate = useNavigate();
    const dispatch = useDispatch();

    const loginFormRef = useRef();
    const idInputRef = useRef();
    const passwordInputRef = useRef();
    const [loading , setLoading] = useState(false);

    const onClickLogin = event => {
        setLoading(true);
        event.preventDefault();
        const id = idInputRef.current.value;
        const password = passwordInputRef.current.value;
        console.log("id: " + id, ", password: " + password);

        //server reqeust
        server.login(id, password)
        .then(response => {
            const accessToken = response.accessToken.value;
            // API 요청하는 콜마다 헤더에 accessToken 담아 보내도록 설정
            axios.defaults.headers.common['Authorization'] = 'Bearer ' + accessToken;
            dispatch(userLogin(accessToken));
            navigate('/');
        })
        .catch(ex => {
            console.log("login requset fail : " + ex);
            setLoading(false);
            loginFormRef.current.reset();
        })
        .finally(() => {
            console.log("login request end");
        });
        
    }

    if(!loading){
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
    }else{
        return (
          <div>
            Loading ....
          </div>
        )
    }
}

export default Login;