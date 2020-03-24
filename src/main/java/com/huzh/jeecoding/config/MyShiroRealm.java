package com.huzh.jeecoding.config;

import com.huzh.jeecoding.entity.Permission;
import com.huzh.jeecoding.entity.Role;
import com.huzh.jeecoding.entity.User;
import com.huzh.jeecoding.service.UserService;
import com.huzh.jeecoding.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @ClassName MyShiroRealm
 * @Description TODO
 * @Date 2020/3/19 17:06
 * @Author huzh
 * @Version 1.0
 */
@Slf4j
public class MyShiroRealm extends AuthorizingRealm {

    @Resource
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * 必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = jwtTokenUtil.getUserNameFromToken(principals.toString());
        User user = userService.getUserByName(username);
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        for (Role role : user.getRoles()) {
            authorizationInfo.addRole(role.getRole());
            for (Permission permission : role.getPermissions()) {
                authorizationInfo.addStringPermission(permission.getPermission());
            }
        }
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String token = (String) auth.getCredentials();
        if (token == null) {
            throw new AuthenticationException("token为空!");
        }
        // 校验token有效性
        User loginUser = this.checkUserTokenIsEffect(token);
        return new SimpleAuthenticationInfo(loginUser, token, getName());
    }

    /**
     * 校验token的有效性
     *
     * @param token
     */
    public User checkUserTokenIsEffect(String token) throws AuthenticationException {
        // 解密获得username，用于和数据库进行对比
        String username = jwtTokenUtil.getUserNameFromToken(token);
        if (username == null) {
            throw new AuthenticationException("token非法无效!");
        }

        // 查询用户信息
        User loginUser = new User();
        User user = userService.getUserByName(username);
        if (user == null) {
            throw new AuthenticationException("用户不存在!");
        }

        // 校验token是否超时失效 & 或者账号密码是否错误
        if (!jwtTokenUtil.validateToken(token, user)) {
            throw new AuthenticationException("Token失效请重新登录!");
        }

        // 判断用户状态
        if ("0".equals(user.getState())) {
            throw new AuthenticationException("账号已被删除,请联系管理员!");
        }

        BeanUtils.copyProperties(user, loginUser);
        return loginUser;
    }
}
