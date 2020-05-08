package com.huzh.jeecoding.security.jwt;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author huzh
 * @date 2020/3/20 15:26
 */
public class JWTToken implements AuthenticationToken {

    private static final long serialVersionUID = 1L;
    private String token;

    public JWTToken(String token) {
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