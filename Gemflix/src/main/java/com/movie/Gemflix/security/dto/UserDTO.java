package com.movie.Gemflix.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;
import java.util.Collection;

@Getter
@Setter
@ToString
public class UserDTO extends User {

    @JsonProperty("mId")
    private int mId;
    private String id;
    private String password;
    private int point;
    private String phone;
    private String status;
    private String email;
    private String authority;
    private String grade;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private String delStatus;

    public UserDTO(String username,
                   String password,
                   int point,
                   String phone,
                   String status,
                   String email,
                   String grade,
                   LocalDateTime regDate,
                   LocalDateTime modDate,
                   String delStatus,
                   Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = username;
        this.password = password;
        this.point = point;
        this.phone = phone;
        this.status = status;
        this.email = email;
        this.grade = grade;
        this.regDate = regDate;
        this.modDate = modDate;
        this.delStatus = delStatus;
        this.authority = authorities.stream().map(GrantedAuthority::getAuthority).toString();
    }
}
