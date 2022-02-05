package com.movie.Gemflix.service;

import com.movie.Gemflix.common.ApiResponseMessage;
import com.movie.Gemflix.common.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Service
public class CommonService {

    public ApiResponseMessage checkError(BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            FieldError fieldError = (FieldError) bindingResult.getAllErrors().get(0);
            String field = fieldError.getField();
            String message = fieldError.getDefaultMessage();

            switch (field){
                case "id":
                    return new ApiResponseMessage(HttpStatus.BAD_REQUEST.value(), message, ErrorType.INVALID_MEMBER_ID);
                case "password":
                    return new ApiResponseMessage(HttpStatus.BAD_REQUEST.value(), message, ErrorType.INVALID_MEMBER_PASSWORD);
                case "phone":
                    return new ApiResponseMessage(HttpStatus.BAD_REQUEST.value(), message, ErrorType.INVALID_MEMBER_PHONE);
                case "email":
                    return new ApiResponseMessage(HttpStatus.BAD_REQUEST.value(), message, ErrorType.INVALID_MEMBER_EMAIL);
                default:
                    return new ApiResponseMessage(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), ErrorType.ETC_FAIL);
            }
        }else{
            return null;
        }
    }

}
