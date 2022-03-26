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

    public static class FileExtension{
        public static final String[] JPG_AND_PNG = {"jpg", "jpeg", "png"};
    }

    public static class FilePath{
        //        public static final String PATH_STORE = "C:/z_crim/toy/Gemflix/Gemflix-Front/public/images/upload/";
        public static final String PATH_STORE = "C:/TestFolder/";
    }

    public static class StoreCategory{
        public static final Set<String> STORE_CATEGORY_SET = new HashSet(Arrays.asList("snack", "movieTicket", "photoTicket"));
    }

}