package com.huzh.jeecoding.config;

import com.huzh.jeecoding.entity.Permission;
import com.huzh.jeecoding.entity.Role;
import com.huzh.jeecoding.entity.User;
import com.huzh.jeecoding.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;

/**
 * @ClassName MyShiroRealm
 * @Description TODO
 * @Date 2020/3/19 17:06
 * @Author huzh
 * @Version 1.0
 */
public class MyShiroRealm extends AuthorizingRealm {

    @Resource
    private UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        System.out.println("权限配置-->MyShiroRealm.doGetAuthorizationInfo()");
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        User user = (User) principals.getPrimaryPrincipal();
        for (Role role : user.getRoles()) {
            authorizationInfo.addRole(role.getRole());
            for (Permission permission : role.getPermissions()) {
                authorizationInfo.addStringPermission(permission.getPermission());
            }
        }
        return authorizationInfo;    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        System.out.println("MyShiroRealm.doGetAuthenticationInfo()");
        //获得当前用户的用户名
        String username = (String) token.getPrincipal();
        System.out.println(token.getCredentials());
        //根据用户名找到对象
        //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
        User user = userService.get(username);
//        System.out.println("----->>userInfo=" + user);
        if (user == null) {
            return null;
        }
        //这里会去校验密码是否正确
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user, //用户名
                user.getPassWord(), //密码
                ByteSource.Util.bytes(user.getCredentialsSalt()),//salt=username+salt
                getName()  //realm name
        );
        return authenticationInfo;

    }
}
