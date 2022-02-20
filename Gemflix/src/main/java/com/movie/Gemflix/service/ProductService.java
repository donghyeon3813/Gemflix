package com.movie.Gemflix.service;

import com.movie.Gemflix.common.ApiResponseMessage;
import com.movie.Gemflix.common.Constant;
import com.movie.Gemflix.common.ErrorType;
import com.movie.Gemflix.dto.product.ProductDto;
import com.movie.Gemflix.entity.Product;
import com.movie.Gemflix.entity.QProduct;
import com.movie.Gemflix.repository.ProductRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final CommonService commonService;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final JPAQueryFactory queryFactory;

    private QProduct qProduct = QProduct.product;

    @PersistenceContext
    private EntityManager entityManager;


    @Transactional
    public ApiResponseMessage createProduct(ProductDto productDTO) throws Exception{

        //파일 확장자 검사
        MultipartFile file = productDTO.getFile();
        if(!commonService.checkFile(file, Constant.FileExtension.JPG_AND_PNG)){
            return new ApiResponseMessage(HttpStatus.BAD_REQUEST.value(), ErrorType.INVALID_EXTENSION);
        }
        //파일 업로드
        String imgLocation = commonService.uploadFile(file, Constant.FilePath.PATH_STORE + productDTO.getMemberId() + "/");
        if(imgLocation == null){
            return new ApiResponseMessage(HttpStatus.BAD_REQUEST.value(), ErrorType.STORE_FAILED_TO_UPLOAD_FILE);
        }
        //상품 등록
        String status = productDTO.getStatus();
        switch (status){
            case "Y":
                productDTO.setStatus("1");
                break;
            case "N":
                productDTO.setStatus("0");
                break;
        }

        productDTO.setImgLocation(imgLocation);
        Product product = modelMapper.map(productDTO, Product.class);
        log.info("product: {}", product);
        productRepository.save(product);
        return new ApiResponseMessage(HttpStatus.OK.value(), "Product Register Success");
    }

    public List<ProductDto> getProducts() throws Exception{
        List<Product> products = productRepository.findAll();
        if(products.size() == 0) return null;

        List<ProductDto> productDtos = products.stream()
                .map(product -> {
                    ProductDto productDTO = modelMapper.map(product, ProductDto.class);
                    InputStream imageStream = null;
                    try {
                        /*String oldLocation = productDTO.getImgLocation();
                        System.out.println("oldLocation: " + oldLocation);
                        byte[] binary = getFileBinary(oldLocation);
                        System.out.println("binary: " + binary);
                        String base64data = Base64.getEncoder().encodeToString(binary);
                        System.out.println("base64data: " + base64data);

                        //imageUrl

                        imageStream.close();*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return productDTO;
                }).collect(Collectors.toList());
        return productDtos;
    }

    // 파일 읽어드리는 함수
    private static byte[] getFileBinary(String filepath) {
        File file = new File(filepath);
        byte[] data = new byte[(int) file.length()];
        try (FileInputStream stream = new FileInputStream(file)) {
            stream.read(data, 0, data.length);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return data;
    }


}