package com.flowmaster.workflow.application.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 任务DTO
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Data
public class TaskDTO {

    /**
     * 任务ID
     */
    private Long id;

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 流程实例ID
     */
    private String processInstanceId;

    /**
     * 流程定义Key
     */
    private String processDefinitionKey;

    /**
     * 任务名称
     */
    private String name;

    /**
     * 任务描述
     */
    private String description;

    /**
     * 任务定义Key
     */
    private String taskDefinitionKey;

    /**
     * 任务指派人
     */
    private Long assignee;

    /**
     * 任务候选人
     */
    private String candidateUsers;

    /**
     * 任务候选组
     */
    private String candidateGroups;

    /**
     * 任务所有者
     */
    private Long owner;

    /**
     * 任务优先级
     */
    private Integer priority;

    /**
     * 任务状态
     */
    private String status;

    /**
     * 任务状态描述
     */
    private String statusDescription;

    /**
     * 任务到期时间
     */
    private LocalDateTime dueDate;

    /**
     * 任务完成时间
     */
    private LocalDateTime completeTime;

    /**
     * 任务变量
     */
    private Map<String, Object> variables;

    /**
     * 任务表单Key
     */
    private String formKey;

    /**
     * 删除原因
     */
    private String deleteReason;

    /**
     * 是否过期
     */
    private Boolean overdue;

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
