package com;

import com.bean.HelpCategory;
import com.bean.User;
import com.service.UserServiceByMybatisPlus;
import com.service.UserServiceBySpringJdbc;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author yzzhang
 * @date 2020/9/8 22:33
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyApplication.class)
public class TestJdbc {
    @Autowired
  private UserServiceBySpringJdbc userServiceTemplate;
    @Autowired
    private UserServiceByMybatisPlus userServiceMybatis;
    @Test
    public void testTemplateDB1(){
        List<User> list = userServiceTemplate.selectByDB1();
        System.out.println(list);
    }

    @Test
    public void testTemplateDB2(){
        List<User> list = userServiceTemplate.selectByDb2();
        System.out.println(list);
    }

    @Test
    public void testMybatisDB1(){
        List<User> list = userServiceMybatis.selectByDB1();
        System.out.println(list);
    }

    @Test
    public void testMybatisDB2(){
        List<HelpCategory> list = userServiceMybatis.selectByDb2();
        System.out.println(list);
    }
}
