import { React, memo } from 'react';
import { useNavigate } from 'react-router-dom';

const Header = memo(() => {
    const navigate = useNavigate();

    return (
        <div className='header' onClick={()=> { navigate('/'); }}>
            Gemflix
        </div>
    );
});

export default Header;