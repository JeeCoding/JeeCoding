package com.huzh.jeecoding.security.shiro;

import com.huzh.jeecoding.security.jwt.JWTFilter;
import com.huzh.jeecoding.security.shiro.cache.CustomCacheManager;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author huzh
 * @description: Shiro配置类
 * @date 2020/5/7 20:28
 */
@Configuration
public class ShiroConfig {

    @Value("${config.shiro-cache-expireTime}")
    private String shiroCacheExpireTime;

    @Autowired
    private RedisTemplate<String, Object> template;

    /**
     * 配置securityManager 管理subject（默认）,并把自定义realm交由manager
     */
    @Bean("securityManager")
    public DefaultSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(authRealm());
        //非web关闭sessionManager(官网有介绍)
        DefaultSubjectDAO defaultSubjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator storageEvaluator = new DefaultSessionStorageEvaluator();
        storageEvaluator.setSessionStorageEnabled(false);
        defaultSubjectDAO.setSessionStorageEvaluator(storageEvaluator);
        securityManager.setSubjectDAO(defaultSubjectDAO);
        securityManager.setCacheManager(new CustomCacheManager(template, shiroCacheExpireTime));
        return securityManager;
    }

    /**
     * 自定义realm，实现登录授权流程
     */
    @Bean
    public Realm authRealm() {
        return new MyShiroRealm();
    }

    /**
     * 拦截链
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setFilters(filterMap());
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setFilterChainDefinitionMap(definitionMap());
        return shiroFilterFactoryBean;
    }

    /**
     * 注入bean，此处应注意：如不在此注册，在filter中将无法正常注入bean
     */
    @Bean("jwtFilter")
    public JWTFilter jwtFilterBean() {
        return new JWTFilter();
    }

    /**
     * TODO 坑
     * 让filter仍然通过注入的方式让spring进行管理，同时又不会被spring默认注册.
     * 用于解决filter总是拦截login请求
     *
     * @param filter
     * @return
     */
    @Bean
    public FilterRegistrationBean registration(JWTFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        registration.setEnabled(false);
        return registration;
    }

    /**
     * 自定义拦截器，处理所有请求
     */
    private Map<String, Filter> filterMap() {
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("jwt", jwtFilterBean());
        return filterMap;
    }

    /**
     * url拦截规则
     */
    private Map<String, String> definitionMap() {
        Map<String, String> definitionMap = new HashMap<>();
        definitionMap.put("/login", "anon");
        definitionMap.put("/**", "jwt");
        return definitionMap;
    }

    /**
     * 开启注解
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        // 强制使用cglib代理，防止和aop冲突
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    /**
     * TODO 坑
     * 将生命周期处理器LifecycleBeanPostProcessor 设为静态.
     * 可以使用@value和@Autowired注入属性
     *
     * @return
     */
    @Bean
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    //开启shiro aop注解支持，不开启的话权限验证就会失效
    @Bean("authorizationAttributeSourceAdvisor")
    public AuthorizationAttributeSourceAdvisor advisor(DefaultSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}
