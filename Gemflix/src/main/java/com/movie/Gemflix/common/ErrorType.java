package com.movie.Gemflix.common;

public enum ErrorType {

    DUPLICATED_MEMBER_ID(1001, "duplicated id"), //memberId 중복
    INVALID_MEMBER_ID(1002, "invalid member id"), //memberId 유효성검사 실패
    INVALID_MEMBER_PASSWORD(1003, "invalid password"), //password 유효성검사 실패
    INVALID_MEMBER_PHONE(1004, "invalid phone"), //phone 유효성검사 실패
    INVALID_MEMBER_EMAIL(1005, "invalid email"), //email 유효성검사 실패
    INVALID_MEMBER(1006, "invalid member"), //유효하지않은 member
    ACCESS_TOKEN_EXPIRED(1007, "access token expired"), //access token 유효시간 만료
    REFRESH_TOKEN_EXPIRED(1008, "refresh token expired"), //refresh token 유효시간 만료
    REFRESH_IS_NULL(1009, "refresh token is null"), //refresh token is null
    DUPLICATED_MEMBER_EMAIL(1010, "duplicated email"), //email 중복
    INVALID_EXTENSION(1011, "invalid extension"), //file 확장자 검사 실패

    STORE_NAME_IS_TOO_LONG(1012, "name is too long"), //제목 유효성 검사 실패
    STORE_CONTENT_IS_TOO_LONG(1013, "content is too long"), //상세설명 유효성 검사 실패
    STORE_INVALID_PRICE(1014, "invalid price"), //가격 유효성 검사 실패
    STORE_FAILED_TO_UPLOAD_FILE(1015, "failed to upload file"), //상품 파일 업로드 실패

    MOVIE_DETAIL_NOT_FOUND(1100,"영화 상세정보를 찾을 수 없습니다."),

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