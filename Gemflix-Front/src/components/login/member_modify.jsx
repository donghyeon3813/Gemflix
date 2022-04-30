import React from 'react';
import { useLocation } from 'react-router';

const MemberModify = (props) => {

    const location = useLocation();
    const memberIds = location.state.memberIds;

    return (
        <>
        <div>
            <p>회원님의 아이디 목록입니다.</p>
            {
                memberIds.map((id, index) => {
                    let tempId = String(id).substring(0,3) + "****";
                    return <p>{tempId}</p>
                })
            }
        </div>
        </>
    );
};

export default MemberModify;