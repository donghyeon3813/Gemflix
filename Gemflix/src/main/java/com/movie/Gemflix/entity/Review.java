package com.movie.Gemflix.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@DynamicInsert //insert시 null인 필드 제외
@Table(name = "REVIEW")
public class Review extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rvId;

    @OneToOne
    @JoinColumn(name = "TK_ID")
    private Ticket ticket;

    private String content;

    private float score;

    private String delStatus;

}
