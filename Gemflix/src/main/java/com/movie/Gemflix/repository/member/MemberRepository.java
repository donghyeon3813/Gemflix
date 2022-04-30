package com.movie.Gemflix.repository.member;

import com.movie.Gemflix.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByIdAndDelStatus(String id, String delStatus);
    Optional<Member> findByEmailAndDelStatus(String email, String delStatus);
    Optional<List<Member>> findByPhoneAndDelStatus(String phone, String delStatus);

}