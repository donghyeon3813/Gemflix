// Action Creators
export const userLogin = (payload) => {
    return {
        type: "LOGIN",
        payload
    }
}

export const userLogout = () => {
    return {
        type: "LOGOUT"
    }
}
