package com.example.fileshare.common;

import lombok.Data;

/**
 * LayUi 返回结果特殊处理
 *
 * @author vague 2021/10/7 18:40
 */
@Data
public class Result<T> {

    public static final String DATA_CANNOT_BE_EMPTY = "数据不能为空";

    private String message;

    private Integer code;

    private Boolean success;

    private Long count;


    private T data;

    public Result() {
        this.code = 0;
        this.message = "success";
        this.success = true;
        this.count = 0L;
    }

    public Result(int code, String msg) {
        this.code = code;
        this.message = msg;
    }

    public static <T> Result<T> success() {
        return new Result<>();
    }

    public static <T> Result<T> success(Integer code) {
        Result<T> r = new Result<>();
        r.setCode(code);
        return r;
    }

    public static <T> Result<T> success(T data) {
        Result<T> r = new Result<>();
        r.setData(data);
        return r;
    }

    public static <T> Result<T> success(String message) {
        Result<T> r = new Result<>();
        r.setMessage(message);
        return r;
    }

    public static <T> Result<T> success(String message, T data) {
        Result<T> r = new Result<>();
        r.setMessage(message);
        r.setData(data);
        return r;
    }

    public static <T> Result<T> success(Integer code, String message) {
        Result<T> r = new Result<>();
        r.setCode(code);
        r.setMessage(message);
        return r;
    }

    public static <T> Result<T> success(Integer code, T data) {
        Result<T> r = new Result<>();
        r.setCode(code);
        r.setData(data);
        return r;
    }


    public static <T> Result<T> failure() {
        return failure(500, "failure");
    }

    public static <T> Result<T> failure(String msg) {
        return failure(500, msg);
    }

    public static <T> Result<T> failure(T data) {
        return failure(500, "failure", data);
    }

    public static <T> Result<T> failure(int code, String msg) {
        return failure(code, msg, null);
    }

    public static <T> Result<T> failure(int code, String msg, T data) {
        Result<T> r = new Result<>();
        r.setCode(code);
        r.setMessage(msg);
        r.setData(data);
        r.setSuccess(false);
        return r;
    }


    public static <T> Result<T> success(T data, Long count) {
        Result<T> r = new Result<>();
        r.setCode(0);
        r.setData(data);
        r.setCount(count);
        return r;
    }


}
