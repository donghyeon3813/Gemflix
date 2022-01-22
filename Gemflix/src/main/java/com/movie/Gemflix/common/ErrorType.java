package com.movie.Gemflix.common;

public enum ErrorType {

    DUPLICATED_MEMBER_ID(1001, "duplicated id"), //memberId 중복
    INVALID_MEMBER_ID(1002, "invalid id"), //memberId 유효성검사 실패
    INVALID_MEMBER_PASSWORD(1003, "invalid password"), //password 유효성검사 실패
    INVALID_MEMBER_PHONE(1004, "invalid phone"), //phone 유효성검사 실패
    INVALID_MEMBER_EMAIL(1005, "invalid email"), //email 유효성검사 실패

    ETC_FAIL(1999, "fail...") //그 외 오류
    ;

    private int errorCode;
    private String errorMessage;

    ErrorType(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public Integer getErrorCode(){
        return this.errorCode;
    }

    public String getErrorMessage(){
        return this.errorMessage;
    }
}
