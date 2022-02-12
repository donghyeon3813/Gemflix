import { BrowserRouter, Route, Routes } from 'react-router-dom';
import './app.css';
import Home from './components/home/home';
import Profile from './components/login/profile';
import Login from './components/login/login';
import Join from './components/login/join';
import { React } from 'react';
import MovieList from "./components/movie/list";
import { useCookies } from 'react-cookie';
import { userLogout } from './store/actions';
import { useDispatch } from 'react-redux';

function App({server}) {

  const dispatch = useDispatch();
  const [cookies, setCookie, removeCookie] = useCookies(["refreshToken"]);
  let alreadyLogout = false;

  function checkLogin() {
    setTimeout(function() {
      console.log("alreadyLogout: " + alreadyLogout);
      if(!alreadyLogout){
          const existsRefreshToken = document.cookie.indexOf("refreshToken");
          if(existsRefreshToken < 0){ //로그아웃 됨
            onClickLogout(true);
          }else{
            checkLogin();
          }
        }
    }, 1000); //1초
  }

  const onClickLogout = (auto) => {
    console.log("auto: " + auto);
    if(auto){ //자동 로그아웃
      alert("세션이 만료되어 자동 로그아웃 되었습니다.");
    }else{ //수동 로그아웃
      alert("로그아웃 되었습니다.");
      alreadyLogout = true;
      console.log("alreadyLogout: " + alreadyLogout);
    }
    window.localStorage.setItem('logout', Date.now());
    dispatch(userLogout());
    removeCookie('refreshToken', { path: '/' });
  }

  return (
    <>
    <BrowserRouter>
      <Routes>
        <Route path="/" exact element={<Home server={server} onClickLogout={onClickLogout}/>}></Route>
        <Route path="/home" element={<Home server={server} onClickLogout={onClickLogout}/>}></Route>
        <Route path="/profile" element={<Profile />}></Route>
        <Route path="/join" element={<Join server={server}/>}></Route>
        <Route path="/login" element={<Login server={server} checkLogin={checkLogin}/>}></Route>
        <Route path="/movies" exact element={<MovieList />}></Route>
      </Routes>
    </BrowserRouter>
    </>
  );
}

export default App;