package com.flowmaster.auth.interfaces.rest;

import com.flowmaster.common.exception.BusinessException;
import com.flowmaster.common.exception.ParameterException;
import com.flowmaster.common.exception.SystemException;
import com.flowmaster.common.response.Result;
import com.flowmaster.common.response.ResultCode;
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
 * 认证服务全局异常处理器
 *
 * @author FlowMaster Team
 * @since 1.0.0
 */
@RestControllerAdvice
@Slf4j
public class AuthGlobalExceptionHandler {

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
     * 处理JWT相关异常
     */
    @ExceptionHandler(io.jsonwebtoken.ExpiredJwtException.class)
    public ResponseEntity<Result<Void>> handleExpiredJwtException(io.jsonwebtoken.ExpiredJwtException e) {
        log.warn("JWT令牌已过期: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Result.fail("令牌已过期"));
    }

    @ExceptionHandler(io.jsonwebtoken.MalformedJwtException.class)
    public ResponseEntity<Result<Void>> handleMalformedJwtException(io.jsonwebtoken.MalformedJwtException e) {
        log.warn("JWT令牌格式错误: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Result.fail("令牌格式错误"));
    }

    @ExceptionHandler(io.jsonwebtoken.SignatureException.class)
    public ResponseEntity<Result<Void>> handleSignatureException(io.jsonwebtoken.SignatureException e) {
        log.warn("JWT令牌签名验证失败: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Result.fail("令牌签名验证失败"));
    }

    @ExceptionHandler(io.jsonwebtoken.UnsupportedJwtException.class)
    public ResponseEntity<Result<Void>> handleUnsupportedJwtException(io.jsonwebtoken.UnsupportedJwtException e) {
        log.warn("不支持的JWT令牌: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Result.fail("不支持的令牌"));
    }

    @ExceptionHandler(io.jsonwebtoken.security.SecurityException.class)
    public ResponseEntity<Result<Void>> handleSecurityException(io.jsonwebtoken.security.SecurityException e) {
        log.warn("JWT安全异常: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Result.fail("令牌安全验证失败"));
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
