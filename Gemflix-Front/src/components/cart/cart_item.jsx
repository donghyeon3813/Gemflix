import React from 'react';
import { deleteCart } from '../../store/actions';

const CartItem = (props) => {

    const item = props.cart.item;
    const totalPrice = props.cart.totalPrice;
    const count = props.cart.count;

    return (
        <div>
            
            <div className='cart_item'>
                <div className='cart_img'>
                    <img
                        className="preview"
                        src={item.base64}
                        style={{width:"100px",height:"100px"}}
                    />
                </div>
                <div className='cart_content'>
                    <h4>{item.name}</h4>
                    <p>수량 : {count} 개</p>
                    <p>가격 : {totalPrice} 원</p>
                </div>
            </div>
        </div>
    );
};

export default CartItem;