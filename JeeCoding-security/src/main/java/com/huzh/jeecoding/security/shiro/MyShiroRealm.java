package com.huzh.jeecoding.security.shiro;

import com.huzh.jeecoding.dao.entity.Permission;
import com.huzh.jeecoding.dao.entity.Role;
import com.huzh.jeecoding.dao.entity.User;
import com.huzh.jeecoding.security.jwt.JWTToken;
import com.huzh.jeecoding.security.util.JWTUtil;
import com.huzh.jeecoding.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @ClassName MyShiroRealm
 * @Description TODO
 * @Date 2020/3/19 17:06
 * @Author huzh
 * @Version 1.0
 */
@Slf4j
public class MyShiroRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;
    @Autowired
    private JWTUtil jwtUtil;


    /**
     * 设置realm支持的authenticationToken类型,必须重写此方法，不然会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return null != token && token instanceof JWTToken;
    }

    /**
     * 登陆认证
     *
     * @param authenticationToken jwtFilter传入的token
     * @return 登陆信息
     * @throws AuthenticationException 未登陆抛出异常
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        //getCredentials getPrincipal getToken 都是返回jwt生成的token串
        String token = (String) authenticationToken.getCredentials();

        String username = jwtUtil.getUserNameFromToken(token);
        if (username == null) {
            throw new AccountException("token失效！");
        }
        //如果需要可以根据业务实现db操作,这里根据service写死
        User loginUser = userService.getUserByName(username);
        if (loginUser == null) {
            throw new AuthenticationException("用户不存在！");
        }

        if (!jwtUtil.validateToken(token, loginUser.getUserName())) {
            throw new UnknownAccountException("用户名或密码错误！");
        }

        if ("0".equals(loginUser.getState())) {
            throw new AuthenticationException("该用户已被封号！");
        }
        return new SimpleAuthenticationInfo(token, token, getName());
    }

    /**
     * 授权认证
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String token = principals.toString();
        //根据token获取权限授权
        String username = jwtUtil.getUserNameFromToken(token);
        User loginUser = userService.getUserByName(username);
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        for (Role role : loginUser.getRoles()) {
            authorizationInfo.addRole(role.getRole());
            for (Permission permission : role.getPermissions()) {
                authorizationInfo.addStringPermission(permission.getPermission());
            }
        }
//        authorizationInfo.setRoles(loginUser.getRoles());
//        authorizationInfo.setStringPermissions(loginUser.getPermissions());
        return authorizationInfo;
    }
}

