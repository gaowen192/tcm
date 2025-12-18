package com.ride.common;

import java.util.List;
import org.springframework.data.domain.Page;

/**
 * 通用API返回结果
 * @param <T> 数据类型（支持List、Page或单个对象）
 */
public class Result<T> {
    /** 响应码（200表示成功，其他为失败） */
    private Integer code;
    /** 响应消息 */
    private String message;
    /** 响应数据（泛型：支持普通List或Page） */
    private T data;

    // 私有构造，通过静态方法创建实例
    private Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功响应 - 带自定义消息
     * @param message 成功消息
     * @param data 数据
     * @param <T> 数据类型
     * @return Result对象
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(200, message, data);
    }
    
    // 成功返回（不分页，直接返回List）
    public static <T> Result<List<T>> success(List<T> data) {
        return new Result<>(200, "操作成功", data);
    }

    // 成功返回（分页，返回Page）
    public static <T> Result<Page<T>> success(Page<T> page) {
        return new Result<>(200, "操作成功", page);
    }

    // 成功返回（单个对象，如详情接口）
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }
    
    // 失败返回
    public static <T> Result<T> fail(Integer code, String message) {
        return new Result<>(code, message, null);
    }

    /**
     * 失败响应
     * @param message 失败信息
     * @param <T> 数据类型
     * @return Result对象
     */
    public static <T> Result<T> fail(String message) {
        return new Result<>(500, message, null);
    }

    /**
     * 错误响应
     * @param message 错误信息
     * @param <T> 数据类型
     * @return Result对象
     */
    public static <T> Result<T> error(String message) {
        return new Result<>(500, message, null);
    }

    /**
     * 400错误响应
     * @param message 错误信息
     * @param <T> 数据类型
     * @return Result对象
     */
    public static <T> Result<T> badRequest(String message) {
        return new Result<>(400, message, null);
    }

    /**
     * 401未授权响应
     * @param message 错误信息
     * @param <T> 数据类型
     * @return Result对象
     */
    public static <T> Result<T> unauthorized(String message) {
        return new Result<>(401, message, null);
    }

    /**
     * 403禁止访问响应
     * @param message 错误信息
     * @param <T> 数据类型
     * @return Result对象
     */
    public static <T> Result<T> forbidden(String message) {
        return new Result<>(403, message, null);
    }

    /**
     * 404未找到响应
     * @param message 错误信息
     * @param <T> 数据类型
     * @return Result对象
     */
    public static <T> Result<T> notFound(String message) {
        return new Result<>(404, message, null);
    }

    /**
     * 成功响应 - 仅返回消息，无数据
     * @param message 成功消息
     * @param <T> 数据类型
     * @return Result对象
     */
    public static <T> Result<T> success(String message) {
        return new Result<>(200, message, null);
    }

    // Getters and Setters
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}