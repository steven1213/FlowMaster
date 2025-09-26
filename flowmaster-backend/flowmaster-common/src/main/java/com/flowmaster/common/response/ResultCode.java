package com.flowmaster.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * 统一响应码枚举
 * 
 * @author FlowMaster Team
 * @version 1.0
 * @since 2024-01-01
 */
@Getter
@AllArgsConstructor
public enum ResultCode {
    
    // 成功
    SUCCESS(200, "操作成功"),
    
    // 客户端错误
    BAD_REQUEST(400, "请求参数错误"),
    PARAM_ERROR(400, "请求参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不允许"),
    CONFLICT(409, "资源冲突"),
    VALIDATION_ERROR(422, "参数验证失败"),
    
    // 服务器错误
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    SYSTEM_ERROR(500, "系统内部错误"),
    SERVICE_UNAVAILABLE(503, "服务不可用"),
    GATEWAY_TIMEOUT(504, "网关超时"),
    
    // 业务错误
    BUSINESS_ERROR(1000, "业务处理失败"),
    USER_NOT_FOUND(1001, "用户不存在"),
    USER_ALREADY_EXISTS(1002, "用户已存在"),
    INVALID_CREDENTIALS(1003, "用户名或密码错误"),
    USER_DISABLED(1004, "用户已被禁用"),
    
    // 工作流错误
    PROCESS_DEFINITION_NOT_FOUND(2001, "流程定义不存在"),
    PROCESS_INSTANCE_NOT_FOUND(2002, "流程实例不存在"),
    TASK_NOT_FOUND(2003, "任务不存在"),
    TASK_ALREADY_COMPLETED(2004, "任务已完成"),
    INVALID_TASK_ASSIGNEE(2005, "任务指派人无效"),
    
    // 审批错误
    APPROVAL_RULE_NOT_FOUND(3001, "审批规则不存在"),
    APPROVAL_REQUEST_NOT_FOUND(3002, "审批请求不存在"),
    APPROVAL_ALREADY_PROCESSED(3003, "审批已处理"),
    INVALID_APPROVAL_DECISION(3004, "审批决定无效"),
    
    // 权限错误
    PERMISSION_DENIED(4001, "权限不足"),
    ROLE_NOT_FOUND(4002, "角色不存在"),
    PERMISSION_NOT_FOUND(4003, "权限不存在"),
    INVALID_PERMISSION_ASSIGNMENT(4004, "权限分配无效"),
    
    // 系统错误
    DATABASE_ERROR(5001, "数据库操作失败"),
    CACHE_ERROR(5002, "缓存操作失败"),
    MESSAGE_QUEUE_ERROR(5003, "消息队列操作失败"),
    EXTERNAL_SERVICE_ERROR(5004, "外部服务调用失败"),
    FILE_UPLOAD_ERROR(5005, "文件上传失败"),
    FILE_DOWNLOAD_ERROR(5006, "文件下载失败");
    
    private final Integer code;
    private final String message;
}
