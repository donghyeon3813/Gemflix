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
    @Column(name = "CG_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cgId;

    @Column(name = "CG_NAME")
    private String cgName;

}