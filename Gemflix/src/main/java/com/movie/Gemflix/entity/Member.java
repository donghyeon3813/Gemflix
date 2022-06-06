package com.movie.Gemflix.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.movie.Gemflix.dto.member.PointHistoryDto;
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
@ToString(exclude = "pointHistories")
@DynamicInsert //insert시 null인 필드 제외
@Table(name = "MEMBER")
public class Member extends BaseEntity {

    @Id
    @Column(name = "M_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mId;

    @Column(name = "ID")
    private String id;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "POINT")
    private int point;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "AUTHORITY")
    private MemberRole authority;

    @Column(name = "GRADE")
    private String grade;

    @Column(name = "DEL_STATUS")
    private String delStatus;

    @Column(name = "FROM_SOCIAL")
    private String fromSocial;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<PointHistory> pointHistories = new ArrayList<>();

}