package com.movie.Gemflix.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@DynamicInsert //insert시 null인 필드 제외
@Table(name = "CATEGORY")
public class Category {

    @Id
    @SequenceGenerator(name = "CG_ID_SEQ_GEN", sequenceName = "CG_ID_SEQ", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CG_ID_SEQ_GEN")
    private Long cgId;

    private String cgName;

}