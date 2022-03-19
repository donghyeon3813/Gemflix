import React, { useEffect, useState } from 'react';
import { useSelector, shallowEqual, useDispatch } from 'react-redux';
import CartItem from './cart_item';
import { deleteCart } from '../../store/actions';

const CartList = (props) => {

    const dispatch = useDispatch();
    const user = useSelector(store => store.userReducer, shallowEqual);
    const carts = useSelector(store => store.cartReducer, shallowEqual);
    const [memberCarts, setMemberCarts] = useState([]);
    const [allPrice, setAllPrice] = useState(0);
    const [selectedPrice, setSelectedPrice] = useState(0);

    const [checkList, setCheckList] = useState([])
    const [idList, setIdList] = useState([])

    useEffect(() => {
        setAllPrice(0);
        setSelectedPrice(0);
        setCheckList([]);
        setIdList([]);

        if(0 < carts.length){
            const tempCarts = carts.filter((cart) => cart.memberId === user.memberId);
            setMemberCarts(tempCarts);
            
            let tempAllPrice = 0;
            let ids = [];
            tempCarts.map((cart, index) => {
                tempAllPrice = tempAllPrice + cart.totalPrice;
                ids[index] = cart.id;
            });
            setAllPrice(tempAllPrice);
            setIdList(ids);
        }
    }, [carts]);

    const onChangeAll = (e) => {
        setCheckList(e.target.checked ? idList : []);
        setSelectedPrice(e.target.checked ? allPrice : 0);
    }

    const onChangeEach = (e, id, price) => {
        if(e.target.checked){
            setCheckList([...checkList, id]);
            setSelectedPrice(selectedPrice + price);
        }else{
            setCheckList(checkList.filter((checkedId) => checkedId !== id));
            setSelectedPrice(selectedPrice - price);
        }
    }

    const onClickDeleteCart = () => {
        const deleteAfterCarts = carts.filter((item) => {
            if(!checkList.includes(item.id)){
                return item;
            }
        });
        console.log(deleteAfterCarts);
        dispatch(deleteCart(deleteAfterCarts));
    }

    const onClickOrderCart = () => {
        //TODO
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
                <button type='button' onClick={() => onClickOrderCart()}>선택상품 주문</button>
                <button type='button' onClick={() => onClickDeleteCart()}>선택상품 삭제</button>
                <div className='cart_list'>
                    <input type="checkbox" onChange={onChangeAll} checked={checkList.length === idList.length}/>
                    {
                        memberCarts.map((cart, index) => (
                            <div key={index}>
                            <div className='cart_box'>
                                <input type="checkbox" onChange={(e) => onChangeEach(e, cart.id, cart.totalPrice)} checked={checkList.includes(cart.id)}/>
                                <CartItem key={cart.id} cart={cart}/>
                            </div>
                        </div>
                    ))
                    }
                    <h2>총 {inputPriceFormat(selectedPrice)}원</h2>
                </div>
            </>
        );
    }
};

export default CartList;