class AuthService{
    constructor(httpClient){
        this.server = httpClient;
    }

    //회원가입
    async register(id, password, phone, email) {
        return await this.server.post('/auth/register', {
            id: id,
            password: password,
            phone: phone,
            email: email
        })
        .then(function (success) {
            return success.data.status;
        })
        .catch(function (error) {
            return error.request.response;
        });
    }

    //로그인 (token 발급)
    async login(id, password) {
        return await this.server.post('/auth/login', {
            username: id,
            password: password
        })
        .then(function (success) {
            return success.data;
        })
        .catch(function (error) {
            return error.request.response;
        });
    }

    //accessToken 재발급 요청
    async refresh() {
        return await this.server.post('/auth/refresh')
        .then(function (success) {
            return success.data;
        })
        .catch(function (error) {
            const response = error.request.response;
            const json = JSON.parse(response);
            if(json.status === 401){
                return json.message.errorCode;
            }else{
                return null;
            }
        });
    }

    //회원 프로필
    async profile(accessToken) {
        return await this.server.post('/member/profile', {}, {
            headers: {Authorization: 'Bearer ' + accessToken}
        })
        .then(function (success) {
            return success.data;
        })
        .catch(function (error) {
            //return
            const response = error.request.response;
            const json = JSON.parse(response);
            if(json.status === 401){
                return json.message.errorCode;
            }else{
                return null;
            }
        });
    }
    
}

export default AuthService;