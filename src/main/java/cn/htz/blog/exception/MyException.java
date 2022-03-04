package cn.htz.blog.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MyException extends RuntimeException {
    private Integer code = 500;

    public MyException() {
        super(ErrorEnum.UNKNOWN.getMsg());
    }

    public MyException(String msg, int code){
        super(msg);
        this.code = code;
    }

    public MyException(String msg){
        super(msg);
    }

    public MyException(ErrorEnum errorEnum){
        super(errorEnum.getMsg());
        this.code = errorEnum.getCode();
    }
}
