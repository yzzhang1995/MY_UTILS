package com.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bean.User;
import com.mapper.CategoryMapper;
import com.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Wrapper;
import java.util.List;

/**
 * 基于MybatisPlus实现动态切库
 * 动态切库方法
 * （1）引入dynamic-datasource-spring-boot-starter坐标。
 * （2）在application.yml中配置数据源。
 * （3）使用@DS()注解实现动态切库,方法上的注解优先于类上的注解
 *
 * @author yzzhang
 * @date 2020/9/8 22:21
 */
@Service
@DS("master")
public class UserServiceByMybatisPlus {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private UserMapper userMapper;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private CategoryMapper categoryMapper;

    public List<User> selectByDB1() {
        return userMapper.selectList(null);
    }

    public User selectByDB1ID(String id) {
        return userMapper.selectById(id);
    }

    public List<User> selectByDB1Age(String age) {
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.eq("age", age);
        return userMapper.selectList(queryWrapper);
    }

    @DS("slave_1")
    public List selectByDb2() {
        return categoryMapper.selectList(null);
    }
}