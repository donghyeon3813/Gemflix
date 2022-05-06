import { React } from 'react';
import Navbar from './navbar';


const Home = ({server, onClickLogout }) => {
    
    return (
        <div className='home'>
            <Navbar server={server} onClickLogout={onClickLogout}/>
            <img id='main' style={{width:"100%",height:"100%"}} src="images/default/main.jpg"/>
            
        </div>
    );
}
export default Home;