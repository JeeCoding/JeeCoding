package com.huzh.jeecoding.dao;

import com.huzh.jeecoding.entity.Permission;

import java.util.List;

public interface PermissionMapper {
    int deleteByPrimaryKey(String id);

    int insert(Permission record);

    Permission selectByPrimaryKey(String id);

    List<Permission> selectAll();

    int updateByPrimaryKey(Permission record);
}