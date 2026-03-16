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
        // Use SCAN instead of KEYS
        ScanOptions options = ScanOptions.scanOptions().match("secret:*").count(100).build();
        assert redisTemplate.getConnectionFactory() != null;
        Cursor<byte[]> cursor = redisTemplate.getConnectionFactory()
                .getConnection()
                .scan(options);

        while (cursor.hasNext()) {
            String key = new String(cursor.next());
            String value = redisTemplate.opsForValue().get(key);
            SECRETS.put(key, value);
        }
    }

    public void storeResetPassToken(String pKey_UserId, String pToken) {
        Map<String, String> lUserData = new HashMap<>();
        lUserData.put("enc-token",pToken);
        lUserData.put("createdAt", Instant.now().toString());
        redisTemplate.opsForHash().putAll(pKey_UserId, lUserData);
        redisTemplate.expire(pKey_UserId, 1, TimeUnit.HOURS); // auto-expire after 1 hour
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
