import { React, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useSelector, shallowEqual } from 'react-redux';
import ProductItem from './product_item';

const ProductList = ({server}) => {

    const navigate = useNavigate();
    const user = useSelector(store => store.userReducer, shallowEqual);
    const [products, setProducts] = useState([]);

    const onClickCreate = () => {
        navigate('/product/create');
    }

    useEffect(() => {
        getProducts();
    }, []);

    const getProducts = () => {
        server.products(user.token)
        .then(response => {
            console.log("response: " + response);
            setProducts(response);
            console.log("products: " + products);
        })
    }

    if(user.memberRole === 'ADMIN'){
        if(products.length === 0){
            return (
                <div className='store'>
                    <div>
                        <button type="button" onClick={onClickCreate}>상품추가</button>
                    </div>
                    <div>등록된 상품이 없습니다.</div>
                </div>
            );
        }else{
            return (
                <div className='store'>
                    <div>
                        <button type="button" onClick={onClickCreate}>상품추가</button>
                    </div>
                    <div>
                        <ul>
                            {products.map(product => (
                            <ProductItem name={product.name} price={product.price} imgLocation={product.imgLocation}/>
                            ))}
                        </ul>
                    </div>
                </div>
            );
        }
        
    }else{
        return (
            <div className='store'>
                
            </div>
        );
    }
};

export default ProductList;