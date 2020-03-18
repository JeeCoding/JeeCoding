package com.huzh.jeecoding.controller;

import com.huzh.jeecoding.entity.User;
import com.huzh.jeecoding.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName UserController
 * @Description TODO
 * @Date 2020/3/17 15:51
 * @Author huzh
 * @Version 1.0
 */
@Api(tags = "UserController", description = "用户管理")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation("分页查询品牌列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public List<User> findAll(@RequestParam(value = "pageNum", defaultValue = "1L")
                                  @ApiParam("页码") Integer pageNum,
                              @RequestParam(value = "pageSize", defaultValue = "3L")
                                  @ApiParam("每页数量") Integer pageSize) {
        return userService.selectAll();
    }
}
