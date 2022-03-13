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

export const addCart = (item) => {
    return {
          type: "ADD_ITEM",
          payload: item
    }
}

export const deleteCart = (items) => {
    return {
        type:"DELETE_ITEM",
        payload:items
    }
 }