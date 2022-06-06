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

    @Column(name = "NAME")
    private String name;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "PRICE")
    private int price;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "IMG_LOCATION")
    private String imgLocation;

    @Column(name = "DEL_STATUS")
    private String delStatus;

    @ManyToOne
    @JoinColumn(name = "CG_ID")
    private Category category;

}