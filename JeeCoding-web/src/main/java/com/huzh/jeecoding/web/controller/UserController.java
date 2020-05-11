package com.huzh.jeecoding.web.controller;

import com.github.pagehelper.PageHelper;
import com.huzh.jeecoding.common.api.RestResult;
import com.huzh.jeecoding.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huzh
 * @description: 用户
 * @date 2020/5/7 20:28
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
    public RestResult findAll(@RequestParam(value = "pageNum", defaultValue = "1")
                              @ApiParam("页码") Integer pageNum,
                              @RequestParam(value = "pageSize", defaultValue = "2")
                              @ApiParam("每页数量") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return RestResult.success(userService.getPage());
    }
}
