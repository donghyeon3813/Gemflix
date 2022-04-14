package com.movie.Gemflix.dto.movie;

import lombok.*;
import org.springframework.data.domain.Pageable;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieSearchDto{
    private String title;
    private Long mvId;
}
