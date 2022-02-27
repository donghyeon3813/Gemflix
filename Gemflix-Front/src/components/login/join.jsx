import React from 'react';
import { useRef, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

const Join = ({server}) => {
    const navigate = useNavigate();
    const [response, setReponse] = useState([]);
    const [requestCnt, setRequestCnt] = useState(0);
    const [loading , setLoading] = useState(false);
    const [id , setId] = useState('');
    const [password , setPassword] = useState('');
    const [phone , setPhone] = useState('');
    const [email , setEmail] = useState('');

    const onClickJoin = (event) => {
        setLoading(true);

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
            console.log("response: " + response);

            if(response.code === 1000){
                alert("회원님의 메일로 인증메일이 발송되었습니다.\n메일인증을 완료해주세요!\n(인증 미완료시 활동 범위가 제한됩니다.)");
                //로그인페이지로 이동
                navigate('/');
            }else{ //fail
                alert(response.message);
            }
        }
    }, [requestCnt]);

    const handleKeyPress = (event) => {
        if (event.key === "Enter") {
            onClickJoin();
        }
    };

    const changeId = (event) => {
        setId(event.target.value);
    }

    const changePassword = (event) => {
        setPassword(event.target.value);
    }

    const changePhone = (event) => {
        setPhone(event.target.value);
    }

    const changeEmail = (event) => {
        setEmail(event.target.value);
    }

    if(!loading){
        return (
            <>
            <div>
                <h2>회원가입 페이지</h2>
                <form className="join-form" onSubmit={onClickJoin}>
                    <label>아이디 : </label>
                    <input value={id} type="text" placeholder="id" onChange={changeId}/><br />
                    <label>비밀번호 : </label>
                    <input value={password} type="password" placeholder="password" onChange={changePassword}/><br />
                    <label>핸드폰 번호 : </label>
                    <input value={phone} type="tel" placeholder="phone" onChange={changePhone}/><br />
                    <label>이메일 : </label>
                    <input value={email} type="email" placeholder="email" onChange={changeEmail} onKeyPress={handleKeyPress}/>
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