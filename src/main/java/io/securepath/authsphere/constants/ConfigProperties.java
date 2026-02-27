package io.securepath.authsphere.constants;

import io.securepath.authsphere.config.RedisConfig;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class ConfigProperties {
    private final StringRedisTemplate redisTemplate;

    // Static map accessible everywhere
    public static final Map<String, String> SECRETS = new HashMap<>();

    public ConfigProperties(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void init() {
        // Fetch all keys from Redis
        Set<String> keys = redisTemplate.keys("*"); // careful: can be expensive in production
        for (String key : keys) {
            String value = redisTemplate.opsForValue().get(key);
            SECRETS.put(key, value);
        }
    }

}
