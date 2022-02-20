import React from 'react';
import { useRef, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

const Join = ({server}) => {
    const navigate = useNavigate();
    const joinFormRef = useRef();
    const idInputRef = useRef();
    const passwordInputRef = useRef();
    const phoneInputRef = useRef();
    const emailInputRef = useRef();

    const [response, setReponse] = useState([]);
    const [requestCnt, setRequestCnt] = useState(0);
    const [loading , setLoading] = useState(false);

    const onClickJoin = (event) => {
        setLoading(true);
        const id = idInputRef.current.value;
        const password = passwordInputRef.current.value;
        const phone = phoneInputRef.current.value;
        const email = emailInputRef.current.value;

        if(id && password && phone && email){
            //server reqeust
            server.register(id, password, phone, email)
            .then(response => {
                setReponse(response);
                setRequestCnt(requestCnt + 1);
                setLoading(false);
            });
        }else{
            alert("모든 정보를 입력해주세요.");
            setLoading(false);
        }
    }

    //response가 업데이트 될 때만 특정 함수를 실행
    const mounted = useRef(false);
    useEffect(() => {
        if(!mounted.current){
            mounted.current = true;
        } else { //success
            if(response === 200){
                alert("회원님의 메일로 인증메일이 발송되었습니다.\n메일인증을 완료해주세요!\n(인증 미완료시 활동 범위가 제한됩니다.)");
                //로그인페이지로 이동
                navigate('/');
            }else{ //fail
                const json = JSON.parse(response);
                const errorCode = json.message.errorCode;
                const errorMessage = json.message.errorMessage;
                switch(errorCode){
                    case 1001:
                        alert("이미 사용중인 아이디입니다.");
                        break;
                    case 1010:
                        alert("이미 사용중인 이메일입니다.");
                        break;
                    default:
                        alert(errorMessage);
                        break;
                }
            }
        }
    }, [requestCnt]);

    const handleKeyPress = (event) => {
        if (event.key === "Enter") {
            onClickJoin();
        }
    };

    if(!loading){
        return (
            <>
            <div>
                <h2>회원가입 페이지</h2>
                <form ref={joinFormRef} className="join-form" onSubmit={onClickJoin}>
                    <label>아이디 : </label>
                    <input ref={idInputRef} type="text" placeholder="id"/><br />
                    <label>비밀번호 : </label>
                    <input ref={passwordInputRef} type="password" placeholder="password"/><br />
                    <label>핸드폰 번호 : </label>
                    <input ref={phoneInputRef} type="tel" placeholder="phone"/><br />
                    <label>이메일 : </label>
                    <input ref={emailInputRef} type="email" placeholder="email" onKeyPress={handleKeyPress}/>
                </form>
                <button type="button" className="join-button" onClick={onClickJoin}>가입하기</button>
            </div>
            </>
        );
    }else{
        return (
          <div>
            Loading ....
          </div>
        )
    }
};

export default Join;