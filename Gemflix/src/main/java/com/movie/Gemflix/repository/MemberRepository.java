package com.movie.Gemflix.repository;

import com.movie.Gemflix.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findById(String id);
    Optional<Member> findByEmail(String email);

}