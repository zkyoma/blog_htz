package cn.htz.blog.interceptor;

import cn.htz.blog.exception.ErrorEnum;
import cn.htz.blog.exception.MyException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getSession().getAttribute("user") == null) {
            throw new MyException(ErrorEnum.NO_AUTH);
        }
        return true;
    }
}
