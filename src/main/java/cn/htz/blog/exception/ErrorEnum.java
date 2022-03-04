package cn.htz.blog.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * error类型枚举类
 */
@Getter
@AllArgsConstructor
public enum ErrorEnum {

    // 系统错误
    UNKNOWN(500,"系统内部错误，请联系管理员"),
    PATH_NOT_FOUND(404,"路径不存在，请检查路径"),
    NO_AUTH(403,"没有权限，请联系管理员"),
    BAD_REQUEST_METHOD(405, "请求方法对指定的资源不适用"),
    DUPLICATE_KEY(501,"数据库中已存在该记录"),
    Constraint_Exception(502, "数据库存在约束（外键、非空、唯一等），不能执行该操作"),
    VALID_EXCEPTION(400, "数据校验出现错误"),
    // TOKEN_GENERATOR_ERROR(502,"token生成失败"),
    // NO_UUID(503,"uuid为空"),
    // SQL_ILLEGAL(504,"sql非法"),

    //用户权限错误
    // INVALID_TOKEN(1001,"token不合法"),

    //登录模块错误
    LOGIN_FAIL(10001,"登录失败"),
    CAPTCHA_WRONG(10002,"验证码错误"),
    USERNAME_OR_PASSWORD_WRONG(10003,"用户名或密码错误"),

    //OOS上传错误
    OOS_UPLOAD_ERROR(10050,"OOS上传文件错误");

    private Integer code;
    private String msg;
}
