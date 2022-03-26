import setupInterceptors from "./setup_interceptors";

class AuthService{
    constructor(httpClient){
        this.server = setupInterceptors(httpClient);
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
    async profile() {
        return await this.server.get('/member/profile', {})
        .then(function (success) {
            return success.data;
        })
        .catch(function (error) {
            return JSON.parse(error.request.response);
        });
    }

    //상품 카테고리
    async category() {
        return await this.server.get('/category', {})
        .then(function (success) {
            return success.data;
        })
        .catch(function (error) {
            return JSON.parse(error.request.response);
        });
    }

    //상품 등록
    async createProduct(formData) {
        return await this.server.post('/product', formData, {})
        .then(function (success) {
            return success.data;
        })
        .catch(function (error) {
            return JSON.parse(error.request.response);
        });
    }

    //상품 목록
    async products() {
        return await this.server.get('/products', {})
        .then(function (success) {
            return success.data;
        })
        .catch(function (error) {
            return JSON.parse(error.request.response);
        });
    }

    //상품 수정
    async modifyProduct(formData) {
        return await this.server.put('/product', formData, {})
        .then(function (success) {
            return success.data;
        })
        .catch(function (error) {
            return JSON.parse(error.request.response);
        });
    }

    //상품 삭제
    async deleteProduct(prId) {
        return await this.server.delete('/product', { 
            params: {
                prId: prId
            } 
        })
        .then(function (success) {
            return success.data;
        })
        .catch(function (error) {
            return JSON.parse(error.request.response);
        });
    }
    
    //결제 endpoint
    async completePayment(formData) {
        return await this.server.post('/payments/complete', {
            headers: { "Content-Type": "application/json" },
            formData
        }, {})
        .then(function (success) {
            console.log(success.data);
            return success.data;
        })
        .catch(function (error) {
            console.log(error.request.response);
            return JSON.parse(error.request.response);
        });
    }

}

export default AuthService;