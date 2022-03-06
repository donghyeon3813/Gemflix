package com.movie.Gemflix.service;

import com.movie.Gemflix.common.CommonResponse;
import com.movie.Gemflix.common.Constant;
import com.movie.Gemflix.common.ErrorType;
import com.movie.Gemflix.controller.ProductController;
import com.movie.Gemflix.dto.product.CategoryDto;
import com.movie.Gemflix.dto.product.ProductDto;
import com.movie.Gemflix.entity.Category;
import com.movie.Gemflix.entity.Product;
import com.movie.Gemflix.entity.QProduct;
import com.movie.Gemflix.repository.ProductRepository;
import com.movie.Gemflix.repository.ProductRepositorySupport;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final CommonService commonService;
    private final ProductRepository productRepository;
    private final ProductRepositorySupport productRepositorySupport;
    private final ModelMapper modelMapper;

    @Transactional
    public CommonResponse createProduct(ProductDto productDto) throws Exception{

        //파일 확장자 검사
        MultipartFile file = productDto.getMultiPartFile();
        if(!commonService.checkFile(file, Constant.FileExtension.JPG_AND_PNG)){
            return new CommonResponse(ErrorType.INVALID_EXTENSION.getErrorCode(),
                    ErrorType.INVALID_EXTENSION.getErrorMessage());
        }
        //파일 업로드
        String imgLocation = commonService.uploadFile(file, Constant.FilePath.PATH_STORE + productDto.getMemberId() + "/");
        if(imgLocation == null){
            return new CommonResponse(ErrorType.STORE_FAILED_TO_UPLOAD_FILE.getErrorCode(),
                    ErrorType.STORE_FAILED_TO_UPLOAD_FILE.getErrorMessage());
        }
        //상품 등록
        String status = productDto.getStatus();
        switch (status){
            case "Y":
                productDto.setStatus("1");
                break;
            case "N":
                productDto.setStatus("0");
                break;
        }
        productDto.setImgLocation(imgLocation);

        //setting category
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setCgId(productDto.getCgId());
        categoryDto.setCgName(productDto.getCgName());
        productDto.setCategory(categoryDto);

        //setting product
        Product product = modelMapper.map(productDto, Product.class);
        log.info("product: {}", product);
        productRepository.save(product);
        return null;
    }

    public List<ProductDto> getProducts() throws Exception{

        List<Product> products = productRepositorySupport.findByStatusProductAndCategory("1");
        if(products.size() == 0) return null;

        List<ProductDto> productDtos = products.stream()
                .map(product -> {
                    ProductDto productDTO = modelMapper.map(product, ProductDto.class);
                    String location = productDTO.getImgLocation();

                    try {
                        Resource resource = new FileSystemResource(location);
                        File file = resource.getFile();
                        String base64 = fileToString(file);
                        productDTO.setBase64(base64);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return productDTO;
                }).collect(Collectors.toList());
        return productDtos;
    }

    public String fileToString(File file) throws IOException {

        String fileName = file.getName();
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);

        String fileString = "";
        FileInputStream inputStream =  null;
        ByteArrayOutputStream byteOutStream = null;

        try {
            inputStream = new FileInputStream(file);
            byteOutStream = new ByteArrayOutputStream();

            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = inputStream.read(buf)) != -1) {
                byteOutStream.write(buf, 0, len);
            }
            byte[] fileArray = byteOutStream.toByteArray();
            fileString = new String(Base64.encodeBase64(fileArray));

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            inputStream.close();
            byteOutStream.close();
        }
        return "data:image/" + extension + ";base64," + fileString;
    }

}