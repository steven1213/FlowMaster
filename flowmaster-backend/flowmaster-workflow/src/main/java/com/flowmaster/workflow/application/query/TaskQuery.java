package com.flowmaster.workflow.application.query;

import lombok.Data;

/**
 * 任务查询对象
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Data
public class TaskQuery {

    /**
     * 流程实例ID
     */
    private String processInstanceId;

    /**
     * 任务名称
     */
    private String name;

    /**
     * 任务定义Key
     */
    private String taskDefinitionKey;

    /**
     * 指派人
     */
    private Long assignee;

    /**
     * 任务状态
     */
    private String status;

    /**
     * 是否过期
     */
    private Boolean overdue;

    /**
     * 页码
     */
    private Integer page = 0;

    /**
     * 每页大小
     */
    private Integer size = 20;

    /**
     * 排序字段
     */
    private String sortBy = "createdAt";

    /**
     * 排序方向
     */
    private String sortDirection = "desc";
}
