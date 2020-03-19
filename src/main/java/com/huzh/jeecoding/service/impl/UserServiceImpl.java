package com.huzh.jeecoding.service.impl;

import com.github.pagehelper.Page;
import com.huzh.jeecoding.dao.UserMapper;
import com.huzh.jeecoding.entity.User;
import com.huzh.jeecoding.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public int delete(String id) {
        return 0;
    }

    @Override
    public int insert(User record) {
        return 0;
    }

    @Override
    public User get(String id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public User getUserByName(String username) {
        return userMapper.getUserByName(username);
    }

    @Override
    public Page<User> getPage() {
        return userMapper.selectAll();
    }

    @Override
    public int update(User record) {
        return 0;
    }
}
