package com.flowmaster.workflow.interfaces.rest;

import com.flowmaster.common.exception.BusinessException;
import com.flowmaster.common.exception.ParameterException;
import com.flowmaster.common.exception.SystemException;
import com.flowmaster.common.response.Result;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.stream.Collectors;

/**
 * 工作流引擎全局异常处理器
 *
 * @author FlowMaster Team
 * @since 1.0.0
 */
@RestControllerAdvice
@Slf4j
public class WorkflowGlobalExceptionHandler {

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Result<Void>> handleBusinessException(BusinessException e) {
        log.warn("业务异常: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.OK).body(Result.fail(e.getMessage()));
    }

    /**
     * 处理参数异常 (自定义ParameterException)
     */
    @ExceptionHandler(ParameterException.class)
    public ResponseEntity<Result<Void>> handleParameterException(ParameterException e) {
        log.warn("参数异常: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Result.fail(e.getMessage()));
    }

    /**
     * 处理JSR303参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Result<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        log.warn("参数校验失败: {}", errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Result.fail("参数校验失败: " + errorMessage));
    }

    /**
     * 处理ValidationException (如 @Valid 注解失败)
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Result<Void>> handleValidationException(ValidationException e) {
        log.warn("校验异常: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Result.fail("校验失败: " + e.getMessage()));
    }

    /**
     * 处理类型转换异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Result<Void>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.warn("参数类型不匹配: {} -> {}", e.getName(), e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "未知类型");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Result.fail("参数类型不匹配: " + e.getName()));
    }

    /**
     * 处理Flowable相关异常
     */
    @ExceptionHandler(org.flowable.common.engine.api.FlowableException.class)
    public ResponseEntity<Result<Void>> handleFlowableException(org.flowable.common.engine.api.FlowableException e) {
        log.warn("Flowable引擎异常: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Result.fail("工作流引擎异常: " + e.getMessage()));
    }

    @ExceptionHandler(org.flowable.common.engine.api.FlowableIllegalArgumentException.class)
    public ResponseEntity<Result<Void>> handleFlowableIllegalArgumentException(org.flowable.common.engine.api.FlowableIllegalArgumentException e) {
        log.warn("Flowable参数异常: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Result.fail("工作流参数异常: " + e.getMessage()));
    }

    @ExceptionHandler(org.flowable.common.engine.api.FlowableObjectNotFoundException.class)
    public ResponseEntity<Result<Void>> handleFlowableObjectNotFoundException(org.flowable.common.engine.api.FlowableObjectNotFoundException e) {
        log.warn("Flowable对象未找到: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Result.fail("工作流对象未找到: " + e.getMessage()));
    }

    @ExceptionHandler(org.flowable.common.engine.api.FlowableOptimisticLockingException.class)
    public ResponseEntity<Result<Void>> handleFlowableOptimisticLockingException(org.flowable.common.engine.api.FlowableOptimisticLockingException e) {
        log.warn("Flowable乐观锁异常: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Result.fail("工作流并发冲突，请重试"));
    }

    /**
     * 处理系统异常
     */
    @ExceptionHandler(SystemException.class)
    public ResponseEntity<Result<Void>> handleSystemException(SystemException e) {
        log.error("系统异常: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.fail("系统内部错误: " + e.getMessage()));
    }

    /**
     * 处理所有未捕获的异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<Void>> handleGenericException(Exception e) {
        log.error("未捕获的异常: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.fail("未知系统错误，请联系管理员"));
    }
}
