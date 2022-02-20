import React from 'react';

const ProductItem = (props) => {
    return (
        <div>
            <img src={props.imgLocation} alt="product thumbnail"/>
            <div>
                <p>{props.name}</p>
                <p>{props.price}원</p>
            </div>
        </div>
    );           
};

export default ProductItem;