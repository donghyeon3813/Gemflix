import { React, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';

const Login = ({server, kakaoLoginUrl, settingAccessToken}) => {
    
    const navigate = useNavigate();
    const loginFormRef = useRef();
    const idInputRef = useRef();
    const passwordInputRef = useRef();
    const [loading , setLoading] = useState(false);

    const onClickLogin = event => {
        setLoading(true);
        const id = idInputRef.current.value;
        const password = passwordInputRef.current.value;

        //server reqeust
        server.login(id, password)
        .then(response => {
            settingAccessToken(response);
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

    const handleKeyPress = (event) => {
        if (event.key === "Enter") {
            onClickLogin();
        }
    };

    if(!loading){
        return (
            <>
            <div>
                <div>
                    <h1>로그인페이지</h1>
                    <form ref={loginFormRef} className="login-form">
                        <label>아이디 : </label>
                        <input ref={idInputRef} type="text" placeholder="id" name='input_id'/><br />
                        <label>비밀번호 : </label>
                        <input ref={passwordInputRef} type="password" placeholder="password" name='input_password' onKeyPress={handleKeyPress}/><br />
                    </form>
                    <button type="button" className="login-button" onClick={onClickLogin}>로그인</button>
                </div>
                <div>
                    <button type='button' onClick={()=> { navigate('/join'); }}>회원가입</button>
                    <button type='button' onClick={()=> { navigate('/search'); }}>ID / PW 찾기</button>
                </div>
                <div>
                    <a href={kakaoLoginUrl}>
                        <img src="images/default/kakao_login_medium_wide.png"/>
                    </a>
                </div>
            </div>
            </>
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