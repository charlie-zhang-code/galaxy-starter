package io.charlie.config;

import org.apache.ibatis.cache.Cache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MybatisPlusRedisCache implements Cache {
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final String id; // 缓存实例id
    private RedisTemplate<String, Object> redisTemplate;

    // 必须的构造函数
    public MybatisPlusRedisCache(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Cache instances require an ID");
        }
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void putObject(Object key, Object value) {
        if (value != null) {
//            getRedisTemplate().opsForValue().set(key.toString(), value, 120, TimeUnit.MINUTES); // 120 分钟
            getRedisTemplate().opsForValue().set(key.toString(), value, 5, TimeUnit.MINUTES); // 5 分钟
        }
    }

    @Override
    public Object getObject(Object key) {
        try {
            if (key != null) {
                return getRedisTemplate().opsForValue().get(key.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object removeObject(Object key) {
        if (key != null) {
            getRedisTemplate().delete(key.toString());
        }
        return null;
    }

    @Override
    public void clear() {
        Set<String> keys = getRedisTemplate().keys("*:" + this.id + "*");
        if (!CollectionUtils.isEmpty(keys)) {
            getRedisTemplate().delete(keys);
        }
    }

    @Override
    public int getSize() {
        Set<String> keys = getRedisTemplate().keys("*:" + this.id + "*");
        return keys != null ? keys.size() : 0;
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return this.readWriteLock;
    }

    private RedisTemplate<String, Object> getRedisTemplate() {
        if (redisTemplate == null) {
            redisTemplate = SpringContextHolder.getBean("redisTemplate");
        }
        return redisTemplate;
    }
}