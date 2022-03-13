import { combineReducers } from "redux";
import userReducer from "./user_reducer";
import cartReducer from "./cart_reducer";

// redux 에서 제공하는 combineReducers 를 통해 주제별로 나눈 조각 reducer들을 rootReducer 로 모아준다.
const rootReducer = combineReducers({
    userReducer, cartReducer
})

export default rootReducer;