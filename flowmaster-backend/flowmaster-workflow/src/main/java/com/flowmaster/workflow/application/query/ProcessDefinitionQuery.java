package com.flowmaster.workflow.application.query;

import lombok.Data;

import java.util.List;

/**
 * 流程定义查询对象
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Data
public class ProcessDefinitionQuery {

    /**
     * 流程定义Key
     */
    private String key;

    /**
     * 流程定义名称
     */
    private String name;

    /**
     * 流程定义分类
     */
    private String category;

    /**
     * 流程定义状态
     */
    private String status;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 标签
     */
    private String tags;

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
