package com.benny.usercenter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.benny.usercenter.mapper")
public class UserCenterApplication {
    // MyBatis-plus 框架会扫描mapper文件夹下的文件，把mapper文件下的增删改查注入到项目中
    public static void main(String[] args) {
        SpringApplication.run(UserCenterApplication.class, args);
    }

}
