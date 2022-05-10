package com.movie.Gemflix.dto.movie;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PeopleDto {
    private Long peId;
    private String name;
    private String type;
    private String nationality;
    private Date birth;
    private String apiId;
}
