package com.movie.Gemflix.dto.movie;

import lombok.Data;
import org.springframework.data.domain.Pageable;

@Data
public class MovieSearchDto{
    private String title;
    private Long mvId;
}
