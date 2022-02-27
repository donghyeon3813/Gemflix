import { React, useEffect, useState} from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { userLogin } from '../../store/actions';
import { useDispatch, useSelector, shallowEqual } from 'react-redux';

const Navbar = ({server, onClickLogout}) => {

    const [retryProfile , setRetryProfile] = useState(false);
    const user = useSelector(store => store.userReducer, shallowEqual);
    const dispatch = useDispatch();
    const navigate = useNavigate();

    useEffect(() => {
        if(retryProfile){
            onClickProfile();
        }
    }, [user]);

    const onClickProfile = () => {
        //server reqeust
        server.profile(user.token)
        .then(response => {
            const code = response.code;
            console.log("profile code: " + code);
            switch(code){
                case 1000: //success
                    console.log("member: " + response.data.id + response.data.password + response.data.phone);
                    navigate('/profile', { memeber: response.data });
                    break;

                case 1007: //accessToken 만료 -> 재발급
                    server.refresh()
                    .then(response => {
                        const code = response.code;
                        console.log("refresh code: " + code);
                        switch(code){
                            case 1000: //accessToken 재발급 성공 -> 재요청
                                setRetryProfile(true);
                                dispatch(userLogin(response.data.accessToken, response.data.memberId, response.data.memberRole));
                                break;

                            case 1008: //refreshToken 만료 -> 로그아웃
                                onClickLogout(true);
                                break;

                            default: //fail
                                alert("해당 작업을 수행할 수 없습니다. 잠시 후 다시 시도해주세요.");
                                navigate('/');
                                break;
                        }
                    })
                    .catch(ex => {
                        console.log("refresh requset fail : " + ex);
                    })
                    .finally(() => {
                        console.log("refresh request end");
                    });
                    break;

                default: //fail
                    alert("해당 작업을 수행할 수 없습니다. 잠시 후 다시 시도해주세요.");
                    navigate('/');
                    break;
            }
        })
        .catch(ex => {
            console.log("profile requset fail : " + ex);
        })
        .finally(() => {
            console.log("profile request end");
        });
    }

    const onClickLogoutBtn = () => {
        onClickLogout(false);
    }

    if(user.loggedIn){
        return (
            <>
            <nav className='navbar'>
              <ul className='navbar_menu'>
                <li><Link to="/reserve">예매</Link></li>
                <li><Link to="/movies">영화</Link></li>
                <li><Link to="/products">스토어</Link></li>
              </ul>
              <ul className='navbar_member_button'>
                <li>{user.memberId} 님</li>
                <li><button type='button' onClick={onClickProfile}>마이페이지</button></li>
                <li><button type='button' onClick={onClickLogoutBtn}>로그아웃</button></li>
              </ul>
            </nav>
            </>
        );
    }else{
        return (
            <>
            <nav className='navbar'>
              <ul className='navbar_menu'>
                <li><Link to="/reserve">예매</Link></li>
                <li><Link to="/movies">영화</Link></li>
                <li><Link to="/products">스토어</Link></li>
              </ul>
              <ul className='navbar_member_button'>
                <li><button type='button' onClick={()=> { navigate('/login'); }}>로그인</button></li>
              </ul>
            </nav>
            </>
        );
    }
};

export default Navbar;