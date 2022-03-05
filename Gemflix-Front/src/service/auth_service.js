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
            return success.data;
        })
        .catch(function (error) {
            return JSON.parse(error.request.response);
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
            return JSON.parse(error.request.response);
        });
    }

    //카카오 로그인
    async kakaoLogin(code) {
        return await this.server.post('/auth/callback/kakao', {
            code: code
        })
        .then(function (success) {
            return success.data;
        })
        .catch(function (error) {
            return error.request.response;
        });
    }

    //회원 프로필
    async profile(accessToken) {
        return await this.server.get('/member/profile', {
            headers: {Authorization: 'Bearer ' + accessToken}
        })
        .then(function (success) {
            return success.data;
        })
        .catch(function (error) {
            return JSON.parse(error.request.response);
        });
    }

    //상품 카테고리
    async category(accessToken) {
        return await this.server.get('/none/category', {
            headers: {Authorization: 'Bearer ' + accessToken}
        })
        .then(function (success) {
            return success.data;
        })
        .catch(function (error) {
            return JSON.parse(error.request.response);
        });
    }

    //상품 등록
    async createProduct(accessToken, formData) {
        return await this.server.post('/product', formData,
        {
            headers: {
                Authorization: 'Bearer ' + accessToken
            }
        })
        .then(function (success) {
            return success.data;
        })
        .catch(function (error) {
            return JSON.parse(error.request.response);
        });
    }

    //상품 목록
    async products(accessToken) {
        return await this.server.get('/none/products',
        {
            headers: {
                Authorization: 'Bearer ' + accessToken
            }
        })
        .then(function (success) {
            return success.data;
        })
        .catch(function (error) {
            return JSON.parse(error.request.response);
        });
    }

}

export default AuthService;