package com.benny.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.benny.usercenter.model.domain.User;
import com.benny.usercenter.service.UserService;
import com.benny.usercenter.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.benny.usercenter.contant.UserConstant.USER_LOGIN_STATE;

/**
* @author benny
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2023-08-18 11:18:12
*/
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{
    @Resource
    private UserMapper userMapper;

    /*
        盐值，混淆密码
     */
    private static final String SALT = "yupi";
    // Note：输入 prsf，快速打出常量的类型。
    /**
     * 用户登录态键
     */
    // public static final String USER_LOGIN_STATE = "userLoginState";

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 快速测试方法，鼠标放在放在方法上，按Alt+Enter，选择 Generate missed test methods，
        // 会在test相对应的目录下生成测试类
        // 1. 校验
        if(StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)){
            return -1;
        }
        if (userAccount.length() < 4){
            return -1;
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8 ){
            return -1;
        }

        //账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            return -1;
        }
        // 密码和校验密码相同
        if(!userPassword.equals(checkPassword)){
            return -1;
        }

        // 账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // https://blog.csdn.net/qq_26383975/article/details/119646390
        // QueryWrapper就是在使用 Mybatis-plus 中真实用到的一种技术，也叫作构造器，能简化sql的操作。
        // QueryWrapper其实可以理解成一个放查询条件的盒子，我们把查询条件放在里面，他就会自动按照对应的条件进行查询数据。
        // 构建一个查询的wrapper
        queryWrapper.eq("userAccount",userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if(count > 0){
            return -1;
        }
        // 2. 加密 Note：使用加密工具库 DigestUtils。
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 3. 插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);

        boolean saveResult = this.save(user);
        if (!saveResult){
            return -1;
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 校验
        if(StringUtils.isAnyBlank(userAccount, userPassword)){
            return null;
        }
        if (userAccount.length() < 4){
            return null;
        }
        if (userPassword.length() < 8){ //密码必须大于等于8位
            return null;
        }

        //账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            return null;
        }

        // 2. 加密 使用加密工具库 DigestUtils
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        // 查询用户是否存在,返回用户信息
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        if(user == null){ // 用户不存在
            /**
             * Note: 可以使用注解 @Slf4j来直接使用 log对象; @Slf4j是用来做日志输出的
             */
            log.info("user login failed, userAccount cannot match userPassword");
            return null;
        }

        // 3. 用户脱敏
        User safetyUser = getSafetyUser(user);

        //4. 记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);
        // 返回脱敏后的用户信息
        return safetyUser;
    }

    /**
     * 用户脱敏
     * @param originalUser 原始的 user
     * @return 脱敏后的 safetyUser
     */
    @Override
     public User getSafetyUser(User originalUser){
         User safetyUser = new User();
         /**
          * Note:
          * 使用插件，Generate all setter with default value，快速生成类的默认值
          */
         safetyUser.setId(originalUser.getId());
         safetyUser.setUsername(originalUser.getUsername());
         safetyUser.setUserAccount(originalUser.getUserAccount());
         safetyUser.setAvatarUrl(originalUser.getAvatarUrl());
         safetyUser.setGender(originalUser.getGender());
         safetyUser.setPhone(originalUser.getPhone());
         safetyUser.setEmail(originalUser.getEmail());
         safetyUser.setUserRole(originalUser.getUserRole());
         safetyUser.setUserStatus(0);
         safetyUser.setCreateTime(originalUser.getCreateTime());
         return safetyUser;
     }
}




