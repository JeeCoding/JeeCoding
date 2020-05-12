package com.huzh.jeecoding.security.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author huzh
 * @description: TODO
 * @date 2020/5/11 10:05
 */
public class CustomCacheManager implements CacheManager {

    private String shiroCacheExpireTime;
    private RedisTemplate<String, Object> redisTemplate;


    public CustomCacheManager(RedisTemplate redisTemplate,String shiroCacheExpireTime) {
        this.redisTemplate = redisTemplate;
        this.shiroCacheExpireTime = shiroCacheExpireTime;
    }

    @Override
    public <K, V> Cache<K, V> getCache(String s) throws CacheException {
        return new CustomCache<K, V>(redisTemplate,shiroCacheExpireTime);
    }
}
