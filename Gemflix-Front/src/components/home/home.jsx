import { React } from 'react';
import Header from './header';
import Navbar from './navbar';
import Footer from './footer';


const Home = ({server, onClickLogout }) => {
    
    return (
        <>
        <Header/>
        <Navbar server={server} onClickLogout={onClickLogout}/>
        <div>
            <h1>메인페이지</h1>
        </div>
        <Footer/>
        </>
    );
}
export default Home;