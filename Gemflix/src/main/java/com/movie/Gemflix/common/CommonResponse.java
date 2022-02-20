package com.movie.Gemflix.common;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
@Builder
public class CommonResponse<T> {
    private String code;
    private String message;
    private T data;

    public CommonResponse(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    public CommonResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ResponseEntity<?> createResponse (CommonResponse response, HttpStatus httpStatus){
        return new ResponseEntity<>(response,httpStatus);
    }

}
