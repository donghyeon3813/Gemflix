// Action Creators
export const userLogin = (accessToken, memberId, memberRole) => {
    return {
        type: "LOGIN",
        accessToken, memberId, memberRole
    }
}

export const userLogout = () => {
    return {
        type: "LOGOUT"
    }
}