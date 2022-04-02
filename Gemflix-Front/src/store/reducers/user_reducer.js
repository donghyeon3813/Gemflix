const defaultState = {
    loggedIn: false,
    token: null,
    memberId: null,
    memberRole: null
}

const userReducer = (state = defaultState, action) => {
    switch(action.type){
        case "LOGIN":
            return {...state,
                loggedIn: true,
                token: action.payload.accessToken,
                memberId: action.payload.memberId,
                memberRole: action.payload.memberRole
            };
        case "LOGOUT":
            return {...state,
                loggedIn: false,
                token: null,
                memberId: null,
                memberRole: null
            };
        default:
            return state
    }
}

export default userReducer;