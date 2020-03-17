package com.huzh.jeecoding.controller;

import com.huzh.jeecoding.entity.User;
import com.huzh.jeecoding.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName UserController
 * @Description TODO
 * @Date 2020/3/17 15:51
 * @Author huzh
 * @Version 1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public List<User> findAll() {
        return userService.selectAll();
    }
}
