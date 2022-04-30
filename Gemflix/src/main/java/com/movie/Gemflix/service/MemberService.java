package com.movie.Gemflix.service;

import com.movie.Gemflix.common.Constant;
import com.movie.Gemflix.entity.Member;
import com.movie.Gemflix.repository.member.MemberRepository;
import com.movie.Gemflix.security.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.java_sdk.api.Message;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final RedisUtil redisUtil;
    private final MemberRepository memberRepository;

    @Value("${msg.api.key}")
    private  String msgApiKey;

    @Value("${msg.api.secret}")
    private  String msgApiSecret;

    private static final String CALLBACK = "01090148265";
    private static final Long PHONE_NUMBER_EXPIRE_TIME = 60 * 3L; //3분

    public boolean certifyPhone(String userPhone) {
        try {
            Message coolsms = new Message(msgApiKey, msgApiSecret);
            HashMap<String, String> params = new HashMap<>();

            String randomNumber = String.valueOf(Math.random()).substring(2, 7);

            params.put("to", CALLBACK); //발신번호
            params.put("from", userPhone); //수신번호
            params.put("type", "SMS");
            params.put("text", "[Gemflix] 인증번호는 " + randomNumber + "입니다.");
            params.put("app_version", "test app 1.2");
            redisUtil.setStringDataExpire(RedisUtil.PREFIX_PHONE_KEY + randomNumber, userPhone, PHONE_NUMBER_EXPIRE_TIME);

            log.info("params: {}", params);
            JSONObject result = coolsms.send(params);
            log.info("result: {}", result);
            return true;

        } catch (Exception e) {
            e.getMessage();
            return false;
        }
    }

    public List<String> verifyPhone(String requestUserPhone, String randomNumber) {
        boolean result = false;
        List<String> memberIds = null;
        String userPhone = redisUtil.getStringData(RedisUtil.PREFIX_PHONE_KEY + randomNumber);

        if(userPhone != null){
            if(userPhone.equals(requestUserPhone)){
                //핸드폰번호를 이용한 멤버정보 조회
                Optional<List<Member>> otpMembers =
                        memberRepository.findByPhoneAndDelStatus(requestUserPhone, Constant.BooleanStringValue.FALSE);

                if(otpMembers.isPresent()){
                    List<Member> members = otpMembers.get();
                    memberIds = members.stream().map(Member::getId).collect(Collectors.toList());
                    redisUtil.deleteData(RedisUtil.PREFIX_PHONE_KEY + randomNumber);
                    result = true;
                }
            }
        }

        if(result){
            return memberIds;
        }else{
            return null;
        }
    }

}
