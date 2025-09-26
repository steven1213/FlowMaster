package com.flowmaster.common.exception;

import com.flowmaster.common.response.ResultCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 参数异常
 * 
 * @author FlowMaster Team
 * @version 1.0
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ParameterException extends RuntimeException {
    
    private final Integer code;
    private final String message;
    private final String field;
    
    public ParameterException(String field, String message) {
        super(message);
        this.code = ResultCode.VALIDATION_ERROR.getCode();
        this.message = message;
        this.field = field;
    }
    
    public ParameterException(String message) {
        super(message);
        this.code = ResultCode.VALIDATION_ERROR.getCode();
        this.message = message;
        this.field = null;
    }
}
