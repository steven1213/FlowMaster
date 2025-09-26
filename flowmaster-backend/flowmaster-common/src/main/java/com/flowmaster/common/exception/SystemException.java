package com.flowmaster.common.exception;

import com.flowmaster.common.response.ResultCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统异常
 * 
 * @author FlowMaster Team
 * @version 1.0
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SystemException extends RuntimeException {
    
    private final Integer code;
    private final String message;
    
    public SystemException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }
    
    public SystemException(ResultCode resultCode, Throwable cause) {
        super(resultCode.getMessage(), cause);
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }
    
    public SystemException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
    
    public SystemException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }
}
