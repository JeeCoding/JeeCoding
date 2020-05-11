package com.huzh.jeecoding.security.shiro;

import com.huzh.jeecoding.common.constant.JWTConstant;
import com.huzh.jeecoding.common.constant.RedisConstant;
import com.huzh.jeecoding.common.util.JWTUtil;
import com.huzh.jeecoding.common.util.redis.RedisUtil;
import com.huzh.jeecoding.dao.entity.Permission;
import com.huzh.jeecoding.dao.entity.Role;
import com.huzh.jeecoding.dao.entity.User;
import com.huzh.jeecoding.security.jwt.JWTToken;
import com.huzh.jeecoding.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;


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
        String token = (String) authenticationToken.getCredentials();
        if (StringUtils.isEmpty(token)) {
            throw new AuthenticationException("token不能为空");
        }

        String username = JWTUtil.getClaim(token, JWTConstant.USERNAME);
        // 帐号为空
        if (StringUtils.isEmpty(username)) {
            throw new AuthenticationException("token中帐号为空(The account in Token is empty.)");
        }

        //如果需要可以根据业务实现db操作,这里根据service写死
        User loginUser = userService.getUserByName(username);
        if (loginUser == null) {
            throw new AuthenticationException("该帐号不存在(The account does not exist.)");
        }


        // 开始认证，要AccessToken认证通过，且Redis中存在RefreshToken，且两个Token时间戳一致
        if (JWTUtil.verify(token) && RedisUtil.exists(RedisConstant.PREFIX_SHIRO_REFRESH_TOKEN + username)) {
            // 获取RefreshToken的时间戳
            String currentTimeMillisRedis = RedisUtil.get(RedisConstant.PREFIX_SHIRO_REFRESH_TOKEN + username);
            // 获取AccessToken时间戳，与RefreshToken的时间戳对比
            if (JWTUtil.getClaim(token, JWTConstant.CURRENT_TIME_MILLIS).equals(currentTimeMillisRedis)) {
                return new SimpleAuthenticationInfo(token, token, "userRealm");
            }
        }
        throw new AuthenticationException("Token已过期(Token expired or incorrect.)");
    }

    /**
     * 授权认证
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String token = principals.toString();
        // 返回当前用户所拥有的角色、权限等信息，根据自身项目编码即可
        String username = JWTUtil.getClaim(token, JWTConstant.USERNAME);
        User loginUser = userService.getUserByName(username);
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        for (Role role : loginUser.getRoles()) {
            authorizationInfo.addRole(role.getRole());
            for (Permission permission : role.getPermissions()) {
                authorizationInfo.addStringPermission(permission.getPermission());
            }
        }
        return authorizationInfo;
    }
}

