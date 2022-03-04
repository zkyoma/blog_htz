package cn.htz.blog.exception;

import cn.htz.blog.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 统一异常处理器
 */
@RestControllerAdvice
@Slf4j
public class MyExceptionHandler {

    /**
     * 处理自定义异常
     * @param e
     * @return
     */
    @ExceptionHandler(MyException.class)
    public R handleMyException(MyException e){
        log.error(e.getMessage(), e);
        return R.error(e);
    }

    /**
     * 处理controller实体类数据校验异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R HandleValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        BindingResult bindingResult = e.getBindingResult();
        Map<String, String> errorMap = new HashMap<>();
        bindingResult.getFieldErrors().forEach(fieldError -> errorMap.put(fieldError.getField(), fieldError.getDefaultMessage()));
        return R.error(ErrorEnum.VALID_EXCEPTION).put("data", errorMap);
    }

    /**
     * ConstraintViolationException
     * Validator 参数校验异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public R ConstraintViolationExceptionHandler(ConstraintViolationException e) {
        log.error(e.getMessage(), e);
        List<String> messages = e.getConstraintViolations().stream()
                .map(constraintViolation -> constraintViolation.getMessage()).collect(Collectors.toList());
        return R.error(ErrorEnum.VALID_EXCEPTION).put("data", messages);
    }

    /**
     * 处理404异常
     * @param e
     * @return
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public R handlerNoFoundException(NoHandlerFoundException e){
        log.error(e.getMessage(), e);
        return R.error(ErrorEnum.PATH_NOT_FOUND);
    }

    /**
     * 插入或更新时，违反了主键或唯一约束。
     * @param e
     * @return
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public R handleDuplicateKeyException(DuplicateKeyException e){
        log.error(e.getMessage(), e);
        return R.error(ErrorEnum.DUPLICATE_KEY);
    }

    /**
     * 约束（外键，主键或唯一键）已被侵犯
     * @param e
     * @return
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public R handleSQLIntegrityConstraintViolationException(DataIntegrityViolationException e) {
        log.error(e.getMessage(), e);
        return R.error(ErrorEnum.Constraint_Exception);
    }

    /**
     * 请求方式不合法
     * @param e
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public R handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error(e.getMessage(), e);
        return R.error(ErrorEnum.BAD_REQUEST_METHOD);
    }

    // @ExceptionHandler(AuthorizationException.class)
    // public R handleAuthorizationException(AuthorizationException e){
    //     log.error(e.getMessage(),e);
    //     return Result.exception(ErrorEnum.NO_AUTH);
    // }

    @ExceptionHandler(Exception.class)
    public R handleException(Exception e){
        log.error(e.getMessage(), e);
        return R.error();
    }
}
