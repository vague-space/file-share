package com.example.fileshare.handler;

import com.example.fileshare.common.BizException;
import com.example.fileshare.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理类
 *
 * @author vague 2021/8/5 16:10
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    /**
     * 处理自定义的业务异常
     *
     * @param req /
     * @param e /
     * @return /
     */
    @ExceptionHandler(value = BizException.class)
    @ResponseBody
    public Result<String> bizExceptionHandler(HttpServletRequest req, BizException e) {
        return Result.failure(e.getErrorCode(), e.getErrorMsg());
    }


    /**
     * 调用方在以post json方式请求服务时，没有对参数进行json序列化
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        return Result.failure(502, Result.DATA_CANNOT_BE_EMPTY);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public Result<String> handleDuplicateKeyException(DuplicateKeyException e) {
        logger.error(e.getMessage(), e);
        return Result.failure("数据库中已存在该记录");
    }

    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception e) {
        logger.error(e.getMessage(), e);
        return Result.failure("服务异常，请联系管理员！");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<String> validationError(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        if (null != fieldError) {
            String errorMessage = fieldError.getField() + fieldError.getDefaultMessage();
            logger.error(errorMessage);
            return Result.failure(fieldError.getField() + fieldError.getDefaultMessage());
        }
        return Result.failure(ex.getMessage());
    }

    /**
     * NoHandlerFoundException 异常捕获处理
     * 请求的接口不存在
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public Result<String> noHandlerFoundExceptionHandler(HttpServletRequest request, NoHandlerFoundException e) {
        return Result.failure(404, "页面不存在");
    }


}
