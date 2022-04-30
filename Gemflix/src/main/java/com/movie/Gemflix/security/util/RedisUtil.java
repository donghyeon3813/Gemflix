package com.movie.Gemflix.security.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    private final StringRedisTemplate stringRedisTemplate;

    public static final String PREFIX_EMAIL_KEY = "email:";
    public static final String PREFIX_PHONE_KEY = "phone:";
    public static final String PREFIX_REFRESH_TOKEN_KEY = "refresh.token:";

    //String Type
    public String getStringData(String key){
        ValueOperations<String, String> stringValueOperations = stringRedisTemplate.opsForValue();
        return stringValueOperations.get(key);
    }

    public void setStringData(String key, String value){
        ValueOperations<String, String> stringValueOperations = stringRedisTemplate.opsForValue();
        stringValueOperations.set(key,value);
    }

    public void setStringDataExpire(String key, String value, long duration){
        ValueOperations<String, String> stringValueOperations = stringRedisTemplate.opsForValue();
        Duration expireDuration = Duration.ofSeconds(duration);
        stringValueOperations.set(key, value, expireDuration);
    }

    public void deleteData(String key){
        stringRedisTemplate.delete(key);
    }

}
