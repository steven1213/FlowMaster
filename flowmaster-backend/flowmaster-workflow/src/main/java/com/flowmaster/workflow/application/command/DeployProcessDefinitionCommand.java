package com.flowmaster.workflow.application.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.InputStream;

/**
 * 部署流程定义命令
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Data
public class DeployProcessDefinitionCommand {

    /**
     * 流程定义名称
     */
    @NotBlank(message = "流程定义名称不能为空")
    @Size(max = 255, message = "流程定义名称长度不能超过255个字符")
    private String name;

    /**
     * 流程定义Key
     */
    @NotBlank(message = "流程定义Key不能为空")
    @Size(max = 64, message = "流程定义Key长度不能超过64个字符")
    private String key;

    /**
     * 流程定义分类
     */
    @Size(max = 64, message = "流程定义分类长度不能超过64个字符")
    private String category;

    /**
     * 流程定义描述
     */
    @Size(max = 1000, message = "流程定义描述长度不能超过1000个字符")
    private String description;

    /**
     * 流程定义XML内容
     */
    @NotBlank(message = "流程定义XML内容不能为空")
    private String xmlContent;

    /**
     * 流程定义图片内容
     */
    private String imageContent;

    /**
     * 流程定义标签
     */
    @Size(max = 500, message = "流程定义标签长度不能超过500个字符")
    private String tags;

    /**
     * 部署人
     */
    @NotNull(message = "部署人不能为空")
    private Long deployedBy;
}
