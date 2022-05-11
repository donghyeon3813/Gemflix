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
            <div className='login_page'>
                <div className='login_box'>
                    <h1>로그인</h1><br/>
                    <form ref={loginFormRef} className="login-form">
                        <input className='form_box_input' style={{width: '300px'}} ref={idInputRef} type="text" placeholder="ID" name='input_id'/><br />
                        <input className='form_box_input' style={{width: '300px'}} ref={passwordInputRef} type="password" placeholder="PASSWORD" name='input_password' onKeyPress={handleKeyPress}/><br />
                    </form>
                </div>
                <div className='login_box'>
                    <button className="indigo_btn" type="button" onClick={onClickLogin}>로그인</button>
                    <button className="indigo_btn" type='button' onClick={()=> { navigate('/join'); }}>회원가입</button>
                </div>
                <br/>
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
          <div className='loading_box'>
            <img className='loading_img' src="images/default/loading.jpg"/>
          </div>
        )
    }
}

export default Login;