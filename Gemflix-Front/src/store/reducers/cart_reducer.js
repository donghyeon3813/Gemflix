const defaultState = [];

const cartReducer = (state = defaultState, action) => {
    switch(action.type){
        case "ADD_ITEM": //장바구니에 담기
            const createCart = () => {
                let memberId = action.payload.memberId;
                let prId = action.payload.item.prId;
                let name = action.payload.item.name;
                let price = action.payload.item.price;
                let base64 = action.payload.item.base64;

                let selectedCount = {
                    cId: prId + "_" + 1,
                    count: action.payload.count,
                    totalPrice: action.payload.totalPrice
                }

                let selectedCounts = [];
                selectedCounts = [...selectedCounts, selectedCount]
                
                let item = {
                    prId: prId,
                    name: name,
                    price: price,
                    base64: base64,
                    selectedCounts: selectedCounts
                }
                let items = [];
                items = [...items, item];
                return [...state, {
                    [memberId]: items 
                }];
            }

            let newState = null;
            let thisProductExists = false; //해당상품 유무
            let thisMemberCartExists = false; //해당멤버 장바구니 유무
            let memberId = action.payload.memberId;

            if(0 < state.length){ //모든 멤버의 장바구니

                newState = state.map(thisMember => { //멤버 cart 있음
                    if(Object.hasOwn(thisMember, memberId)){ //해당 멤버의 cart
                        thisMemberCartExists = true;
                        console.log("해당 멤버의 장바구니임")
                        let oldCart = thisMember;
                        let prId = action.payload.item.prId;
                        let name = action.payload.item.name;
                        let price = action.payload.item.price;
                        let base64 = action.payload.item.base64;

                        let oldItems = oldCart[memberId]; //[]

                        let newItems = oldItems.map(thisItem => {
                            if(thisItem.prId === prId){ //멤버 cart 에 product 있음
                                console.log("멤버 cart 에 product 있음")
                                thisProductExists = true;
                                let oldSelectedCounts = thisItem.selectedCounts; //[]
                                let newSelectedCount = {
                                    cId: prId + "_" + (oldSelectedCounts.length + 1),
                                    count: action.payload.count,
                                    totalPrice: action.payload.totalPrice
                                }
                                let newSelectedCounts = [...oldSelectedCounts, newSelectedCount];

                                let newItem = {
                                    prId: prId,
                                    name: name,
                                    price: price,
                                    base64: base64,
                                    selectedCounts: newSelectedCounts
                                }
                                return newItem;
                            }else{
                                return thisItem;
                            }
                        });

                        if(!thisProductExists){ //멤버 cart 에 product 없음
                            console.log("멤버 cart 에 product 없음")
                            let newSelectedCount = {
                                cId: prId + "_" + 1,
                                count: action.payload.count,
                                totalPrice: action.payload.totalPrice
                            }
                            let newSelectedCounts = [newSelectedCount];
                            let newItem = {
                                prId: prId,
                                name: name,
                                price: price,
                                base64: base64,
                                selectedCounts: newSelectedCounts
                            }
                            newItems = [...oldItems, newItem];
                        }
                        oldCart[memberId] = newItems;
                        return oldCart;

                    }else{ //해당 멤버의 cart 아님
                        return thisMember;
                    }
                });

                if(!thisMemberCartExists){ //멤버 cart 없음
                    console.log("멤버 cart 없음")
                    newState = createCart();
                }

            }else{ //모든 멤버의 장바구니가 없음
                console.log("모든 멤버의 장바구니가 없음")
                newState = createCart();
            }
            state = newState;
            return state;

        case "DELETE_ITEM": //장바구니에서 삭제
            let deleteAfterMemberItems = action.payload.deleteAfterMemberItems;
            let memberId02 = action.payload.memberId;

            if(0 < state.length){
                let newState = state.map(element => {
                    if(Object.hasOwn(element, memberId02)){ //해당 멤버의 cart
                        element[memberId02] = deleteAfterMemberItems;
                        return element;
                    }else{ //해당 멤버의 cart 아님
                        return element;
                    }
                });
                state = [...newState];
            }
            return state;
        
        case "MODIFY_ITEM_BY_ADMIN": //관리자가 상품 수정시 회원들의 장바구니의 상품 수정
            if(0 < state.length){
                let prId = action.payload.prId;
                let name = action.payload.name;
                let price = action.payload.price;
                let base64 = action.payload.base64;

                let newState = state.map(element => {
                    let arr = Object.values(element);
                    let thisMemberItems = arr[0];

                    let newThisMemberItems = thisMemberItems.map((thisItem) => {
                        if(thisItem.prId === prId){ //수정되어야할 상품
                            thisItem.name = name;
                            thisItem.price = price;
                            thisItem.base64 = base64;
    
                            let oldSelectedCounts = thisItem.selectedCounts;
                            let newSelectedCounts = oldSelectedCounts.map((thisCount) => {
                                thisCount.totalPrice = thisCount.count * price;
                                return thisCount;
                            });
                            thisItem.selectedCounts = newSelectedCounts;
                            return thisItem;
                        }else{
                            return thisItem;
                        }
                    });
                    let keys = Object.keys(element);
                    let key = keys[0];
                    element = {
                       [key]: newThisMemberItems
                    }
                    return element;
                });
                state = [...newState];
            }
            return state;
        
        case "DELETE_ITEM_BY_ADMIN": //관리자가 상품 삭제시 회원들의 장바구니의 상품 삭제
            if(0 < state.length){
                let prId = action.payload.prId;

                let newState = state.map(element => {
                    let arr = Object.values(element);
                    let thisMemberItems = arr[0];

                    let newThisMemberItems = thisMemberItems.map((thisItem) => {
                            if(thisItem.prId === prId){ //삭제되어야할 상품
                                return null;
                            }else{
                                return thisItem;
                            }
                        }).filter((thisItem) => thisItem !== null);

                    let keys = Object.keys(element);
                    let key = keys[0];
                    element = {
                        [key]: newThisMemberItems
                    }
                    return element;
                });
                state = [...newState];
            }
            return state;

        case "RESET":
            return [];

        default :
            return state;
    }
}

export default cartReducer;