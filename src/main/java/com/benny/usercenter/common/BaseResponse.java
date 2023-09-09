package com.benny.usercenter.common;
import lombok.Data;
import java.io.Serializable;

/**
 * @Date 2023/9/7 9:13
 * @Description 通用的返回类，这里利用了泛型，将所有接口的返回值都用通用返回类来替代
 * @Author benny
 **/

@Data
public class BaseResponse<T> implements Serializable {
    // code = 0 表示成功
    private int code;
    private T data;
    private String message;
    private String description;

    /** Note:
     * 快捷键：Alt + Insert 快速生成构造器、Getter、Setter等。
     */
    public BaseResponse(int code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    public BaseResponse(int code, T data, String message) {
        this(code, data, message,"");
    }
    public BaseResponse(int code, T data) {
        this(code, data,"","");
    }
    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage(),errorCode.getDescription());
    }

}
