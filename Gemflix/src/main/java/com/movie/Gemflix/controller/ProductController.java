package com.movie.Gemflix.controller;

import com.movie.Gemflix.common.ApiResponseMessage;
import com.movie.Gemflix.dto.product.ProductDto;
import com.movie.Gemflix.service.CommonService;
import com.movie.Gemflix.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ProductController {

    private final CommonService commonService;
    private final ProductService productService;

    /*@GetMapping("/imageUrl")
    public ResponseEntity<byte[]> getImageAsResponseEntity(@RequestParam String filePath) {
        HttpHeaders headers = new HttpHeaders();
        byte[] media = null;
        try{
            InputStream in = getClass().getResourceAsStream(filePath);
            media = IOUtils.toByteArray(in);
        }catch (Exception e){
            e.printStackTrace();
        }
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());

        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(media, headers, HttpStatus.OK);
        return responseEntity;
    }*/

    @Secured("ROLE_ADMIN")
    @GetMapping("products")
    public ResponseEntity<?> getProducts(){

        try{
            List<ProductDto> productDtos = productService.getProducts();
            if(productDtos == null){
                ApiResponseMessage apiRm =
                        new ApiResponseMessage(HttpStatus.NO_CONTENT.value(), HttpStatus.NO_CONTENT.getReasonPhrase());
                return new ResponseEntity<>(apiRm, HttpStatus.valueOf(apiRm.getStatus()));
            }else{
                return ResponseEntity.ok(productDtos);
            }
        }catch (Exception e){
            log.error("getProducts Exception!!");
            e.printStackTrace();
        }
        return null;
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("product")
    public ResponseEntity<ApiResponseMessage> createProduct(@ModelAttribute @Valid ProductDto productDTO,
                                                            BindingResult bindingResult){

        try{
            log.info("[createProduct] productDTO: {}", productDTO);
            ApiResponseMessage apiRm = commonService.checkError(bindingResult);
            log.info("apiRm: {}", apiRm);
            if(apiRm != null) return new ResponseEntity<>(apiRm, HttpStatus.valueOf(apiRm.getStatus()));

            apiRm = productService.createProduct(productDTO);
            log.info("apiRm: {}", apiRm);
            return new ResponseEntity<>(apiRm, HttpStatus.valueOf(apiRm.getStatus()));

        }catch (Exception e){
            log.error("createStore Exception!!");
            e.printStackTrace();
        }
        return null;
    }

}