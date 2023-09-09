package com.benny.usercenter.exception;

/**
 * @Description BusinessException:自定义的异常类
 * @Author benny
 **/

import com.benny.usercenter.common.ErrorCode;

/** Note：
 * 定义了业务异常类 BusinessException，
 * 1. 相对于 java 的异常类，支持更多字段；
 * 2. 自定义构造函数，更灵活快捷设置新字段。
 * 封装了 RuntimeException，给原本的 RuntimeException 扩充了两个新字段，
 * 并且提供两个构造函数，使之支持传入 ErrorCode。
 */
public class BusinessException extends RuntimeException{
    // RuntimeException 中已经包含了 message，故无需新增该字段。
    private final int code;
    private final String description;

    public BusinessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    public BusinessException(ErrorCode errorCode,String description) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
