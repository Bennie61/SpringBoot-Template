package com.benny.usercenter.service;
import java.util.Date;

import com.benny.usercenter.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class UserServiceTest {
    @Resource
    private UserService userService;

    @Test
    public void testAddUser(){
        // 插件 GenerateAllSetter：
        // 鼠标放在user对象上，Alter+Enter，选择 Generate all setter with default value，
        // 针对已有的model实体对象的属性生成 set代码。
        User user = new User();
        user.setUsername("testyupi");
        user.setUserAccount("123");
        user.setAvatarUrl("png");
        user.setGender(0);
        user.setUserPassword("xxx");
        user.setPhone("123");
        user.setEmail("456");
        boolean res = userService.save(user);
        System.out.println(user.getId());
        assertTrue(res);
    }

    @Test
    void userRegister() {
        String userAccount = "yupi";
        String userPassword = "";
        String checkPassword = "123456";
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        //如果条件不满足，则断言会触发异常并终止程序的执行。
        Assertions.assertEquals(-1,result);
        userAccount  = "yu";
        result = userService.userRegister(userAccount, userPassword,checkPassword );
        Assertions.assertEquals(-1,result);
        userAccount = "yupi";
        userPassword = "123456";
        result = userService.userRegister(userAccount, userPassword,checkPassword );
        Assertions.assertEquals(-1,result);
        userAccount = "yu pi";
        userPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword,checkPassword );
        Assertions.assertEquals(-1,result);

        checkPassword = "123456789";
        result = userService.userRegister(userAccount, userPassword,checkPassword );
        Assertions.assertEquals( -1,result);
        userAccount = "testyupi";
        result = userService.userRegister(userAccount, userPassword,checkPassword );
        Assertions.assertEquals(-1,result);

        userAccount = "benny";
        userPassword = "12345678";
        checkPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword,checkPassword );
        Assertions.assertTrue(result > 0);


    }
}