package com.huzh.jeecoding.dao.mapper;

import com.huzh.jeecoding.dao.entity.Permission;

import java.util.List;

public interface PermissionMapper {
    int deleteByPrimaryKey(String id);

    int insert(Permission record);

    Permission selectByPrimaryKey(String id);

    List<Permission> selectAll();

    int updateByPrimaryKey(Permission record);
}