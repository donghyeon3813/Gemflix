package com.movie.Gemflix.dto.product;

import lombok.*;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    private long cgId;
    private String cgName;

}