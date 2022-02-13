import React from 'react';

const MovieReserve = () => {
    return (
        <div className='reserve_box'>
            <div className='reserve_step'>
                <ul>
                    <li>상영시간</li>
                    <li>인원/좌석</li>
                    <li>결제</li>
                    <li>결제완료</li>
                </ul>
            </div>
            <div className='reserve_group'>
                <div className='reserve_group_step'>
                    <div className='group_top'>
                        <h4 class="title">영화관</h4>
                    </div>
                    <div className='inner'></div>
                </div>
                <div className='reserve_group_step'>
                    <div className='group_top'>
                        <h4 class="title">영화선택</h4>
                    </div>
                    <div className='inner'></div>
                </div>
                <div className='reserve_group_step'>
                    <div className='group_top'>
                        <h4 class="title">날짜</h4>
                    </div>
                    <div className='inner'></div>
                </div>
            </div>
        </div>
    );
};

export default MovieReserve;