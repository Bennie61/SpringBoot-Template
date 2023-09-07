package com.benny.usercenter.service;

import com.benny.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * @author benny
 * @description 针对表【user(用户表)】的数据库操作 Service
 * @createDate 2023-08-18 11:18:12
 * Note: MyBatis-Plus通用的 IService接口，其中包含很多操作数据库的方法；
 * 之后可以在controller层使用userService.xxx来操作数据库。
 */
public interface UserService extends IService<User> {
    // Note: 鼠标放在接口名xxx上，按ALT+Enter，选择 implement method ‘xxx’,
    // 快速在对应目录下生成接口的具体实现代码。

    /**
     * 用户注册接口
     * @param userAccount   账户
     * @param userPassword  密码
     * @param checkPassword 确认密码
     * @param planetCode 星球编号
     * @return 失败返回-1
     */
    long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode);
    /**
     * 用户登录接口
     * @param userAccount 账户
     * @param userPassword 密码
     * @param request 用来处理用户的登录态
     * @return  脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户脱敏接口
     * @param originalUser 原始的 user
     * @return 脱敏后的 safetyUser
     */
    User getSafetyUser(User originalUser);

    /**
     * 用户注销接口
     * @param request
     * @return 返回 1 表示注销成功
     */
    int userLogout(HttpServletRequest request);
}
