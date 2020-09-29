package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 本工程提供了操作ES的三种方式
 * （1）rest低级API：所有版本通用。
 * （2）rest高级API：有的版本可能不支持。
 * （3）springData ：实际项目中推荐使用此方式，但有的版本可能不支持。
 * 操作ES除以上依赖于ES的api之外，还可以直接使用post/delete等请求直接操作ES，从而不依赖任何第三方JAR包。
 * 具体例子见：test目录下的com.zyz包下
 *
 * @author yzzhang
 * @date 2020/9/14 15:41
 */
@SpringBootApplication
public class MyApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }

}