package com.flowmaster.workflow.application.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 流程定义DTO
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Data
public class ProcessDefinitionDTO {

    /**
     * 流程定义ID
     */
    private Long id;

    /**
     * 流程定义ID
     */
    private String processDefinitionId;

    /**
     * 流程定义名称
     */
    private String name;

    /**
     * 流程定义Key
     */
    private String key;

    /**
     * 流程定义版本
     */
    private Integer version;

    /**
     * 流程定义分类
     */
    private String category;

    /**
     * 流程定义描述
     */
    private String description;

    /**
     * 流程定义状态
     */
    private String status;

    /**
     * 流程定义状态描述
     */
    private String statusDescription;

    /**
     * 流程定义XML内容
     */
    private String xmlContent;

    /**
     * 流程定义图片内容
     */
    private String imageContent;

    /**
     * 流程定义部署时间
     */
    private LocalDateTime deploymentTime;

    /**
     * 流程定义部署人
     */
    private Long deployedBy;

    /**
     * 流程定义标签
     */
    private String tags;

    /**
     * 是否启用
     */
    private Boolean enabled;

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
