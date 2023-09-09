package com.benny.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.benny.usercenter.common.BaseResponse;
import com.benny.usercenter.common.ErrorCode;
import com.benny.usercenter.common.ResultUtils;
import com.benny.usercenter.exception.BusinessException;
import com.benny.usercenter.model.domain.User;
import com.benny.usercenter.model.domain.request.UserLoginRequest;
import com.benny.usercenter.model.domain.request.UserRegisterRequest;
import com.benny.usercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.benny.usercenter.contant.UserConstant.ADMIN_ROLE;
import static com.benny.usercenter.contant.UserConstant.USER_LOGIN_STATE;

/** Note:
 * @RestController 是 @controller和 @ResponseBody 的结合
 * @Controller 将当前修饰的类注入SpringBoot IOC容器，使得从该类所在的项目跑起来的过程中，这个类就被实例化。
 * @ResponseBody 它的作用简短说就是指该类中所有的 API 接口返回的数据，甭管你对应的方法返回Map或是其他Object，
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
    /**
     * Note: 由于 UserService 继承了 IService<User>，
     * 故其实例 userService中既包含自定义方法也包含 MyBatisPlus操作数据库的方法。
     */
    @Resource
    private UserService userService;

    @PostMapping("/register")
    /**
     * Note:
     * @RequestBody 主要用来接收前端传递给后端的json字符串中的数据的(请求体中的数据的)
     */
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        /**
         * Note: 插件 Auto filling Java call arguments
         * 快捷键：Alt+Enter 组合键，调出 "Auto fill call parameters" ，
         * 自动使用该函数定义的参数名填充函数的参数列表。
         */
        if (userRegisterRequest == null){
//            return ResultUtils.error(ErrorCode.PARAMS_RREOR);
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数值为 null");
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();
        /**
         * Note:
         *  StringUtils.isAnyBlank(v1,v2,v3...),任意一个参数为空的话，返回 true。
         */
        if (StringUtils.isAnyBlank(userAccount,userPassword,checkPassword,planetCode)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数值为 null");
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
//        return new BaseResponse<>(0, result, "ok");
//        System.out.println("---------result:");
//        System.out.println(result);
        return ResultUtils.success(result);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        if (userLoginRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数值为 null");
//            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount,userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号或密码为空");
        }
        User user = userService.userLogin(userAccount, userPassword, request);
//        return new BaseResponse<>(0, user, "ok");
        return ResultUtils.success(user);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request){
        if (request == null){
//            return null;
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "request为null");
        }
        int result = userService.userLogout(request);
        return ResultUtils.success(result);
    }


    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User)userObj;
        if(currentUser == null){
//            return null;
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        long userId = currentUser.getId(); // 获取session中保存的用户id
        // TODO 校验用户是否合法
        User user = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(user);
        return ResultUtils.success(safetyUser);
    }

    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest request){
        // 仅管理员可查询
        if (!isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTH, "无权限");
//            return new ArrayList<>();
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)){
            queryWrapper.like("username",username);
        }
        List<User> userList = userService.list(queryWrapper);
        //Note: Java 8 的知识
        List<User> list = userList.stream().map(user -> {
            return userService.getSafetyUser(user);
        }).collect(Collectors.toList());
        return ResultUtils.success(list);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id, HttpServletRequest request){
        // Note：从前端传来的参数就用 @RequestBody
        // 仅管理员可删除
        if (!isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTH, "无权限");
        }
        if (id <= 0){
//            return null;
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = userService.removeById(id);
        return ResultUtils.success(b);
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
