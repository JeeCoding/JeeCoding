package com.huzh.jeecoding.security.jwt;

import com.alibaba.fastjson.JSONObject;
import com.huzh.jeecoding.common.api.RestResult;
import com.huzh.jeecoding.common.api.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * JWT过滤
 *
 * @author huzh
 */
@Slf4j
public class JWTFilter extends BasicHttpAuthenticationFilter {

    private static final String AUTHZ_HEADER = "Authorization";
    private static final String CHARSET = "UTF-8";

    /**
     * 处理未经验证的请求
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response)
            throws Exception {
        boolean loggedIn = false;
        if (this.isLoginAttempt(request, response)) {
            loggedIn = this.executeLogin(request, response);
        }

        if (!loggedIn) {
            this.sendChallenge(request, response);
        }

        return loggedIn;
    }

    /**
     * 请求是否已经登录（携带token）
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        String authzHeader = WebUtils.toHttp(request).getHeader(AUTHZ_HEADER);
        return authzHeader != null;
    }

    /**
     * 执行登录方法(由自定义realm判断,吃掉异常返回false)
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response)
            throws Exception {
        String token = WebUtils.toHttp(request).getHeader(AUTHZ_HEADER);
        if (null == token) {
            String msg = "executeLogin method token must not be null";
            throw new IllegalStateException(msg);
        }
        //交给realm判断是否有权限,没权限返回false交给onAccessDenied
        JWTToken jwtToken = new JWTToken(token);
        try {
            this.getSubject(request, response).login(jwtToken);
            return true;
        } catch (AuthenticationException e) {
            return false;
        }
    }

    /**
     * 构建未授权的请求返回,filter层的异常不受exceptionAdvice控制,这里返回401,把返回的json丢到response中
     */
    @Override
    protected boolean sendChallenge(ServletRequest request, ServletResponse response) {
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        String contentType = "application/json;charset=" + CHARSET;
        httpResponse.setStatus((int) ResultCode.FORBIDDEN.getCode());
        httpResponse.setContentType(contentType);
        try {
            RestResult unauthentication = RestResult.failed(ResultCode.FORBIDDEN, ResultCode.FORBIDDEN.getMessage());
            PrintWriter printWriter = httpResponse.getWriter();
            printWriter.append(JSONObject.toJSONString(unauthentication));
//      byte[] data = unauthentication.toString().getBytes(CHARSET);
//      OutputStream outputStream = httpResponse.getOutputStream();
//      outputStream.write(data);
        } catch (IOException e) {
            log.error("sendChallenge error,can not resolve httpServletResponse");
        }

        return false;
    }

    /**
     * 请求前处理,处理跨域
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse
                .setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers",
                httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时,option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }
}