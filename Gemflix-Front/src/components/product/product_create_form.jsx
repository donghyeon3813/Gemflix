import { React, useState, useRef, useEffect } from 'react';
import { useSelector, shallowEqual } from 'react-redux';
import { useNavigate, useLocation } from 'react-router-dom';

const ProductCreateForm = ({server, onClickLogout}) => {
    
    const navigate = useNavigate();
    const location = useLocation();
    const user = useSelector(store => store.userReducer, shallowEqual);
    const [imgBase64, setImgBase64] = useState([]); //파일 base64
    const [imgFile, setImgFile] = useState(null); //파일
    const [name, setName] = useState('');
    const [content, setContent] = useState('');
    const [status, setStatus] = useState(null);
    const [price, setPrice] = useState('');
    const [response, setReponse] = useState([]);
    const [requestCnt, setRequestCnt] = useState(0);
    const [loading , setLoading] = useState(false);

    const [categories, setCategories] = useState([]);
    const [category, setCategory] = useState('');
    const [categoryIdx, setCategoryIdx] = useState(0);

    const storeFormRef = useRef();

    useEffect(() => {
        const tempCategories = location.state.categories;
        setCategories(tempCategories);
        setCategory(tempCategories.get(0));
        setCategoryIdx(0);
    }, [])

    const handleChangeFile = (event) => {
        const files = event.target.files;
        const file = files[0];
        setImgFile(file);
        setImgBase64([]);

        if(file){
            let reader = new FileReader();
            reader.readAsDataURL(file); //파일을 읽어 버퍼에 저장
            reader.onloadend = () => { // 파일 상태 업데이트
            
                //읽기가 완료되면 아래코드가 실행
                const base64 = reader.result;
                if(base64){
                    let base64Sub = base64.toString()
                    setImgBase64(base64Sub);
                }
            }
        }
        
    }

    const onClickCreate = () => {
        setLoading(true);

        if(name && price && content && status && categories.has(categoryIdx)){
            setCategory(categories.get(categoryIdx));

            const formData = new FormData();
            formData.append("cgName", category);
            formData.append("cgId", categoryIdx);
            formData.append("multiPartFile", imgFile);
            formData.append("name", name);
            formData.append("price", parseInt(price.replace(/,/g,"")));
            formData.append("content", content);
            formData.append("status", status);
            formData.append("memberId", user.memberId);
            console.log("formData" + formData);
            
            server.createProduct(formData)
            .then(response => {
                setReponse(response);
                setRequestCnt(requestCnt + 1);
            })
            .catch(ex => {
                console.log("createProduct requset fail : " + ex);
            })
            .finally(() => {
                console.log("createProduct request end");
            });
        }else{
            alert("모든 정보를 입력해주세요.");
            setLoading(false);
        }
    }

    //response가 업데이트 될 때만 특정 함수를 실행
    const mounted = useRef(false);
    useEffect(() => {
        if(!mounted.current){
            mounted.current = true;
        } else { //success
            const code = response.code;
            switch(code){
                case 1007: //interceptor에서 accessToken 재발급
                    break;

                case 1000: //success
                    alert("상품 등록이 완료되었습니다.");
                    navigate('/products'); //목록페이지로 이동
                    break;

                case 1008: //refreshToken 만료 -> 로그아웃
                    onClickLogout(true);
                    break;

                default: //fail
                    alert(response.message);
                    break;
            }    
        }
        setLoading(false);
    }, [requestCnt]);

    const onClickReset = () => {
        if(window.confirm("작성중인 모든 내용을 지우겠습니까?")){
            storeFormRef.current.reset();
            setImgFile(null);
            setStatus(null);
            setImgBase64([]);
            setPrice('');
            setCategory(categories.get(0));
            setName('');
            setContent('');
        }
    }

    const onClickProductList = () => {
        if(window.confirm("작성을 취소하고 목록으로 돌아가겠습니까?")){
            navigate('/products');
        }
    }

    const handleKeyPress = (event) => {
        if (event.key === "Enter") {
            onClickCreate();
        }
    };

    const changeRadioYes = (event) => {
        setStatus(event.target.value);
    };

    const changeRadioNo = (event) => {
        setStatus(event.target.value);
    };

    const changePrice = (event) => {
        setPrice(inputPriceFormat(event.target.value));
    }

    const changeName = (event) => {
        setName(event.target.value);
    }

    const changeContent = (event) => {
        setContent(event.target.value);
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

    const handleSelect = (event) => {
        const value = Number(event.target.value);
        setCategoryIdx(value);
    };

    if(!loading){
        return (
            <div>
                <form ref={storeFormRef} className="store_form">
                    <div>
                        <select name="category" onChange={handleSelect} value={categoryIdx}>
                        {[...categories.keys()].map(key => (
                            <option value={key} key={key}>
                                {categories.get(key)}
                            </option>
                        ))}
                        </select>
    
                        <h4>상품명</h4>
                        <input value={name} type="text" placeholder="상품명(20자 이내)" onChange={changeName} onKeyPress={handleKeyPress}/><br/>
                        <h4>가격</h4>
                        <input value={price} type="text" placeholder="가격(100만원 이내)" onChange={changePrice}/>원<br/>
                        <h4>상세설명</h4>
                        <input value={content} type="text" placeholder="상세설명(500자 이내)" onChange={changeContent}/><br/>
                        <h4>판매상태</h4>
                        <label><input type="radio" value='Y' checked={status === "Y" ? true : false} onChange={changeRadioYes}/>판매중</label>
                        <label><input type="radio" value='N' checked={status === "N" ? true : false} onChange={changeRadioNo}/>판매 중단</label>
                        <br/>
    
                        <input type="file" id="file" multiple="multiple" onChange={handleChangeFile} />(100MB 이내)
                        <div style={{display:`${imgBase64}`?"block":"none"}}>
                            <h4>이미지 미리보기</h4>
                            <img
                            className="preview"
                            src={imgBase64}
                            alt="First slide"
                            style={{width:"100px",height:"100px"}}
                            />
                        </div>
                    </div>
                </form>
    
                <div>
                    <button onClick={onClickCreate}>작성완료</button>
                    <button onClick={onClickReset}>모두 지우기</button>
                    <button onClick={onClickProductList}>목록으로</button>
                </div>
            </div>
        );
    }else{
        return (
          <div>
            Loading ....
          </div>
        )
    }
};

export default ProductCreateForm;