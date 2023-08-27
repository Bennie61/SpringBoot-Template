package com.benny.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.benny.usercenter.model.domain.User;
import com.benny.usercenter.model.domain.request.UserLoginRequest;
import com.benny.usercenter.model.domain.request.UserRegisterRequest;
import com.benny.usercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.benny.usercenter.contant.UserConstant.ADMIN_ROLE;
import static com.benny.usercenter.contant.UserConstant.USER_LOGIN_STATE;

/**
 * @Date 2023/8/26 12:48
 * @Description ToDo
 **/

/** Note:
 * @RestController 是 @controller和 @ResponseBody 的结合
 * @Controller 将当前修饰的类注入SpringBoot IOC容器，使得从该类所在的项目跑起来的过程中，这个类就被实例化。
 * @ResponseBody 它的作用简短截说就是指该类中所有的 API 接口返回的数据，甭管你对应的方法返回Map或是其他Object，
 * 它会以Json字符串的形式返回给客户端
 * 原文链接：https://blog.csdn.net/qq_41192218/article/details/113354167
 *
 * @RequestMapping 注解是一个用来处理请求地址映射的注解，可用于映射一个请求或一个方法，可以用在类或方法上。
 */

/**
 * 用户接口
 * @author benny
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/register")
    /**
     * Note:
     * @RequestBody 主要用来接收前端传递给后端的json字符串中的数据的(请求体中的数据的)
     */
    public Long userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        /**
         * Note: 插件 Auto filling Java call arguments
         * 使用 Alt+Enter 组合键，调出 "Auto fill call parameters" ，
         * 自动使用该函数定义的参数名填充参数列表。
         */
        if (userRegisterRequest == null){
            return null;
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        /**
         * Note:
         *  StringUtils.isAnyBlank(v1,v2,v3...),任意一个参数为空的话，返回 true。
         */
        if (StringUtils.isAnyBlank(userAccount,userPassword,checkPassword)){
            return null;
        }
        long id = userService.userRegister(userAccount, userPassword, checkPassword);
        return id;
    }

    @PostMapping("/login")
    public User userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        if (userLoginRequest == null){
            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount,userPassword)){
            return null;
        }
        return userService.userLogin(userAccount, userPassword, request);
    }

    @GetMapping("/search")
    public List<User> searchUsers(String username,HttpServletRequest request){
        // 仅管理员可查询
        if (!isAdmin(request)){
            return new ArrayList<>();
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)){
            queryWrapper.like("username",username);
        }
        List<User> userList = userService.list(queryWrapper);
        //Note: Java 8 的知识
        return userList.stream().map(user ->{
            return userService.getSafetyUser(user);
        }).collect(Collectors.toList());
    }

    @PostMapping("/delete")
    public boolean deleteUser(@RequestBody long id, HttpServletRequest request){
        // Note：从前端传来的参数就用 @RequestBody
        // 仅管理员可删除
        if (!isAdmin(request)){
            return false;
        }
        if (id <= 0){
            return false;
        }
        return userService.removeById(id);
    }

    /**
     * 是否为管理员
     * @param request
     * @return boolean
     */
    private boolean isAdmin(HttpServletRequest request){
        // 鉴权，仅管理员可操作
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        if (user == null || user.getUserRole()!= ADMIN_ROLE){
            return false;
        }
        return true;
    }

}
