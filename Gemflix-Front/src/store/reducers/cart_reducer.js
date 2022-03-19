const defaultState = [];

const cartReducer = (state = defaultState, action) => {
    switch(action.type){
        case "ADD_ITEM":
            let id = 0;
            if(0 < state.length){
                id = state[state.length - 1].id + 1;
            }
            state = [...state, {
                id: id,
                memberId: action.payload.memberId,
                count: action.payload.count,
                totalPrice: action.payload.totalPrice,
                item: action.payload.item
            }];
            return state;

        case "DELETE_ITEM":
            return [...action.payload];

        case "RESET":
            return [];

        default :
            return state;
    }
}

export default cartReducer;