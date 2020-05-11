package com.huzh.jeecoding.security.shiro.cache;

import com.huzh.jeecoding.common.constant.JWTConstant;
import com.huzh.jeecoding.common.constant.RedisConstant;
import com.huzh.jeecoding.common.util.JWTUtil;
import com.huzh.jeecoding.common.util.redis.RedisUtil;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author huzh
 * @description: 重写Shiro的Cache保存读取
 * @date 2020/5/11 9:58
 */
public class CustomCache<K, V> implements Cache<K, V> {

    private String shiroCacheExpireTime = "600";

    private RedisTemplate redisTemplate;

    public CustomCache(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 缓存的key名称获取为shiro:cache:username
     *
     * @param key
     * @return
     */
    private String getKey(Object key) {
        return RedisConstant.PREFIX_SHIRO_CACHE + JWTUtil.getClaim(key.toString(), JWTConstant.USERNAME);
    }


    @Override
    public Object get(Object key) throws CacheException {
        if (Boolean.FALSE.equals(RedisUtil.exists(this.getKey(key)))) {
            return null;
        }
        return redisTemplate.opsForValue().get(this.getKey(key));
    }

    @Override
    public Object put(Object key, Object value) throws CacheException {
        try {
            redisTemplate.opsForValue().set(this.getKey(key), value, Integer.parseInt(shiroCacheExpireTime), TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Object remove(Object key) throws CacheException {
        if (Boolean.FALSE.equals(RedisUtil.exists(this.getKey(key)))) {
            return null;
        }
        redisTemplate.delete(this.getKey(key));
        return null;
    }

    @Override
    public void clear() throws CacheException {
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Set<K> keys() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }
}
