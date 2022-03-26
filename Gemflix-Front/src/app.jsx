import { BrowserRouter, Route, Routes } from "react-router-dom";
import "./app.css";
import Home from "./components/home/home";
import Profile from "./components/login/profile";
import Login from "./components/login/login";
import Join from "./components/login/join";
import { React, useEffect, useState } from "react";
import { useCookies } from "react-cookie";
import { userLogout, userLogin } from "./store/actions";
import Header from "./components/home/header";
import Footer from "./components/home/footer";
import MovieReserve from "./components/movie/movie_reserve";
import KakaoAuth from "./components/login/kakao_auth";
import { useDispatch, useSelector, shallowEqual } from "react-redux";
import ProductCreateForm from "./components/product/product_create_form";
import ProductList from "./components/product/product_list";
import MovieView from "./components/movie/movie_view";
import MovieList from "./components/movie/movie_list";
import dotenv from "dotenv";
import CartList from "./components/cart/cart_list";
dotenv.config();

function App({ server, movieServer }) {
  const KAKAO_CLIENT_ID = process.env.REACT_APP_KAKAO_CLIENT_ID;
  const KAKAO_REDIRECT_URI = process.env.REACT_APP_KAKAO_REDIRECT_URI;

  const kakaoLoginUrl = `https://kauth.kakao.com/oauth/authorize?client_id=${KAKAO_CLIENT_ID}&redirect_uri=${KAKAO_REDIRECT_URI}&response_type=code`;

  const dispatch = useDispatch();
  const user = useSelector((store) => store.userReducer, shallowEqual);
  const [cookies, setCookie, removeCookie] = useCookies(["refreshToken"]);
  const [loading, setLoading] = useState(false);
  let alreadyLogout = false;

  useEffect(() => {
    if (user.loggedIn === true && user.token) {
      checkLogin();
    }
  }, [user.loggedIn]);

  function checkLogin() {
    console.log("checkLogin");
    setTimeout(function () {
      if (!alreadyLogout) {
        const existsRefreshToken = document.cookie.indexOf("refreshToken");
        if (existsRefreshToken < 0) {
          //로그아웃 됨
          onClickLogout(true);
        } else {
          checkLogin();
        }
      }
    }, 1000); //1초
  }

  const onClickLogout = (auto) => {
    if (auto) {
      //자동 로그아웃
      alert("세션이 만료되어 자동 로그아웃 되었습니다.");
    } else {
      //수동 로그아웃
      alert("로그아웃 되었습니다.");
      alreadyLogout = true;
    }
    window.localStorage.setItem("logout", Date.now());
    dispatch(userLogout());
    removeCookie("refreshToken", { path: "/" });
    window.location.href = "/";
  };

  const settingAccessToken = (response) => {
    const code = response.code;
    if (code === 1000) {
      //success
      const accessToken = response.data.accessToken;
      const refreshToken = response.data.refreshToken;
      const memberId = response.data.memberId;
      const memberRole = response.data.memberRole;
      const expire = response.data.expire;

      if (accessToken && refreshToken) {
        setCookie("refreshToken", refreshToken, {
          path: "/",
          secure: true,
          maxAge: expire,

          // , httpOnly: true //도메인에만 적용가능
        });
        dispatch(userLogin(accessToken, memberId, memberRole));
        alert(memberId + "님 환영합니다!");
        window.location.href = "/";
      }
    } else {
      //fail
      alert("아이디와 비밀번호를 정확히 입력해주세요.");
      setLoading(false);
      window.location.href = "/login";
    }
  };

  if (!loading) {
    return (
      <div className="app">
        <BrowserRouter>
          <Header />
          <Routes>
            {/* home */}
            <Route
              path="/"
              exact
              element={<Home server={server} onClickLogout={onClickLogout} />}
            ></Route>
            <Route
              path="/home"
              element={<Home server={server} onClickLogout={onClickLogout} />}
            ></Route>

            {/* login */}
            <Route path="/profile" element={<Profile />}></Route>
            <Route path="/cartList" element={<CartList />}></Route>
            <Route path="/join" element={<Join server={server} />}></Route>
            <Route
              path="/login"
              element={
                <Login
                  server={server}
                  kakaoLoginUrl={kakaoLoginUrl}
                  settingAccessToken={settingAccessToken}
                />
              }
            ></Route>
            <Route
              exact
              path="kakaoLoginUrl"
              element={<Home server={server} onClickLogout={onClickLogout} />}
            ></Route>
            <Route
              path="/auth/callback/kakao"
              element={
                <KakaoAuth
                  server={server}
                  settingAccessToken={settingAccessToken}
                />
              }
            ></Route>

            {/* movie */}
            <Route
              path="/movies"
              exact
              element={<MovieList movieServer={movieServer} />}
            ></Route>
            <Route
              path="/movies/view"
              exact
              element={<MovieView movieServer={movieServer} />}
            ></Route>
            <Route path="/reserve" exact element={<MovieReserve />}></Route>

            {/* product */}
            <Route
              path="/products"
              element={<ProductList server={server} />}
            ></Route>
            <Route
              path="/product/create"
              element={<ProductCreateForm server={server} />}
            ></Route>
          </Routes>
          <Footer />
        </BrowserRouter>
      </div>
    );
  } else {
    return <div>Loading ....</div>;
  }
}

export default App;
