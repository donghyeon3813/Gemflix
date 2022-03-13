import { React, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useSelector, shallowEqual } from 'react-redux';
import ProductItem from './product_item';

const ProductList = ({server}) => {

    const navigate = useNavigate();
    const user = useSelector(store => store.userReducer, shallowEqual);
    const [products, setProducts] = useState([]);
    const [textCategories, setTextCategories] = useState([]);
    const [categories, setCategories] = useState(new Map());

    const onClickCreate = () => {
        navigate('/product/create', { state: { categories: categories } });
    }

    useEffect(() => {
        //get category
        server.category()
        .then(response => {
            const data = response.data;
            data.map(category => {
                setCategories(categories.set(category.cgId, category.cgName));
            });
        })
        .catch(ex => {
            console.log("category requset fail : " + ex);
        })
        .finally(() => {
            console.log("category request end");
        });

        //get product
        getProducts();
    }, []);

    const getProducts = () => {
        server.products()
        .then(response => {
            const products = response.data;
            setProducts(products);
            
            const tempCt = [...categories.values()];
            setTextCategories(tempCt);
        })
        .catch(ex => {
            console.log("products requset fail : " + ex);
        })
        .finally(() => {
            console.log("products request end");
        });
    }


    if(user.memberRole === 'ADMIN'){
        if(products == null){
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
                        {
                        textCategories.map((category, index) => (
                            <div key={index}>
                                <div className='product_category'>{category}</div>
                                <div className='product_list'>
                                    {
                                    products
                                    .filter((product) => (product.category.cgName === category))
                                    .map((product) => (
                                        <ProductItem key={product.prId} name={product.name} price={product.price} base64={product.base64}/>   
                                    ))
                                    }
                                </div>
                            </div>
                        ))
                        }
                    </div>
                </div>
            );
        }
        
    }else{
        return (
            <div className='product'>
                <div>
                    {
                    textCategories.map((category, index) => (
                        <div key={index}>
                            <div className='product_category'>{category}</div>
                            <div className='product_list'>
                                {
                                products
                                .filter((product) => (product.category.cgName === category))
                                .map((product) => (
                                    <ProductItem key={product.prId} name={product.name} price={product.price} base64={product.base64}/>   
                                ))
                                }
                            </div>
                        </div>
                    ))
                    }
                </div>
            </div>
        );
    }
};

export default ProductList;