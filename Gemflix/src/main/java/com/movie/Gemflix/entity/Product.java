package com.movie.Gemflix.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@DynamicInsert //insert시 null인 필드 제외
@Table(name = "PRODUCT")
public class Product extends BaseEntity{

    @Id
    @SequenceGenerator(name = "PR_ID_SEQ_GEN", sequenceName = "PR_ID_SEQ", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PR_ID_SEQ_GEN")
    private Long prId;

    private String name;
    private String content;
    private int price;
    private String status;
    private String imgLocation;
    private String category;

}