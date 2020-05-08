package com.huzh.jeecoding.dao.mapper;


import com.github.pagehelper.Page;
import com.huzh.jeecoding.dao.entity.User;

/**
 * @author huzh
 */
public interface UserMapper {

    int deleteByPrimaryKey(String id);

    int insert(User record);

    User selectByPrimaryKey(String id);

    User getUserByName(String username);

    Page<User> selectAll();

    int updateByPrimaryKey(User record);
}