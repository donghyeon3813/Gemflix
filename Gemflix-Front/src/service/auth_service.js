class AuthService{
    constructor(httpClient){
        this.server = httpClient;
    }

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
}

export default AuthService;