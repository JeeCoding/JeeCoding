package com.huzh.jeecoding.service;

/**
 * @ClassName RedisService
 * @Description TODO
 * @Date 2020/3/19 14:42
 * @Author huzh
 * @Version 1.0
 */
public interface RedisService {

    void set(String key, String value);

    String get(String key);

    /**
     * 设置超期时间
     */
    boolean expire(String key, long expire);

    void remove(String key);

    /**
     * 自增操作
     * @param delta 自增步长
     */
    Long increment(String key, long delta);
}
