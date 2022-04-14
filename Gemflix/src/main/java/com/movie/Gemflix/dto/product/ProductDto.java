package com.movie.Gemflix.dto.product;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    @NotBlank
    @Size(min = 1, max = 20, message = "제목은 20자 이내로 입력해주세요.")
    private String name;

    @NotBlank
    @Size(min = 1, max = 500, message = "상세설명은 500자 이내로 입력해주세요.")
    private String content;

    @Min(value = 1, message = "1원 이상만 가능합니다.")
    @Max(value = 1000000, message = "100만원 이하만 가능합니다.")
    private String price;

    private int prId;
    private String status;
    private String imgLocation;
    private MultipartFile multiPartFile;
    private String base64;
    private String memberId;
    private String delStatus;

    private CategoryDto category;
    private long cgId;
    private String cgName;

}