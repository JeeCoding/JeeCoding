package com.huzh.jeecoding.service;

import com.github.pagehelper.Page;
import com.huzh.jeecoding.entity.User;

public interface UserService {

    int delete(String id);

    int insert(User record);

    User get(String id);

    User getUserByName(String username);

    Page<User> getPage();

    int update(User record);
}
