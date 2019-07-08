package bhe.net.cn.base;

import java.util.Map;
import java.util.Set;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

/**
 * Helper class that simplifies Redis data access code.
 * <p/>
 * Performs automatic serialization/deserialization between the given objects
 * and the underlying binary data in the Redis store. By default, it uses Java
 * serialization for its objects (through JdkSerializationRedisSerializer ). For
 * String intensive operations consider the dedicated StringRedisTemplate.
 * <p/>
 * The central method is execute, supporting Redis access code implementing the
 * RedisCallback interface. It provides RedisConnection handling such that
 * neither the RedisCallback implementation nor the calling code needs to
 * explicitly care about retrieving/closing Redis connections, or handling
 * Connection lifecycle exceptions. For typical single step actions, there are
 * various convenience methods.
 * <p/>
 * Once configured, this class is thread-safe.
 *
 */
@Component
public class RedisTemplateUtils {

    private RedisTemplate<String, String> redisTemplate;
    private RedisSerializer<String> redisSerializer = new StringRedisSerializer();
    private HashOperations<String, Object, Object> hashOperations;

    public RedisTemplateUtils(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        hashOperations = redisTemplate.opsForHash();
    }

    public Long del(byte[] key) {
        return redisTemplate.execute((RedisCallback<Long>) connection -> {
            return connection.del(key);
        });
    }

    public Long set(byte[] key, byte[] value, long seconds) {
        return redisTemplate.execute((RedisCallback<Long>) connection -> {
            connection.set(key, value);
            connection.expire(key, seconds);
            return 1L;
        });
    }

    public byte[] get(byte[] key) {
        return redisTemplate.execute((RedisCallback<byte[]>) connection -> {
            return connection.get(key);
        });
    }

    public Long set(String key, String value, long seconds) {
        return redisTemplate.execute((RedisCallback<Long>) connection -> {
            byte[] bytekey = redisSerializer.serialize(key);
            byte[] bytevalue = redisSerializer.serialize(value);
            connection.set(bytekey, bytevalue);
            connection.expire(bytekey, seconds);
            return 1L;
        });
    }

    public Long getTime(String key) {
        return redisTemplate.execute((RedisCallback<Long>) connection -> {
            long time = connection.ttl(redisSerializer.serialize(key));
            return time;
        });
    }

    public Set<String> keys(String pattern) {
        return redisTemplate.execute((RedisCallback<Set<String>>) connection -> redisTemplate.keys("*" + pattern + "*"));
    }

    public String get(String key) {
        return redisTemplate.execute((RedisCallback<String>) connection -> redisSerializer.deserialize(connection.get(redisSerializer.serialize(key))));
    }

    public void put(String key, Object hashKey, Object value) {
        hashOperations.put(key, hashKey, value);
    }

    public Object get(String key, Object hashKey) {
        return hashOperations.get(key, hashKey);
    }

    public void delete(String key, Object... hashKeys) {
        hashOperations.delete(key, hashKeys);
    }

    public Map<Object, Object> entries(String key) {
        return hashOperations.entries(key);
    }

    public long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }

}
