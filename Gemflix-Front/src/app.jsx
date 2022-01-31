import { BrowserRouter, Link, Route, Routes } from 'react-router-dom';
import './app.css';
import Home from './components/home';
import Profile from './components/profile';
import Login from './components/login';
import Join from './components/join';
import { React, useEffect } from 'react';
import { userLogin, userLogout } from './store/actions';
import { useCookies } from 'react-cookie';
import { useDispatch, useSelector } from 'react-redux';


function App({server}) {
  const [cookies, setCookie, removeCookie] = useCookies(["user"]);
  const dispatch = useDispatch();  
  const user = useSelector(store => store.userReducer);

  useEffect(() => {
    if (user.loggedIn) {
      const accessToken = user.token;
      server.authenticate(accessToken)
      .then(response => {
        console.log("authenticate requset success : " + response);
      })
      .catch(ex => {
        console.log("authenticate requset fail : " + ex);
      })
      .finally(() => {
          console.log("authenticate request end");
      });

      dispatch(userLogin(accessToken));
    }else{
      dispatch(userLogout());
      removeCookie('refreshToken');
    }
  }, []);

  return (
    <BrowserRouter>
    <nav>
      <Link to="/">Home</Link>
    </nav>
      <Routes>
        <Route path="/" exact element={<Home/>}></Route>
        <Route path="/home" element={<Home/>}></Route>
        <Route path="/profile" element={<Profile />}></Route>
        <Route path="/join" element={<Join server={server}/>}></Route>
        <Route path="/login" element={<Login server={server}/>}></Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
