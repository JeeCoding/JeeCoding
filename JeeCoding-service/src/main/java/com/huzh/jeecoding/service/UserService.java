package com.huzh.jeecoding.service;


import com.github.pagehelper.Page;
import com.huzh.jeecoding.dao.entity.User;

/**
 * @author huzh
 */
public interface UserService {

    int delete(String id);

    int insert(User record);

    User get(String id);

    User getUserByName(String username);

    Page<User> getPage();

    int update(User record);
}
