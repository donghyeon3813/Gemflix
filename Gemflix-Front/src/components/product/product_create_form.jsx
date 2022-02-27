import { React, useState, useRef, useEffect } from 'react';
import { useSelector, shallowEqual } from 'react-redux';
import { useNavigate } from 'react-router-dom';

const ProductCreateForm = ({server}) => {

    const navigate = useNavigate();
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

    const selectList = ["스낵바", "관람권", "포토티켓"];
    const [category, setCategory] = useState("스낵바");

    const storeFormRef = useRef();

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

        if(name && price && content && status && category){
            const formData = new FormData();
            formData.append("multiPartFile", imgFile);
            formData.append("name", name);
            formData.append("price", parseInt(price.replace(/,/g,"")));
            formData.append("content", content);
            formData.append("status", status);
            formData.append("memberId", user.memberId);
            
            switch(category){
                case '스낵바':
                    formData.append("category", "0");
                    break;
                case '관람권':
                    formData.append("category", "1");
                    break;
                case '포토티켓':
                    formData.append("category", "2");
                    break;
            }
            server.productCreate(user.token, formData)
            .then(response => {
                setReponse(response);
                setRequestCnt(requestCnt + 1);
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
            if(code === 1000){ //success
                alert("상품 등록이 완료되었습니다.");
                //목록페이지로 이동
                navigate('/products');

            }else{ //fail
                alert(response.message);
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
            setCategory('스낵바');
            setName('');
            setContent('');
        }
    }

    const onClickProductList = () => {
        if(window.confirm("작성을 취소하고 목록으로 돌아가겠습니까?")){
            navigate('/product');
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
        setCategory(event.target.value);
    };

    if(!loading){
        return (
            <div>
                <form ref={storeFormRef} className="store_form">
                    <div>
                        <select name="category" onChange={handleSelect} value={category}>
                            {selectList.map((item) => (
                                <option value={item} key={item}>
                                    {item}
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