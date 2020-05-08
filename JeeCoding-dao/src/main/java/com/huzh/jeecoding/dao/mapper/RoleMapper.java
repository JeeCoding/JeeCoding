package com.huzh.jeecoding.dao.mapper;


import com.huzh.jeecoding.dao.entity.Role;

import java.util.List;

public interface RoleMapper {

    int deleteByPrimaryKey(String id);

    int insert(Role record);

    Role selectByPrimaryKey(String id);

    List<Role> selectAll();

    int updateByPrimaryKey(Role record);
}