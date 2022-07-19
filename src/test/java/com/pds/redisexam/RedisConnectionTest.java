package com.pds.redisexam;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
class RedisConnectionTest {
    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String KEY = "key";
    private static final String KEY2 = "key2";

    @Test
    void setStringOpsShouldSuccess() {
        redisTemplate.opsForValue().set(KEY, "value");
        String value = redisTemplate.opsForValue().get(KEY);
        assertThat(value).isEqualTo("value");
    }

    @Test
    @DisplayName("만료된 데이터를 조회할 경우 null 을 반환한다.")
    void findExpiredStringValueByKeyShouldReturnNull() {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(KEY2, "value", Duration.ofMillis(500));

        assertThat(valueOperations.get(KEY2)).isEqualTo("value");
        await().pollDelay(Duration.ofMillis(1000))
                .untilAsserted(() -> {
                    assertThat(valueOperations.get(KEY2)).isNull();
                });
    }

}
