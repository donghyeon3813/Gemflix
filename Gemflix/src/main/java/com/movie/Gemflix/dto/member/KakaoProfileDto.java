package com.movie.Gemflix.dto.member;

import lombok.Data;

@Data
public class KakaoProfileDto {
    public Integer id;
    public String connected_at;
    public Properties properties;
    public KakaoAccount kakao_account;

    @Data
    public class Properties {
        public String nickname;
    }

    @Data
    public class KakaoAccount {
        public Boolean profile_nickname_needs_agreement;
        public Boolean has_email;
        public Boolean email_needs_agreement;
        public Boolean has_birthday;
        public Boolean birthday_needs_agreement;

        @Data
        public class Profile {
            public String nickname;
        }
    }
}