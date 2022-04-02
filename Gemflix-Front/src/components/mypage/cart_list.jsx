import React, { memo, useEffect, useState } from 'react';
import { useSelector, shallowEqual, useDispatch } from 'react-redux';
import CartItem from './cart_item';
import { deleteCart } from '../../store/actions';
import { useLocation, useNavigate } from 'react-router';

const CartList = memo((props) => {

    const location = useLocation();
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const user = useSelector(store => store.userReducer, shallowEqual);
    const carts = useSelector(store => store.cartReducer, shallowEqual);
    const [memberCarts, setMemberCarts] = useState([]);
    const [cartName, setCartName] = useState('');
    const [orderCartName, setOrderCartName] = useState('');

    const [allPrice, setAllPrice] = useState(0);
    const [selectedPrice, setSelectedPrice] = useState(0);

    const [checkList, setCheckList] = useState([]);
    const [idList, setIdList] = useState([]);
    const [refreshCnt, setRefreshCnt] = useState(0);

    useEffect(() => {
        setAllPrice(0);
        setSelectedPrice(0);
        setCheckList([]);
        setIdList([]);

        if(0 < carts.length){
            const memberId = user.memberId;
            let tempMemberCart = null;
            carts.forEach(thisMember => {
                if(Object.hasOwn(thisMember, memberId)){
                    tempMemberCart = thisMember[memberId];
                    setMemberCarts(tempMemberCart);
                    console.log(tempMemberCart);
                }
            });

            let tempAllPrice = 0;
            let ids = [];
            let i = 0;

            if(tempMemberCart.length != null){
                tempMemberCart.forEach((item) => {
                    item.selectedCounts.forEach((count) => {
                        tempAllPrice = tempAllPrice + count.totalPrice;
                        ids[i] = count.cId;
                        i = i + 1;
                    });
                });
                setAllPrice(tempAllPrice);
                setIdList(ids);
    
                let len = tempMemberCart.length;
                if(0 < len){
                    if(len === 1){
                        setCartName(tempMemberCart[0].name);
                    }else{
                        setCartName(tempMemberCart[0].name + " 외 " + (len-1) + "개의 상품...");
                    }
                    setOrderCartName(tempMemberCart[0].name);
                }
            }
        }
    }, [refreshCnt]);

    const onChangeAll = (e) => {
        setCheckList(e.target.checked ? idList : []);
        setSelectedPrice(e.target.checked ? allPrice : 0);
    }

    const onChangeEach = (e, cId, totalPrice) => {
        if(e.target.checked){
            setCheckList([...checkList, cId]);
            setSelectedPrice(selectedPrice + totalPrice);
        }else{
            setCheckList(checkList.filter((checkedId) => checkedId !== cId));
            setSelectedPrice(selectedPrice - totalPrice);
        }
    }

    const onClickDeleteCart = () => {
        const memberId = user.memberId;
        let deleteAfterMemberItems;

        carts.filter(thisMember => {
            if(Object.hasOwn(thisMember, memberId)){
                let oldItems = thisMember[memberId];

                deleteAfterMemberItems = oldItems.map(thisItem => {
                    let oldSelectedCounts = thisItem.selectedCounts;
                    let newSelectedCounts = oldSelectedCounts.map((thisCount) => {
                        if(!checkList.includes(thisCount.cId)){
                            return thisCount;
                        }
                    });
                    newSelectedCounts = newSelectedCounts.filter((element) => element !== undefined);
                    if(0 < newSelectedCounts.length){
                        thisItem.selectedCounts = newSelectedCounts;
                        return thisItem;
                    }
                });
            }
        });
        deleteAfterMemberItems = deleteAfterMemberItems.filter((element) => element !== undefined);
        console.log(deleteAfterMemberItems);
        dispatch(deleteCart(deleteAfterMemberItems, memberId));
        setMemberCarts([]);
        setCheckList([]);
        setRefreshCnt(refreshCnt + 1);
    }

    const onClickOrderCart = () => {
        const len = checkList.length;
        if(1 < len){
            setOrderCartName(orderCartName + " 외 " + (len-1) + "개의 상품...");
        }
        navigate('/payment', {
            state: {
                price: selectedPrice,
                cartName: orderCartName,
                carts: memberCarts
            }
        });
    }

    const inputPriceFormat = (str) => {
        const comma = (str) => {
          str = String(str);
          return str.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, "$1,");
        };
        const uncomma = (str) => {
          str = String(str);
          return str.replace(/[^\d]+/g, "");
        };
        return comma(uncomma(str));
    };


    if(memberCarts.length == 0){
        return (
            <div className='cart_list'>
                <div>장바구니에 담은 상품이 없습니다.</div>
            </div>
        );
    }else{
        return (
            <>
                <div className='cart_list'>
                    <input type="checkbox" onChange={onChangeAll} checked={checkList.length === idList.length}/>
                    <h3>{cartName}</h3>
                    {
                        memberCarts.map((cart) => {
                            return (
                                cart.selectedCounts.map((thisCount) => {
                                    return (
                                    <div className='cart_box' key={thisCount.cId}>
                                        <input type="checkbox" onChange={(e) => onChangeEach(e, thisCount.cId, thisCount.totalPrice)} 
                                            checked={checkList.includes(thisCount.cId)}/>
                                        <CartItem key={thisCount.cId} count={thisCount.count} 
                                            totalPrice={thisCount.totalPrice} name={cart.name} base64={cart.base64}/>
                                    </div>
                                    );
                                })

                            );
                        })
                    }
                    <h2>총 {inputPriceFormat(selectedPrice)}원</h2>
                    <button type='button' onClick={() => onClickOrderCart()}>선택상품 주문</button>
                    <button type='button' onClick={() => onClickDeleteCart()}>선택상품 삭제</button>
                </div>
            </>
        );
    }
});

export default CartList;