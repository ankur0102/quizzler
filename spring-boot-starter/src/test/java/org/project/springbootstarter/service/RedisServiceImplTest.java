package org.project.springbootstarter.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
public class RedisServiceImplTest {

    @InjectMocks
    private RedisServiceImpl redisService;

    @Mock
    private RedisTemplate<String, Long> redisTemplate;

    @Mock
    private ValueOperations<String, Long> valueOperations;

    @Test
    void testSave() {
        Mockito.when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        redisService.save("testKey", 123L);
        Mockito.verify(valueOperations, Mockito.times(1)).set("testKey", 123L);
    }

    @Test
    void testGet() {
        Mockito.when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        Mockito.when(valueOperations.get(anyString())).thenReturn(123L);
        Long value = redisService.get("testKey");
        Assertions.assertNotNull(value);
        Assertions.assertEquals(123L, value);
        Mockito.verify(valueOperations, Mockito.times(1)).get("testKey");
    }
}