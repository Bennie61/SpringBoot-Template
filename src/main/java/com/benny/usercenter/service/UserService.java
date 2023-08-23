package com.benny.usercenter.service;

import com.benny.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author benny
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2023-08-18 11:18:12
*/
public interface UserService extends IService<User> {
    /**
     * 用户注册
     * @param userAccount  账户
     * @param userPassword 密码
     * @param checkPassword 确认密码
     * @return 失败返回-1
     */
    long userRegister(String userAccount,String userPassword,String checkPassword);
}
