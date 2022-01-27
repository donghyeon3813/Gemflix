import { BrowserRouter, Link, Route, Routes } from 'react-router-dom';
import './app.css';
import Home from './components/home';
import Profile from './components/profile';
import Login from './components/login';
import Join from './components/join';
import { useState } from 'react';

function App({server, id, pw, phone, email}) {
  const [isLogin , setIsLogin] = useState(false);

  const loginCallBack = login => {
    setIsLogin(login);
}

  return (
    <BrowserRouter>
    <nav>
      <Link to="/">Home</Link>
    </nav>
      <Routes>
        <Route path="/" exact element={<Home />}></Route>
        <Route path="/home" element={<Home />}></Route>
        <Route path="/profile" element={<Profile />}></Route>
        <Route path="/join" element={<Join server={server}/>}></Route>
        {/* <Route path="/login" element={<Login />}></Route> */}
        <Route path="/login" element={<Login loginCallBack={loginCallBack} server={server}/>} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
