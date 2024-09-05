package com.stone.judging.redis;

import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

public class RedisDistributedLock {

    private static final String LOCK_KEY_PREFIX = "lock:";
    private static final long LOCK_EXPIRE_TIME = 3000L;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    private final String lockKey;
    private final String lockValue;
    private boolean locked = false;

    public RedisDistributedLock(String lockKey, String lockValue) {
        this.lockKey = LOCK_KEY_PREFIX + lockKey;
        this.lockValue = lockValue;
    }

    public boolean lock() {
        if (Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(lockKey, lockValue))) {
            redisTemplate.expire(lockKey, LOCK_EXPIRE_TIME, TimeUnit.MILLISECONDS);
            locked = true;
            return true;
        } else {
            return false;
        }
    }

    public void unlock() {
        if (locked) {
            redisTemplate.delete(lockKey);
        }
    }
}

