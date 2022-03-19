package com.movie.Gemflix.service;

import com.movie.Gemflix.common.CommonResponse;
import com.movie.Gemflix.common.ErrorType;
import com.movie.Gemflix.security.service.UserDetailsServiceImpl;
import com.movie.Gemflix.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonService {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;



    public CommonResponse checkError(BindingResult bindingResult) {
        log.info("bindingResult: {}", bindingResult);
        if(bindingResult.hasErrors()) {
            FieldError fieldError = (FieldError) bindingResult.getAllErrors().get(0);
            String field = fieldError.getField();
            String message = fieldError.getDefaultMessage();
            log.info("fieldError: {}, field: {}, message: {}", fieldError, field, message);

            switch (field){
                case "id":
                    return new CommonResponse(ErrorType.INVALID_MEMBER_ID.getErrorCode(),
                            ErrorType.INVALID_MEMBER_ID.getErrorMessage());
                case "password":
                    return new CommonResponse(ErrorType.INVALID_MEMBER_PASSWORD.getErrorCode(),
                            ErrorType.INVALID_MEMBER_PASSWORD.getErrorMessage());
                case "phone":
                    return new CommonResponse(ErrorType.INVALID_MEMBER_PHONE.getErrorCode(),
                            ErrorType.INVALID_MEMBER_PHONE.getErrorMessage());
                case "email":
                    return new CommonResponse(ErrorType.INVALID_MEMBER_EMAIL.getErrorCode(),
                            ErrorType.INVALID_MEMBER_EMAIL.getErrorMessage());
                case "name":
                    return new CommonResponse(ErrorType.STORE_NAME_IS_TOO_LONG.getErrorCode(),
                            ErrorType.STORE_NAME_IS_TOO_LONG.getErrorMessage());
                case "content":
                    return new CommonResponse(ErrorType.STORE_CONTENT_IS_TOO_LONG.getErrorCode(),
                            ErrorType.STORE_CONTENT_IS_TOO_LONG.getErrorMessage());
                case "price":
                    return new CommonResponse(ErrorType.STORE_INVALID_PRICE.getErrorCode(),
                            ErrorType.STORE_INVALID_PRICE.getErrorMessage());
                default:
                    return new CommonResponse(ErrorType.ETC_FAIL.getErrorCode(), ErrorType.ETC_FAIL.getErrorMessage());
            }
        }else{
            return null;
        }
    }

    public String getRequesterId(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        String accessToken = authHeader.substring(7);
        log.info("accessToken: {}", accessToken);
        return jwtUtil.getUsernameFromToken(accessToken);
    }

    public boolean checkFile(MultipartFile mfile, String[] type) throws Exception {
        //파일 확장자 검사
        String extension = FilenameUtils.getExtension(mfile.getOriginalFilename());
        int resultCnt = 0;

        for(String s : type){
            if (extension.equals(s)) {
                resultCnt = 0;
                break;
            }else{
                resultCnt++;
            }
        }
        if(resultCnt > 0) return false;

        //Mimetype 검사
        Tika tika = new Tika();
        String mimeType = tika.detect(mfile.getInputStream());

        if(extension.equals("zip") || extension.equals("pdf")){
            return mimeType.startsWith("application");

        }else if(extension.equals("jpg") || extension.equals("png") || extension.equals("tif")){
            return mimeType.startsWith("image");
        }
        return true;
    }

    public String uploadFile(MultipartFile file, String location){
        try {
            //파일 업로드
            String originFileName = file.getOriginalFilename(); //원본 파일명
            long fileSize = file.getSize(); //파일 사이즈

            String safeFile = System.currentTimeMillis() + "_" + originFileName;

            String pathFile = location + safeFile;
            Path path = Paths.get(pathFile);
            Files.createDirectories(path.getParent()); //path 경로에 있는 부모 디렉터리를 생성 한다.

            File f1 = new File(pathFile);
            file.transferTo(f1);
            return pathFile;

        }catch (Exception e){
            log.error("[CREATE FILE ERROR] file: {}, location: {}, e: {}", file, location, e);
            e.printStackTrace();
            return null;
        }
    }

}