package com.example.fileshare.handler;

import com.example.fileshare.common.Result;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;


/**
 * @author vague 2021/8/5 15:32
 */
@ControllerAdvice
public class ResponseResultHandler implements ResponseBodyAdvice<Object> {


    /**
     * 判断是否要执行 beforeBodyWrite 方法，true为执行
     *
     * @param methodParameter /
     * @param clazz /
     * @return /
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> clazz) {
        return true;
    }

    /**
     * 对返回值做包装处理
     *
     * @param body /
     * @param methodParameter /
     * @param mediaType /
     * @param clazz /
     * @param serverHttpRequest /
     * @param serverHttpResponse /
     * @return /
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> clazz, ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {
        if (body instanceof Result) {
            return body;
        }
        return Result.success("success", body);
    }

}
