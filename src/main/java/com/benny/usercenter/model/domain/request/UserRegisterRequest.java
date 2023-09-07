package com.benny.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description 用户注册请求体
 * @Author benny
 **/

/**
 * Note：一般情况下，在定义实体类时会使用 Serializable，序列化可以提高传输的效率。
 * 什么时候需要序列化？ 需要把对象的状态信息通过网络传输时，或者需要将对象的状态信息持久化时。
 * serialVersionUID: 唯一标识了一个可序列化的类。
 * 其用来标识继承serializable的类的版本，以验证加载的类和序列化的对象是否兼容，不同的类具有不同的serialVersionUID
 */

/**
 * Note:
 * @Data 注释是由 Lombok框架提供的一个注释标签，它可以自动生成JavaBean类（实体类）中的getter、setter方法、toString方法、equals方法和 hashCode方法。
 */
@Data
public class UserRegisterRequest implements Serializable {
    private static final long serialVersionUID = 3191241716373120793L;
    private String userAccount;
    private String userPassword;
    private String checkPassword;
    private String planetCode;

}
