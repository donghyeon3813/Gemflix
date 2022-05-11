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
            <div className='join_page'>
                <h1>회원가입</h1><br/>
                <form className="join_form" onSubmit={onClickJoin}>
                    <table className='join_table'>
                        <tr>
                            <td><h4>ID : </h4></td>
                            <td><input className='form_box_input' style={{width: '300px'}} value={id} type="text" placeholder="ID" onChange={changeId}/><br /></td>
                        </tr>
                        <tr>
                            <td><h4>PW : </h4></td>
                            <td><input className='form_box_input' style={{width: '300px'}} value={password} type="password" placeholder="PASSWORD" onChange={changePassword}/><br /></td>
                        </tr>
                        <tr>
                            <td><h4>PHONE : </h4></td>
                            <td><input className='form_box_input' style={{width: '300px'}} value={phone} type="tel" placeholder="PHONE" onChange={changePhone}/><br /></td>
                        </tr>
                        <tr>
                            <td><h4>EMAIL : </h4></td>
                            <td><input className='form_box_input' style={{width: '300px'}} value={email} type="email" placeholder="EMAIL" onChange={changeEmail} onKeyPress={handleKeyPress}/></td>
                        </tr>
                    </table>
                    <button className='indigo_btn' type="button" onClick={onClickJoin}>가입하기</button>
                </form>
            </div>
            </>
        );
    }else{
        return (
            <div className='loading_box'>
                <img className='loading_img' src="images/default/loading.jpg"/>
            </div>
        )
    }
};

export default Join;