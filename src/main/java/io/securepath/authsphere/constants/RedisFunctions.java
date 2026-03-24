package io.securepath.authsphere.constants;

import jakarta.annotation.PostConstruct;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisFunctions {
    private final StringRedisTemplate redisTemplate;

    // Static map accessible everywhere
    public static final Map<String, String> SECRETS = new HashMap<>();

    public RedisFunctions(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void init() {
        Set<String> keys = redisTemplate.keys("*");
        for (String key : keys) {
            String type = redisTemplate.type(key).code(); // returns "string", "hash", etc.

            if ("string".equals(type)) {
                String value = redisTemplate.opsForValue().get(key);
                SECRETS.put(key, value);
            } else if ("hash".equals(type)) {
                Map<Object, Object> map = redisTemplate.opsForHash().entries(key);
                // You can decide how to flatten or store this in SECRETS
                map.forEach((k, v) -> SECRETS.put(key + ":" + k, v.toString()));
            }
            // optionally handle list/set/zset if you use them
        }
    }

    public void storeResetPassToken(String pKey_UserId, String pToken) {
        Map<String, String> lUserData = new HashMap<>();
        lUserData.put("reset_pass_token", pToken);
        lUserData.put("createdAt", Instant.now().toString());

        // Store under a namespaced key
        String redisKey = "userid:" + pKey_UserId;

        // Save hash
        redisTemplate.opsForHash().putAll(redisKey, lUserData);

        // Apply expiry to the same key
        redisTemplate.expire(redisKey, 1, TimeUnit.HOURS);
    }

    public Map<Object, Object> findRestTokenByUserId(String pUserId) {
        Map<Object, Object> lUserData = new HashMap<>();
        try {
            lUserData = redisTemplate.opsForHash().entries(pUserId);
        } catch (Exception e) {
            lUserData = null;
        }
        return lUserData;
    }

}
