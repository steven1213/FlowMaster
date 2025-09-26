package com.flowmaster.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一响应结果封装
 * 
 * @param <T> 数据类型
 * @author FlowMaster Team
 * @version 1.0
 * @since 2024-01-01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    
    /**
     * 响应码
     */
    private Integer code;
    
    /**
     * 响应消息
     */
    private String message;
    
    /**
     * 响应数据
     */
    private T data;
    
    /**
     * 时间戳
     */
    private Long timestamp;
    
    /**
     * 请求追踪ID
     */
    private String traceId;
    
    /**
     * 成功响应
     * 
     * @param data 响应数据
     * @param <T> 数据类型
     * @return 成功响应结果
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), 
                           ResultCode.SUCCESS.getMessage(), 
                           data, 
                           System.currentTimeMillis(),
                           TraceContext.getTraceId());
    }
    
    /**
     * 成功响应（无数据）
     * 
     * @param <T> 数据类型
     * @return 成功响应结果
     */
    public static <T> Result<T> success() {
        return success(null);
    }
    
    /**
     * 失败响应
     * 
     * @param resultCode 结果码
     * @param <T> 数据类型
     * @return 失败响应结果
     */
    public static <T> Result<T> error(ResultCode resultCode) {
        return new Result<>(resultCode.getCode(), 
                           resultCode.getMessage(), 
                           null, 
                           System.currentTimeMillis(),
                           TraceContext.getTraceId());
    }
    
    /**
     * 失败响应（自定义消息）
     * 
     * @param resultCode 结果码
     * @param message 自定义消息
     * @param <T> 数据类型
     * @return 失败响应结果
     */
    public static <T> Result<T> error(ResultCode resultCode, String message) {
        return new Result<>(resultCode.getCode(), 
                           message, 
                           null, 
                           System.currentTimeMillis(),
                           TraceContext.getTraceId());
    }
    
    /**
     * 失败响应（自定义码和消息）
     * 
     * @param code 自定义码
     * @param message 自定义消息
     * @param <T> 数据类型
     * @return 失败响应结果
     */
    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, 
                           message, 
                           null, 
                           System.currentTimeMillis(),
                           TraceContext.getTraceId());
    }
    
    /**
     * 失败响应（自定义消息）
     * 
     * @param message 自定义消息
     * @param <T> 数据类型
     * @return 失败响应结果
     */
    public static <T> Result<T> fail(String message) {
        return new Result<>(ResultCode.BUSINESS_ERROR.getCode(), 
                           message, 
                           null, 
                           System.currentTimeMillis(),
                           TraceContext.getTraceId());
    }
    
    /**
     * 判断是否成功
     * 
     * @return 是否成功
     */
    public boolean isSuccess() {
        return ResultCode.SUCCESS.getCode().equals(this.code);
    }
}
