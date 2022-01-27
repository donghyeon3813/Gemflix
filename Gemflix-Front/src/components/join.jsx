import React from 'react';
import { useRef, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

const Join = ({server}) => {
    const navigate = useNavigate();
    const joinFormRef = React.createRef();
    const idInputRef = React.createRef();
    const passwordInputRef = React.createRef();
    const phoneInputRef = React.createRef();
    const emailInputRef = React.createRef();

    const [response, setReponse] = useState([]);
    const [requestCnt, setRequestCnt] = useState(0);

    useEffect(() => {
        console.log("회원가입 진행중 ... ");
    })

    const onJoinClick = (event) => {
        event.preventDefault();
        const id = idInputRef.current.value;
        const password = passwordInputRef.current.value;
        const phone = phoneInputRef.current.value;
        const email = emailInputRef.current.value;

        if(id && password && phone && email){
            console.log("[Join] id: " + id + ", password: " + password + ", phone: " + phone + ", email: " + email);
            //server reqeust
            server.register(id, password, phone, email)
            .then(response => {
                setReponse(response);
                setRequestCnt(requestCnt + 1);
            });
        }else{
            alert("모든 정보를 입력해주세요.");
        }
    }

    //response가 업데이트 될 때만 특정 함수를 실행
    const mounted = useRef(false);
    useEffect(() => {
        if(!mounted.current){
            mounted.current = true;
        } else { //success
            if(response == 200){
                alert("회원님의 메일로 인증메일이 발송되었습니다.\n메일인증을 완료해주세요!\n(인증 미완료시 활동 범위가 제한됩니다.)");
                joinFormRef.current.reset();
                //로그인페이지로 이동
                navigate('/');
            }else{ //fail
                const json = JSON.parse(response);
                const errorCode = json.message.errorCode;
                const errorMessage = json.message.errorMessage;
                console.log("errorCode: "+errorCode);
                if(errorCode == 1001){
                    alert("이미 사용중인 아이디입니다.");
                }else{
                    alert(errorMessage);
                }
            }
        }
      }, [requestCnt]);

    return (
        <div>
            <h2>회원가입 페이지</h2>
            <form ref={joinFormRef} className="join-form" onSubmit={onJoinClick}>
                <label>아이디 : </label>
                <input ref={idInputRef} type="text" placeholder="id" name='input_id'/><br />
                <label>비밀번호 : </label>
                <input ref={passwordInputRef} type="password" placeholder="password" name='input_password'/><br />
                <label>핸드폰 번호 : </label>
                <input ref={phoneInputRef} type="tel" placeholder="phone" name='input_phone'/><br />
                <label>이메일 : </label>
                <input ref={emailInputRef} type="email" placeholder="email" name='input_email'/>
            </form>
            <button type="button" className="join-button" onClick={onJoinClick}>가입하기</button>
        </div>
    );
};

export default Join;