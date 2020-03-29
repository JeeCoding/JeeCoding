package com.huzh.jeecoding.config.shiro.jwt;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author huzh
 * @date 2020/3/20 15:26
 */
public class JwtToken implements AuthenticationToken {

    private static final long serialVersionUID = 1L;
    private String token;

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}