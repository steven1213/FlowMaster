package com.flowmaster.common.exception;

import com.flowmaster.common.response.ResultCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务异常基类
 * 
 * @author FlowMaster Team
 * @version 1.0
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessException extends RuntimeException {
    
    private final Integer code;
    private final String message;
    private final Object[] args;
    
    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.args = null;
    }
    
    public BusinessException(ResultCode resultCode, Object... args) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.args = args;
    }
    
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
        this.args = null;
    }
    
    public BusinessException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
        this.args = null;
    }
}
