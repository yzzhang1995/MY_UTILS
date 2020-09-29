package com.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 基于SpringJDBC实现动态切库
 * 动态切库方法
 * （1）引入dynamic-datasource-spring-boot-starter坐标。
 * （2）在application.yml中配置数据源。
 * （3）使用@DS()注解实现动态切库,方法上的注解优先于类上的注解
 *
 * @author yzzhang
 * @date 2020/9/8 22:21
 */
@Service
@DS("slave")
public class UserServiceBySpringJdbc {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List selectByDB1() {
        return jdbcTemplate.queryForList("select * from user");
    }

    @DS("slave_1")
    public List selectByDb2() {
        return jdbcTemplate.queryForList("select * from help_category");
    }
}