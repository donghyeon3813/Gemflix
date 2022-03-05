package com.movie.Gemflix.controller;

import com.movie.Gemflix.common.CommonResponse;
import com.movie.Gemflix.common.Constant;
import com.movie.Gemflix.common.ErrorType;
import com.movie.Gemflix.dto.product.CategoryDto;
import com.movie.Gemflix.dto.product.ProductDto;
import com.movie.Gemflix.entity.Category;
import com.movie.Gemflix.entity.Product;
import com.movie.Gemflix.repository.CategoryRepository;
import com.movie.Gemflix.service.CommonService;
import com.movie.Gemflix.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ModelMapper modelMapper;
    private final CommonService commonService;
    private final ProductService productService;
    private final CategoryRepository categoryRepository;

    @GetMapping("none/products")
    public ResponseEntity<?> getProducts(){

        try{
            List<ProductDto> productDtos = productService.getProducts();
            if(productDtos == null){
                return CommonResponse.createResponse(CommonResponse.builder()
                        .code(ErrorType.STORE_NONE_PRODUCT.getErrorCode())
                        .message(ErrorType.STORE_NONE_PRODUCT.getErrorMessage())
                        .build(), HttpStatus.NO_CONTENT);
            }else{
                return CommonResponse.createResponse(CommonResponse.builder()
                        .code(Constant.Success.SUCCESS_CODE)
                        .message("Success Get Products")
                        .data(productDtos)
                        .build(), HttpStatus.OK);
            }
        }catch (Exception e){
            log.error("getProducts Exception!!");
            e.printStackTrace();
            return CommonResponse.createResponse(CommonResponse.builder()
                    .code(ErrorType.ETC_FAIL.getErrorCode())
                    .message(ErrorType.ETC_FAIL.getErrorMessage())
                    .build(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("none/category")
    public ResponseEntity<?> getCategory(){
        try{
            List<Category> category = categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "cgId"));
            return CommonResponse.createResponse(CommonResponse.builder()
                    .code(Constant.Success.SUCCESS_CODE)
                    .message("Success Get Category")
                    .data(category)
                    .build(), HttpStatus.OK);
        }catch (Exception e){
            log.error("getCategory Exception!!");
            e.printStackTrace();
            return CommonResponse.createResponse(CommonResponse.builder()
                    .code(ErrorType.ETC_FAIL.getErrorCode())
                    .message(ErrorType.ETC_FAIL.getErrorMessage())
                    .build(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("product")
    public ResponseEntity<?> createProduct(@ModelAttribute @Valid ProductDto productDTO,
                                           BindingResult bindingResult){

        try{
            log.info("[createProduct] productDTO: {}", productDTO);
            CommonResponse response = commonService.checkError(bindingResult);
            log.info("response: {}", response);
            if(response != null){
                return CommonResponse.createResponse(response, HttpStatus.BAD_REQUEST);
            }

            response = productService.createProduct(productDTO);
            log.info("response: {}", response);
            if(response != null){
                return CommonResponse.createResponse(response, HttpStatus.BAD_REQUEST);
            }
            return CommonResponse.createResponse(CommonResponse.builder()
                    .code(Constant.Success.SUCCESS_CODE)
                    .message("Product Register Success")
                    .build(), HttpStatus.OK);

        }catch (Exception e){
            log.error("createStore Exception!!");
            e.printStackTrace();
            return CommonResponse.createResponse(CommonResponse.builder()
                    .code(ErrorType.ETC_FAIL.getErrorCode())
                    .message(ErrorType.ETC_FAIL.getErrorMessage())
                    .build(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/showImage")
    public ResponseEntity<?> showImage(@RequestParam("imgLocation") String imgLocation) {

        // Resorce를 사용해서 로컬 서버에 저장된 이미지 경로 및 파일 명을 지정
        Resource resource = new FileSystemResource(imgLocation);

        // 로컬 서버에 저장된 이미지 파일이 없을 경우
        if(!resource.exists()){
            return CommonResponse.createResponse(CommonResponse.builder()
                    .code(ErrorType.FILE_DELETED.getErrorCode())
                    .message(ErrorType.FILE_DELETED.getErrorMessage())
                    .build(), HttpStatus.NOT_FOUND);
        }

        // 로컬 서버에 저장된 이미지가 있는 경우 로직 처리
        HttpHeaders header = new HttpHeaders();
        Path filePath = null;
        try {
            filePath = Paths.get(imgLocation);
            // 인풋으로 들어온 파일명 .png / .jpg 에 맞게 헤더 타입 설정
            header.add("Content-Type", Files.probeContentType(filePath));
        }
        catch (Exception e){
            e.printStackTrace();
        }

        // 이미지 리턴 실시 [브라우저에서 get 주소 확인 가능]
        return new ResponseEntity<>(resource, header, HttpStatus.OK);
    }

}