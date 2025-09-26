package com.flowmaster.workflow.application.query;

import lombok.Data;

/**
 * 流程实例查询对象
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Data
public class ProcessInstanceQuery {

    /**
     * 流程定义Key
     */
    private String processDefinitionKey;

    /**
     * 流程实例名称
     */
    private String name;

    /**
     * 业务Key
     */
    private String businessKey;

    /**
     * 流程实例状态
     */
    private String status;

    /**
     * 启动人
     */
    private Long startedBy;

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
    private String sortBy = "startTime";

    /**
     * 排序方向
     */
    private String sortDirection = "desc";
}
