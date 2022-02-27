import React from 'react';

const ProductItem = (props) => {
    return (
        <>
            <div className='product_box'>
                <div className='product_img_box'>
                    <img className='product_img' src={props.base64} alt="product thumbnail"/>
                </div>
                <div className='product_text_box'>
                    <p className='product_name'>{props.name}</p>
                    <p className='product_price'>{props.price}원</p>
                </div>
            </div>
        </>
    );           
};

export default ProductItem;