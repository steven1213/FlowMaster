package com.flowmaster.common.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * 日志工具类
 * 
 * @author FlowMaster Team
 * @version 1.0
 * @since 2024-01-01
 */
public class LogUtils {
    
    /**
     * 业务日志
     * 
     * @param operation 操作名称
     * @param userId 用户ID
     * @param details 详细信息
     */
    public static void businessLog(String operation, String userId, String details) {
        MDC.put("operation", operation);
        MDC.put("userId", userId);
        MDC.put("details", details);
        Logger logger = LoggerFactory.getLogger("BUSINESS");
        logger.info("业务操作: {}", operation);
        MDC.clear();
    }
    
    /**
     * 审计日志
     * 
     * @param action 操作动作
     * @param userId 用户ID
     * @param resource 资源
     * @param result 结果
     */
    public static void auditLog(String action, String userId, String resource, String result) {
        Logger auditLogger = LoggerFactory.getLogger("AUDIT");
        auditLogger.info("审计日志 - 操作: {}, 用户: {}, 资源: {}, 结果: {}", 
                        action, userId, resource, result);
    }
    
    /**
     * 性能日志
     * 
     * @param method 方法名
     * @param duration 耗时（毫秒）
     * @param params 参数
     */
    public static void performanceLog(String method, long duration, String params) {
        Logger performanceLogger = LoggerFactory.getLogger("PERFORMANCE");
        performanceLogger.info("性能日志 - 方法: {}, 耗时: {}ms, 参数: {}", 
                              method, duration, params);
    }
    
    /**
     * 安全日志
     * 
     * @param event 安全事件
     * @param userId 用户ID
     * @param ip IP地址
     * @param details 详细信息
     */
    public static void securityLog(String event, String userId, String ip, String details) {
        Logger securityLogger = LoggerFactory.getLogger("SECURITY");
        securityLogger.warn("安全事件 - 事件: {}, 用户: {}, IP: {}, 详情: {}", 
                           event, userId, ip, details);
    }
    
    /**
     * 错误日志
     * 
     * @param operation 操作名称
     * @param userId 用户ID
     * @param throwable 异常
     */
    public static void errorLog(String operation, String userId, Throwable throwable) {
        Logger errorLogger = LoggerFactory.getLogger("ERROR");
        errorLogger.error("操作失败 - 操作: {}, 用户: {}, 错误: {}", 
                         operation, userId, throwable.getMessage(), throwable);
    }
    
    /**
     * 系统日志
     * 
     * @param level 日志级别
     * @param message 日志消息
     * @param args 参数
     */
    public static void systemLog(String level, String message, Object... args) {
        Logger systemLogger = LoggerFactory.getLogger("SYSTEM");
        switch (level.toUpperCase()) {
            case "ERROR":
                systemLogger.error(message, args);
                break;
            case "WARN":
                systemLogger.warn(message, args);
                break;
            case "INFO":
                systemLogger.info(message, args);
                break;
            case "DEBUG":
                systemLogger.debug(message, args);
                break;
            default:
                systemLogger.info(message, args);
                break;
        }
    }
}
