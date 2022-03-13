import React, { useEffect, useState } from 'react';
import { useSelector, shallowEqual } from 'react-redux';
import CartItem from './cart_item';

const CartList = (props) => {

    const carts = useSelector(store => store.cartReducer, shallowEqual);
    const [lastPrice, setLastPrice] = useState(0);

    useEffect(() => {
        console.log(carts);
        let tempLastPrice = 0;
        carts.map((cart => {
            tempLastPrice = tempLastPrice + cart.totalPrice;
        }));
        setLastPrice(tempLastPrice);
    });

    if(carts == null){
        return (
            <div className='cart_list'>
                <div>장바구니에 담은 상품이 없습니다.</div>
            </div>
        );
    }else{
        return (
            <div className='cart_list'>
                {
                carts.map((cart, index) => (
                    <div key={index}>
                        <div className='cart_box'>
                            <CartItem key={index} cart={cart}/>
                        </div>
                    </div>
                ))
                }
                {/* <h2>총 {lastPrice}원</h2> */}
            </div>
        );
    }
};

export default CartList;