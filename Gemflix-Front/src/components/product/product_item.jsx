import { memo, React, useRef, useState, useEffect } from 'react';
import { useSelector, shallowEqual, useDispatch } from 'react-redux';
import Modal from 'react-modal';
import { addCart } from '../../store/actions';
import { useNavigate } from 'react-router';

const ProductItem = memo((props) => {

    const dispatch = useDispatch();
    const navigate = useNavigate();
    const user = useSelector(store => store.userReducer, shallowEqual);
    const [modalIsOpen, setModalIsOpen] = useState(false);
    const [count , setCount] = useState(1);
    const [totalPrice , setTotalPrice] = useState(props.price);
    

    const onClickUpdate = () => {
        navigate('/product/modify', { 
            state: { 
                product: props, 
                categories: props.categories
            } 
        });
    }

    const onClickDelete = (prId) => {
        console.log(prId);
        //TODO: DELETE
    }

    const changeCount = (event) => {
        const cnt = event.target.value;
        const price = props.price;
        if(cnt < 1 || 10 < cnt){
            alert("수량은 1~10개만 가능합니다.");
            setCount(1);
            setTotalPrice(props.price);
        }else{
            setCount(cnt);
            setTotalPrice(price*cnt);
        }
    }

    const onClickAddCart = (props) => {
        dispatch(addCart(props, count, totalPrice, user.memberId));
        if(window.confirm("해당 상품을 장바구니에 담았습니다.\n장바구니로 이동할까요?")){
            navigate('/cartList');
        }else{
            navigate('/products');
        }
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

    if(user.memberRole === 'ADMIN'){
        return (
            <>
                <div className='product_box' onClick={()=> setModalIsOpen(true)}>
                    <div className='product_img_box'>
                        <img className='product_img' src={props.base64} alt="product thumbnail"/>
                    </div>
                    <div className='product_text_box'>
                        <p className='product_name'>{props.name}</p>
                        <p className='product_price'>{inputPriceFormat(props.price)}원</p>
                    </div>
                </div>
                <Modal isOpen={modalIsOpen} onRequestClose={() => setModalIsOpen(false)} ariaHideApp={false}>
                    <div className='product_modal'>
                        <div className='product_img_box'>
                            <img className='product_img' src={props.base64} alt="product thumbnail"/>
                        </div>
                        <div className='product_text_box'>
                            <p className='product_name'>{props.name}</p>
                            <p className='product_name'>{props.content}</p>
                            <p className='product_price'>{inputPriceFormat(props.price)}원</p>
                        </div>
                        <button type='button' onClick={onClickUpdate}>수정</button>
                        <button type='button' onClick={() => onClickDelete(props.prId)}>삭제</button>
                        <button type='button' onClick={()=> setModalIsOpen(false)}>취소</button>
                    </div>
                </Modal>
            </>
        );
    }else if(user.memberRole === 'MEMBER' || user.memberRole === 'NO_PERMISSION'){
        return (
            <>
                <div className='product_box' onClick={()=> setModalIsOpen(true)}>
                    <div className='product_img_box'>
                        <img className='product_img' src={props.base64} alt="product thumbnail"/>
                    </div>
                    <div className='product_text_box'>
                        <p className='product_name'>{props.name}</p>
                        <p className='product_price'>{inputPriceFormat(props.price)}원</p>
                    </div>
                </div>
                <Modal isOpen={modalIsOpen} onRequestClose={() => setModalIsOpen(false)} ariaHideApp={false}>
                    <div className='product_modal'>
                        <div className='product_img_box'>
                            <img className='product_img' src={props.base64} alt="product thumbnail"/>
                        </div>
                        <div className='product_text_box'>
                            <p className='product_name'>{props.name}</p>
                            <p className='product_name'>{props.content}</p>
                            <p className='product_price'>{inputPriceFormat(props.price)}원</p>
                        </div>
                        <form className='cart_form'>
                            <label>수량 : </label>
                            <input className='product_count' value={count} type='number' min="1" max="10" onChange={changeCount}/><br/>
                            <label>총 금액 : </label>
                            <p className='product_total_price'>{inputPriceFormat(totalPrice)}원</p>
                        </form>
                        <button type='button' onClick={() => onClickAddCart(props)}>장바구니 담기</button>
                        <button type='button' onClick={()=> setModalIsOpen(false)}>취소</button>
                    </div>
                </Modal>
            </>
        );
    }else{
        return (
            <>
                <div className='product_box' onClick={()=> setModalIsOpen(true)}>
                    <div className='product_img_box'>
                        <img className='product_img' src={props.base64} alt="product thumbnail"/>
                    </div>
                    <div className='product_text_box'>
                        <p className='product_name'>{props.name}</p>
                        <p className='product_price'>{inputPriceFormat(props.price)}원</p>
                    </div>
                </div>
                <Modal isOpen={modalIsOpen} onRequestClose={() => setModalIsOpen(false)} ariaHideApp={false}>
                    <div className='product_modal'>
                        <div className='product_img_box'>
                            <img className='product_img' src={props.base64} alt="product thumbnail"/>
                        </div>
                        <div className='product_text_box'>
                            <p className='product_name'>{props.name}</p>
                            <p className='product_name'>{props.content}</p>
                            <p className='product_price'>{inputPriceFormat(props.price)}원</p>
                        </div>
                        <button type='button' onClick={()=> setModalIsOpen(false)}>취소</button>
                    </div>
                </Modal>
            </>
        );
    }
});

export default ProductItem;