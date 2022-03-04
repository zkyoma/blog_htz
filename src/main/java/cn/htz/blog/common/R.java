package cn.htz.blog.common;

import cn.htz.blog.exception.ErrorEnum;
import cn.htz.blog.exception.MyException;

import java.util.HashMap;

public class R extends HashMap<String, Object> {
    public R() {
        put("code", 200);
        put("msg", "success");
    }

    public static R ok() {
        return new R();
    }

    public static R error() {
        return error(ErrorEnum.UNKNOWN);
    }

    public static R error(String msg) {
        return new R().put("msg", msg).put("code", ErrorEnum.UNKNOWN.getCode());
    }

    public static R error(ErrorEnum errorEnum) {
        R r = new R();
        r.put("code", errorEnum.getCode());
        r.put("msg", errorEnum.getMsg());
        return r;
    }

    public static R error(MyException myException) {
        R r = new R();
        r.put("code", myException.getCode());
        r.put("msg", myException.getMessage());
        return r;
    }


    public R put(String key, Object data) {
        super.put(key, data);
        return this;
    }

    public static R ok(String msg) {
        R r = new R();
        r.put("msg", msg);
        return r;
    }

    public Integer getCode() {
        return  (Integer)this.get("code");
    }
}
