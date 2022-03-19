package com.movie.Gemflix.security.service;

import com.movie.Gemflix.dto.member.MemberDto;
import com.movie.Gemflix.repository.member.MemberRepository;
import com.movie.Gemflix.security.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;
    private final RedisUtil redisUtil;
    private final MemberRepository memberRepository;

    private static final String MAIL_VERIFICATION_LINK = "http://localhost:9090/auth/verify/";
    private static final String MAIL_SUBJECT = "[Gemflix] 회원가입 인증메일입니다.";
    private static final Long LINK_EXPIRE_TIME = 60 * 30L; //30분

    public boolean sendVerificationMail(MemberDto memberDTO) throws Exception{
        try {
            String uuid = UUID.randomUUID().toString();
            String memberId = memberDTO.getId();
            redisUtil.setStringDataExpire(RedisUtil.PREFIX_EMAIL_KEY + uuid, memberId, LINK_EXPIRE_TIME);
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
