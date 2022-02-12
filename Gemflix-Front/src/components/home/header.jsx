import React from 'react';
import { useNavigate } from 'react-router-dom';

const Header = () => {
    const navigate = useNavigate();

    return (
        <div onClick={()=> { navigate('/'); }}>
            header
        </div>
    );
};

export default Header;