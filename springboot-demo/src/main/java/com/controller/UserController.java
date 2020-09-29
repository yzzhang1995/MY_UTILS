package com.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bean.User;
import com.service.UserServiceByMybatisPlus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author yzzhang
 * @date 2020/9/16 21:27
 */
@RestController
public class UserController {
    @Autowired
    private UserServiceByMybatisPlus userService;

    @RequestMapping(value = "/userinfo1", method = RequestMethod.GET)
    public User getuserinfo1(HttpServletRequest request) {
        String id = request.getParameter("id");
        return userService.selectByDB1ID(id);
    }

    @RequestMapping(value = "/userinfo2", method = RequestMethod.POST)
    public JSONArray getuserinfo2(@RequestBody Map<String, Object> params) {
        String age = params.getOrDefault("age", "").toString();
        List<User> list = userService.selectByDB1Age(age);
        JSONArray array = JSONArray.parseArray(JSON.toJSONString(list));
        return array;
    }
}
