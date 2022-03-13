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

export const addCart = (item, count, totalPrice, key) => {
    return {
          type: "ADD_ITEM",
          payload: {item, count, totalPrice, key}
    }
}

export const deleteCart = (items) => {
    return {
        type:"DELETE_ITEM",
        payload:items
    }
 }