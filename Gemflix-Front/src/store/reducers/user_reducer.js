const defaultState = {
    loggedIn: false,
    token: null,
    memberId: null,
    memberRole: null
}

const userReducer = (state = defaultState, action) => {
    switch(action.type){
        case "LOGIN":
            return {
                loggedIn: true,
                token: action.accessToken,
                memberId: action.memberId,
                memberRole: action.memberRole
            }
        case "LOGOUT":
            return {
                loggedIn: false,
                token: null
            }
        default:
            return state
    }
}

export default userReducer;