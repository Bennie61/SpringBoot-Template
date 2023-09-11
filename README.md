# SpringBoot-Template
基于SpringBoot的用户管理系统，实现了用户注册、登录、查询、管理等基础功能。该后端模版适用于后续同类项目的快速研发！
# 技术选型
## 后端选型
* Java 编程语言
* Spring（依赖注入框架，帮助管理 Java 对象，集成一些其他的内容）
* SpringMVC（web 框架，提供接口访问、restful接口等能力） 
+ SpringBoot（快速启动 / 快速集成项目。不用自己管理 spring 配置，不用自己整合各种框架）
* MyBatis Plus 数据访问框架
* jUnit 单元测试库
* MySQL 数据库

## 项目知识点整理
* Qes01: @GetMapping、@PostMapping 和 @RequestMapping的区别？
* Ans01: 
  * @RequestMapping是加在类上面的，所以@RequestMapping是具有类属性的；@GetMapping注解相当于标注该请求方法精确到了Get请求方法。
  * @GetMapping和 @PostMapping分别是 `@RequestMapping(method = RequestMethod.GET)`和 `@RequestMapping(method = RequestMethod.POST`的缩写。
* Qes02: `@RestControllerAdvice`的含义及用法？
  * Ans02：
    * 含义：`@RestControllerAdvice`是一个组合注解，由`@ControllerAdvice`、`@ResponseBody`组成。
    * 特点：
    * `@RestControllerAdvice`注解将作用在所有注解了`@RequestMapping`的控制器的方法上；
    * 当`@RestControllerAdvice`与`@ExceptionHandler`配合使用时，用于全局处理控制器里的异常。
    ```java
    @ControllerAdvice  
    public class GlobalExceptionHandler{
      //全局异常处理
      //应用到所有@RequestMapping注解的方法，在其抛出Exception异常时执行  
      //定义全局异常处理，value属性可以过滤拦截指定异常，此处拦截所有的Exception。
      @ExceptionHandler(Exception.class)
      public String handleException(Exception e) {    
          return "error";
      }  
    }
    ```
* Qes03: 如何实现全局异常处理器?
* 在项目中，设计了自定义的业务异常类 BusinessException，继承`RuntimeException`类，并新增了code 和 description字段， 并定义一个枚举类`ErrorCode`，描述了各种异常信息，例如，`PARAMS_ERROR(40000,"请求参数错误","")`。
  ```java
  import com.benny.usercenter.common.ErrorCode;
  public class BusinessException extends RuntimeException{
      private final int code;
      private final String description;
      // 构造方法
      public BusinessException(String message, int code, String description) {
          super(message); // 调用了RuntimeException的构造器
          this.code = code;
          this.description = description;
      }
      public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
      }
  }
  ```
* 采用了Spring Boot的`@RestControllerAdvice`注解来实现封装异常信息返回给前端，通过`@ControllerAdvice`注解可以将对于控制器的全局配置放在同一个位置。
* 编写了一个全局异常处理器，对自定义异常`BusinessException`和运行时异常`RuntimeException`进行捕获，
* @RestControllerAdvice注解将作用在所有注解了`@RequestMapping`的控制器的方法上，在控制器抛出对应Exception异常后执行相应操作。
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    // 针对 BusinessException类型的异常做一些处理操作
    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandler(BusinessException e){
        return ResultUtils.error(e.getCode(), e.getMessage(),e.getDescription());
    }
    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeExceptionHandler(RuntimeException e){
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage(),"");
    }
}
```
* 对应的Controller中注解了`@RequestMapping`的用户退出登录方法；
```
@PostMapping("/logout")
public BaseResponse<Integer> userLogout(HttpServletRequest request){
    if (request == null){
        // 抛出一个自定义 BusinessException异常，并传递ErrorCode和description信息。
        throw new BusinessException(ErrorCode.PARAMS_ERROR, "request为null");
    }
        int result = userService.userLogout(request);
        return ResultUtils.success(result);
    }
}
```

# 项目收获
1. 整体学习了前后端企业主流开发技术的应用
2. 了解到项目开发的完整流程
3. 学到一些实际的编码技巧，比如开发工具、快捷键、相关插件的使用技巧
4. 学到代码的优化技巧，比如抽象、封装、提高系统性能、节约资源的方法
5. 未完待续......
