import React from 'react';

const CartItem = ({cart}) => {
    
    const count = cart.count;
    const item = cart.item;
    const totalPrice = cart.totalPrice;

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
                    <p>가격 : {inputPriceFormat(totalPrice)} 원</p>
                </div>
            </div>
        </div>
    );
};

export default CartItem;