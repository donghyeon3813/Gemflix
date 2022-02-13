const defaultState = {
    loggedIn: false,
    token: null
}

const userReducer = (state = defaultState, action) => {
    switch(action.type){
        case "LOGIN":
            return {
                loggedIn: true,
                token: action.payload
            }
        case "LOGOUT":
            localStorage.clear()
            return {
                loggedIn: false,
                token: null
            }
        default:
            return state
    }
}

export default userReducer;