package com.movie.Gemflix.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "category")
@DynamicInsert //insert시 null인 필드 제외
@Table(name = "PRODUCT")
public class Product extends BaseEntity{

    @Id
    @Column(name = "PR_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long prId;

    private String name;
    private String content;
    private int price;
    private String status;
    private String imgLocation;
    private String delStatus;

    @ManyToOne
    @JoinColumn(name = "CG_ID")
    private Category category;

}