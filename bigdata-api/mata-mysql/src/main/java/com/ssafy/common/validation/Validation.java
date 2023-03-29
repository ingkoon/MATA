package com.ssafy.common.validation;

import com.ssafy.entity.Project;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class Validation {
    private final StringRedisTemplate redisTemplate;

    public void setTokenToRedis(String uuid, Project project){
        redisTemplate.opsForValue().append(uuid, project.getId().toString());
    }

    public boolean checkRedisValidation(String uuid){
        if(redisTemplate.opsForValue().get(uuid)==null){
            return false;
        }
        return true;
    }
}
