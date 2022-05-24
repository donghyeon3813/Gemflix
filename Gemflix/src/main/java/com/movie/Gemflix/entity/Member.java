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

    private String id;
    private String password;
    private int point;
    private String phone;
    private String status;
    private String email;
    private MemberRole authority;
    private String grade;
    private String delStatus;
    private String fromSocial;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<PointHistory> pointHistories = new ArrayList<>();

}