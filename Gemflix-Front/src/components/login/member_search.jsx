import React, { useRef, useState } from 'react';
import { useNavigate } from 'react-router';

const MemberSearch = ({server}) => {

    const navigate = useNavigate();
    const phoneInputRef = useRef();
    const randomNumberInputRef = useRef();
    const [disStatus, setDisStatus] = useState('I');

    const searchIdDisplayType = disStatus === 'I' ? {display:"block"} : {display:"none"};
    const searchPwDisplayType = disStatus === 'P' ? {display:"block"} : {display:"none"};

    const certifyMemberPhone = () => {
        const phone = phoneInputRef.current.value;
        console.log(phone);

        if(phone){
            //server reqeust
            server.certifyPhone(phone)
            .then(response => {
                alert("인증번호가 발송되었습니다.");
            })
            .catch(ex => {
                console.log("certifyPhone requset fail : " + ex);
            })
            .finally(() => {
                console.log("certifyPhone request end");
            });
        }else{
            alert("핸드폰 번호를 입력해주세요.");
        }
    }

    const certifyRandomNumber = () => {
        const phone = phoneInputRef.current.value;
        const randomNumber = randomNumberInputRef.current.value;
        console.log(randomNumber);

        if(randomNumber){
            //server reqeust
            server.certifyRandomNumber(phone, randomNumber)
            .then(response => {
                const code = response.code;
                const memberIds = response.data;

                switch(code){
                    case 1000: //success
                        alert("본인인증이 완료되었습니다.");
                        navigate('/modify', {
                            state: {
                                memberIds: memberIds
                            }
                        }); //비밀번호 수정 페이지로 이동
                        break;

                    default: //fail
                        alert(response.message);
                        break;
                }    
            })
            .catch(ex => {
                console.log("certifyPhone requset fail : " + ex);
            })
            .finally(() => {
                console.log("certifyPhone request end");
            });
        }else{
            alert("핸드폰 번호를 입력해주세요.");
        }
    }

    const changeDisplay = (event) => {
        setDisStatus(event.target.value);
};

    return (
        <>
        <button type='button' value='I' onClick={changeDisplay}>아이디찾기</button>
        <button type='button' value='P' onClick={changeDisplay}>비밀번호 찾기</button>

        {/* 아이디찾기 */}
        <div style={searchIdDisplayType}>
            <label>핸드폰 번호 입력 : </label>
            <input ref={phoneInputRef} type="tel" placeholder="phone" name='search_phone'/>
            <button type='button' onClick={certifyMemberPhone}>인증번호 발송</button><br/>
            <input ref={randomNumberInputRef} type="text" placeholder="random" name='random_number'/>
            <button type='button' onClick={certifyRandomNumber}>인증하기</button>
        </div>

        {/* 비밀번호 찾기 */}
        <div style={searchPwDisplayType}>
            <label>아이디 : </label>
            <input ref={phoneInputRef} type="tel" placeholder="phone" name='search_phone'/>
        </div>
        </>
    );
}

export default MemberSearch;