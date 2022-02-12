import { React, useRef, useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { userLogin } from '../../store/actions';
import { useDispatch, useSelector, shallowEqual } from 'react-redux';
import { useCookies } from 'react-cookie';
import Header from '../home/header';
import Footer from '../home/footer';


const Login = ({server, checkLogin}) => {
    const JWT_REFRESH_TOKEN_EXPIRE = 10; //10초

    const [cookies, setCookie, removeCookie] = useCookies(["refreshToken"]);
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const user = useSelector(store => store.userReducer, shallowEqual);

    const loginFormRef = useRef();
    const idInputRef = useRef();
    const passwordInputRef = useRef();
    const [loading , setLoading] = useState(false);

    useEffect(() => {
        console.log("user.loggedIn: " + user.loggedIn);
        if(user.loggedIn){
            checkLogin();
        }
    }, [user]);

    const onClickLogin = event => {
        setLoading(true);
        event.preventDefault();
        const id = idInputRef.current.value;
        const password = passwordInputRef.current.value;
        console.log("id: " + id, ", password: " + password);

        //server reqeust
        server.login(id, password)
        .then(response => {
            console.log("response: " + response);
            if(response.accessToken){
                const accessToken = response.accessToken;
                const refreshToken = response.refreshToken;
                if(accessToken && refreshToken){
                    setCookie('refreshToken', refreshToken, {
                        path: '/'
                        , secure: true
                        , maxAge: JWT_REFRESH_TOKEN_EXPIRE
                        // , httpOnly: true //도메인에만 적용가능
                    });
                    dispatch(userLogin(accessToken));
                    navigate('/');
                }
            }else{
                alert("아이디와 비밀번호를 정확히 입력해주세요.");
                setLoading(false);
            }
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
            <>
            <Header/>
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
            <Footer/>
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