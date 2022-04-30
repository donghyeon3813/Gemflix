package com.movie.Gemflix.common;

public enum ErrorType {

    DUPLICATED_MEMBER_ID(1001, "이미 사용중인 아이디 입니다."), //memberId 중복
    INVALID_MEMBER_ID(1002, "아이디 형식에 맞게 입력해주세요."), //memberId 유효성검사 실패
    INVALID_MEMBER_PASSWORD(1003, "비밀번호 형식에 맞게 입력해주세요."), //password 유효성검사 실패
    INVALID_MEMBER_PHONE(1004, "핸드폰 형식에 맞춰서 입력해주세요."), //phone 유효성검사 실패
    INVALID_MEMBER_EMAIL(1005, "이메일 형식에 맞춰서 입력해주세요."), //email 유효성검사 실패
    INVALID_MEMBER(1006, "유효하지 않은 회원입니다."), //유효하지않은 member
    ACCESS_TOKEN_EXPIRED(1007, "AccessToken이 만료되었습니다."), //access token 유효시간 만료
    REFRESH_TOKEN_EXPIRED(1008, "RefreshToken이 만료되었습니다."), //refresh token 유효시간 만료
    REFRESH_IS_NULL(1009, "RefreshToken의 값은 null 일 수 없습니다."), //refresh token is null
    DUPLICATED_MEMBER_EMAIL(1010, "이미 사용중인 이메일 입니다."), //email 중복
    INVALID_EXTENSION(1011, "파일 확장자가 유효하지않습니다."), //file 확장자 검사 실패

    STORE_NAME_IS_TOO_LONG(1012, "제목의 최대 길이를 초과했습니다."), //제목 유효성 검사 실패
    STORE_CONTENT_IS_TOO_LONG(1013, "상세설명의 최대 길이를 초과했습니다."), //상세설명 유효성 검사 실패
    STORE_INVALID_PRICE(1014, "최대 가격을 초과했습니다."), //가격 유효성 검사 실패
    STORE_FAILED_TO_UPLOAD_FILE(1015, "파일 업로드에 실패했습니다."), //상품 파일 업로드 실패
    STORE_NONE_PRODUCT(1016, "판매중인 상품이 없습니다."), //등록된 상품 없음
    FILE_DELETED(1017, "해당 이미지가 존재하지 않습니다."), //해당 이미지 조회 실패
    STORE_NONE_CATEGORY(1018, "등록된 카테고리가 없습니다."), //등록된 카테고리 없음
    STORE_INVALID_FILE(1019, "첨부된 파일이 없습니다."), //첨부파일 오류
    PAYMENT_FAIL(1020, "결제에 실패했습니다."), //결제실패
    NONE_PAYMENT(1021, "결제내역이 없습니다."), //등록된 결제내역 없음
    INVALID_MEMBER_PHONE_OR_RANDOM_NUMBER(1022, "핸드폰번호 또는 인증번호가 유효하지않습니다."), //유효하지않은 인증번호 또는 핸드폰번호

    MOVIE_DETAIL_NOT_FOUND(1100,"영화 상세정보를 찾을 수 없습니다."),
    MOVIE_REVIEW_NOT_TICKET(1101, "관람평 작성 가능한 관람권이 없습니다."),


    ETC_FAIL(1999, "서버 내부 오류로 실패했습니다.") //그 외 오류
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