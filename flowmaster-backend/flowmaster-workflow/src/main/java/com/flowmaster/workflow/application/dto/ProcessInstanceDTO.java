package com.flowmaster.workflow.application.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 流程实例DTO
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Data
public class ProcessInstanceDTO {

    /**
     * 流程实例ID
     */
    private Long id;

    /**
     * 流程实例ID
     */
    private String processInstanceId;

    /**
     * 流程定义ID
     */
    private String processDefinitionId;

    /**
     * 流程定义Key
     */
    private String processDefinitionKey;

    /**
     * 流程实例名称
     */
    private String name;

    /**
     * 流程实例状态
     */
    private String status;

    /**
     * 流程实例状态描述
     */
    private String statusDescription;

    /**
     * 流程实例启动人
     */
    private Long startedBy;

    /**
     * 流程实例启动时间
     */
    private LocalDateTime startTime;

    /**
     * 流程实例结束时间
     */
    private LocalDateTime endTime;

    /**
     * 流程实例业务Key
     */
    private String businessKey;

    /**
     * 流程实例变量
     */
    private Map<String, Object> variables;

    /**
     * 流程实例父实例ID
     */
    private String parentInstanceId;

    /**
     * 删除原因
     */
    private String deleteReason;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 创建人
     */
    private Long createdBy;

    /**
     * 更新人
     */
    private Long updatedBy;
}
