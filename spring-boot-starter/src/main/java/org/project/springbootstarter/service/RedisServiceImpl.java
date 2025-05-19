package org.project.springbootstarter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisTemplate<String, Long> redisTemplate;

    public void save(String key, Long value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public Long get(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}