class AuthService{
    constructor(httpClient){
        this.server = httpClient;
    }

    //회원가입
    async register(id, password, phone, email) {
        return await this.server.post('/register', {
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

    //로그인
    async authenticate(id, password) {
        return await this.server.post('/authenticate', {
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
}

export default AuthService;