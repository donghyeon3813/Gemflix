import { React, memo } from 'react';

const Footer = memo(() => {
    return (
        <div className='footer' >
            <p>상호 : Gemflix | 팀원 : 오동현, 임채린</p>
            <p>주소 : 서울특별시 중구 남대문로 120 젬플릭스</p>
            <p>서비스 이용문의 : temp@gmail.com | 서비스제휴문의 : temp@gmail.com</p>
            <p>Copyright © Gemflix. All Rights Reserved.</p>
        </div>
    );
});

export default Footer;