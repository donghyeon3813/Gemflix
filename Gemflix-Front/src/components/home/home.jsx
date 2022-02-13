import { React } from 'react';
import Navbar from './navbar';


const Home = ({server, onClickLogout }) => {
    
    return (
        <div className='home'>
            <Navbar server={server} onClickLogout={onClickLogout}/>
            <div className='main'>
                <h1>메인페이지</h1>
            </div>
        </div>
    );
}
export default Home;