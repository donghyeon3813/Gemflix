package com.movie.Gemflix.entity;

import lombok.*;
import org.springframework.security.access.vote.RoleHierarchyVoter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "MEMBER")
public class Member {

    @Id
    @SequenceGenerator(name = "M_ID_SEQ_GEN", sequenceName = "M_ID_SEQ", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_ID_SEQ_GEN")
    private int mId;

    private String id;
    private String password;
    private int point;
    private String phone;
    private String status;
    private String email;
    private MemberRole authority;
    private String grade;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private String delStatus;

    /*@ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default //builder 사용시 기본값 설정
    private Set<MemberRole> roleSet = new HashSet<>();

    public void addMemberRole(MemberRole memberRole){
        roleSet.add(memberRole);
    }*/

}