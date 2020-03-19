package com.huzh.jeecoding.dao;

import com.github.pagehelper.Page;
import com.huzh.jeecoding.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    int deleteByPrimaryKey(String id);

    int insert(User record);

    User selectByPrimaryKey(String id);

    User getUserByName(String username);

    Page<User> selectAll();

    int updateByPrimaryKey(User record);
}