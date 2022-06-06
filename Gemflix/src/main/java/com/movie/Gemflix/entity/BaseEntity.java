package com.movie.Gemflix.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass //테이블로 생성되지 않는다 -> 이 클래스를 상속한 클래스로 테이블 생성됨
@EntityListeners(value = {AuditingEntityListener.class}) //JPA 사용시 데이터가 저장되는 context를 관리하는 리스너가 필요하다
@Getter
abstract class BaseEntity {

    @CreatedDate //엔티티 생성시간 처리
    @Column(name = "REG_DATE", updatable = false)
    private LocalDateTime regDate;

    @LastModifiedDate //최종 수정시간 자동처리
    @Column(name = "MOD_DATE")
    private LocalDateTime modDate;
}
