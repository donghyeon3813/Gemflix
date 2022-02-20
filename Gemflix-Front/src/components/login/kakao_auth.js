import { React, useEffect } from 'react';

const KakaoAuth = ({server, settingAccessToken}) => {

    const code = new URL(window.location.href).searchParams.get("code");
    
    useEffect(() => {
        console.log("code: " + code);
        if(code !== null){
            server.kakaoLogin(code)
            .then(response => {
                settingAccessToken(response);
            })
            .catch(ex => {
                console.log("kakao login requset fail : " + ex);
            })
            .finally(() => {
                console.log("kakao login request end");
                
            });
        }
    }, [code]);

    return (
        <div>
            Loading ....
        </div>
    );
};

export default KakaoAuth;