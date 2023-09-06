package com.benny.usercenter.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户表
  Note：使用 @TableName(value ="user") 声明该实体类 User映射的表名为 user。
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * Note: 当 Java对象需要在网络上传输或者需要持久化存储到文件中时，需要使用 Java序列化。
     * Java序列化是将对象转换成字节序列的过程，反序列化就是将字节序列转换为目标对象的过程。
     * 使用方法：让类实现 Serializable接口，标注该类对象是可被序列化的。
     */
    /**
     * Note: 使用 @TableId 来表示该属性为表中的主键
     * type = IdType.AUTO 表示该列的值使用自动增长列生成
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 状态0-正常
     */
    private Integer userStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 用户角色 0-普通用户 1-管理员
     */
    private Integer userRole;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /*
        Note: private static final long serialVersionUID = 1L;
        固定的 1L（一般这么做。可以确保代码一致时反序列化成功）
     */
}