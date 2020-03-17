package com.huzh.jeecoding.service;

import com.huzh.jeecoding.entity.User;

import java.util.List;

/**
 * @ClassName UserService
 * @Description TODO
 * @Date 2020/3/17 15:29
 * @Author huzh
 * @Version 1.0
 */
public interface UserService {
    int deleteByPrimaryKey(Long id);

    void insert(User record);

    User selectByPrimaryKey(Long id);

    List<User> selectAll();

    int updateByPrimaryKey(User record);
}
