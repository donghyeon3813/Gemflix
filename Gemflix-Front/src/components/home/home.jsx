import { React } from 'react';
import { useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { userLogout } from '../../store/actions';
import { useCookies } from 'react-cookie';


const Home = () => {
    const [cookies, setCookie, removeCookie] = useCookies(["user"]);
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const user = useSelector(store => store.userReducer);

    const onClickLogout = () => {
        window.localStorage.setItem('logout', Date.now());
        dispatch(userLogout());
        removeCookie('refreshToken');
    }

    if(user.loggedIn){
        return (
            <>
                <h1>메인페이지</h1>
                <button onClick={()=> { navigate('/profile'); }}>마이페이지</button><br />
                <button onClick={onClickLogout}>로그아웃</button>
            </>
        )
    }else{
        return (
            <>
                <h1>메인페이지</h1>
                <button onClick={()=> { navigate('/join'); }}>회원가입</button><br />
                <button onClick={()=> { navigate('/login'); }}>로그인</button>
            </>
        )
    }

}
export default Home;