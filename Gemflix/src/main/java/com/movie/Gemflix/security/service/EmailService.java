package com.movie.Gemflix.security.service;

import com.movie.Gemflix.dto.MemberDTO;
import com.movie.Gemflix.entity.Member;
import com.movie.Gemflix.entity.MemberRole;
import com.movie.Gemflix.repository.MemberRepository;
import com.movie.Gemflix.security.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;
    private final RedisUtil redisUtil;
    private final MemberRepository memberRepository;

    private static final String MAIL_VERIFICATION_LINK = "http://localhost:9091/verify/";
    private static final String MAIL_SUBJECT = "[Gemflix] 회원가입 인증메일입니다.";
    private static final Long EXPIRE_TIME = 60 * 30L;

    public boolean sendVerificationMail(MemberDTO memberDTO) throws Exception{
        try {
            String uuid = UUID.randomUUID().toString();
            String memberId = memberDTO.getId();
            redisUtil.setStringDataExpire(uuid, memberId, EXPIRE_TIME);
            String text = "안녕하세요. " + memberId + "님," +
                    "\n 보석같은 영화를 제공하는 Gemplix 입니다." +
                    "\n 아래 링크를 눌러 Email 인증을 완료하세요." +
                    "\n " + MAIL_VERIFICATION_LINK + uuid;
            sendMail(memberDTO.getEmail(), MAIL_SUBJECT, text);
        }catch (Exception e){
            e.printStackTrace();
            log.error("fail to send mail.");
            return false;
        }
        return true;
    }

    public void sendMail(String to, String sub, String text) throws Exception{
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(sub);
        message.setText(text);
        emailSender.send(message);
    }

}
