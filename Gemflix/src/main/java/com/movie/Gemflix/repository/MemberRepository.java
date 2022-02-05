package com.movie.Gemflix.repository;

import com.movie.Gemflix.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Integer> {

    //회원 한명 불러오기
    Optional<Member> findById(String id);

}
