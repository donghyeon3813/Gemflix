package com.movie.Gemflix.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.LinkedHashMap;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseMessage {

    // HttpStatus
    private int status;

    // Http Default Message
    private Object message;

    public ApiResponseMessage(int status, ErrorType error) {
        LinkedHashMap<String, Object> errorMap = new LinkedHashMap<>();
        errorMap.put("errorCode", error.getErrorCode());
        errorMap.put("errorMessage", error.getErrorMessage());
        this.status = status;
        this.message = errorMap;
    }

    public ApiResponseMessage(int status, String errorMessage, ErrorType error) {
        LinkedHashMap<String, Object> errorMap = new LinkedHashMap<>();
        errorMap.put("errorCode", error.getErrorCode());
        errorMap.put("errorMessage", errorMessage);
        this.status = status;
        this.message = errorMap;
    }

    public boolean hasError(){
        String str = this.message.toString();
        return str.contains("errorCode");
    }

}
