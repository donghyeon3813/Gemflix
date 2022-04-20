package com.movie.Gemflix.common;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Constant {

    public static class Success{
        public static final int SUCCESS_CODE = 1000;
    }

    public static class BooleanStringValue{
        public static final String TRUE = "1";
        public static final String FALSE = "0";
    }

    //1: 브론즈 (기본 등급), 2: 실버 (한달내 영화관람 3편 이상), 3: 골드 (한달내 영화관람 6편 이상), 4: VIP (한달내 영화관람 10편 이상)
    public static class Grade{
        public static final String BRONZE = "1";
        public static final String SILVER = "2";
        public static final String GOLD = "3";
        public static final String VIP = "4";
    }

    //0: 신규가입 적립, 1: 출석체크 적립, 2: 영화관람 적립, 3: 리뷰등록 적립, 4: 결제시 차감
    public static class PointType{
        public static final String REGISTER_POINT = "1";
        public static final String ATTENDANCE_POINT = "2";
        public static final String MOVIE_VIEW_POINT = "3";
        public static final String PAYMENT_POINT = "4";
    }

    public static class FileExtension{
        public static final String[] JPG_AND_PNG = {"jpg", "jpeg", "png"};
    }

    public static class FilePath{
        public static final String PATH_STORE = "C:/TestFolder/";
    }

    public static class StoreCategory{
        public static final Set<String> STORE_CATEGORY_SET = new HashSet(Arrays.asList("snack", "movieTicket", "photoTicket"));
    }

}