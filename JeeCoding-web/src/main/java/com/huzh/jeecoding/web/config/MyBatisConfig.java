package com.huzh.jeecoding.web.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author huzh
 * @description: 配置mybatis扫描包
 * @date 2020/5/7 14:51
 */
@Configuration
@EnableTransactionManagement
@MapperScan({"com.huzh.jeecoding.dao.mapper"})
public class MyBatisConfig {
}
