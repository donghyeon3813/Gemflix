import { React, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useSelector, shallowEqual } from 'react-redux';
import ProductItem from './product_item';

const ProductList = ({server}) => {

    const navigate = useNavigate();
    const user = useSelector(store => store.userReducer, shallowEqual);
    const [snacks, setSnacks] = useState([]);
    const [tickets, setTickets] = useState([]);
    const [photoTickets, setPhotoTickets] = useState([]);

    const onClickCreate = () => {
        navigate('/product/create');
    }

    useEffect(() => {
        getProducts();
    }, []);

    const getProducts = () => {
        server.products(user.token)
        .then(response => {
            const products = response.data;
            setSnacks(products.filter(product => product.category === "0"));
            setTickets(products.filter(product => product.category === "1"));
            setPhotoTickets(products.filter(product => product.category === "2"));
        })
    }


    if(user.memberRole === 'ADMIN'){
        if(snacks == null && tickets == null && photoTickets == null){
            return (
                <div className='product'>
                    <div className='product_buttons'>
                        <button type="button" onClick={onClickCreate}>상품추가</button>
                    </div>
                    <div>등록된 상품이 없습니다.</div>
                </div>
            );
        }else{
            return (
                <div className='product'>
                    <div className='product_buttons'>
                        <button type="button" onClick={onClickCreate}>상품추가</button>
                    </div>
                    <div>
                        {snacks.length === 0 ? <></> : 
                            <>
                            <div className='product_category'>스낵바</div>
                            <ul className='product_list'>
                                {snacks.map((product) => (
                                    <ProductItem key={product.prId} name={product.name} price={product.price} base64={product.base64}/>   
                                ))}
                            </ul>
                            </>
                        }
                        {tickets.length === 0 ? <></> : 
                            <>
                            <div className='product_category'>관람권</div>
                            <ul className='product_list'>
                                {tickets.map(product => (
                                <ProductItem key={product.prId} name={product.name} price={product.price} base64={product.base64}/>
                                ))}
                            </ul>
                            </>
                        }
                        {photoTickets.length === 0 ? <></> : 
                            <>
                            <div className='product_category'>포토티켓</div>
                            <ul className='product_list'>
                                {photoTickets.map(product => (
                                <ProductItem key={product.prId} name={product.name} price={product.price} base64={product.base64}/>
                                ))}
                            </ul>
                            </>
                        }
                    </div>
                </div>
            );
        }
        
    }else{
        return (
            <div className='product'>
                <div>
                    <ul className='product_list'>
                        {snacks.map(product => (
                        <ProductItem key={product.prId} name={product.name} price={product.price} base64={product.base64}/>
                        ))}
                    </ul>
                </div>
            </div>
        );
    }
};

export default ProductList;