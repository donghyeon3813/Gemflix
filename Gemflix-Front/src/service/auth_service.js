import axios from "axios";

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

    //인증 (accessToken, refreshToken 유효성 검사 및 accessToken 재발급)
    async authenticate() {
        return await this.server.post('/authenticate')
        .then(function (success) {
            return success.data;
        })
        .catch(function (error) {
            return error.request.response;
        });
    }
}

export default AuthService;