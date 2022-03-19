package com.movie.Gemflix.security.service;

import com.movie.Gemflix.entity.Member;
import com.movie.Gemflix.entity.MemberRole;
import com.movie.Gemflix.repository.member.MemberRepository;
import com.movie.Gemflix.security.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername username: {}", username);
        Optional<Member> result = memberRepository.findById(username);

        if(!result.isPresent()){
            throw new UsernameNotFoundException("Check Id");
        }
        Member member = result.get();
        log.info("member: {}", member);
        return entityToDTO(member);
    }

    //Entity -> DTO
    public UserDTO entityToDTO(Member member){
        return new UserDTO(
                member.getId(),
                member.getPassword(),
                member.getPoint(),
                member.getPhone(),
                member.getStatus(),
                member.getEmail(),
                member.getGrade(),
                member.getRegDate(),
                member.getModDate(),
                member.getDelStatus(),
                getRoleSet(member.getAuthority())
        );
    }

    private Collection<GrantedAuthority> getRoleSet(MemberRole role){
        Set<GrantedAuthority> roleSet = new HashSet<>();
        roleSet.add(new SimpleGrantedAuthority("ROLE_" + role));
        return roleSet;
    }

}
