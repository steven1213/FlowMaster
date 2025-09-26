package com.flowmaster.workflow.application.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Map;

/**
 * 启动流程实例命令
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Data
public class StartProcessInstanceCommand {

    /**
     * 流程定义Key
     */
    @NotBlank(message = "流程定义Key不能为空")
    @Size(max = 64, message = "流程定义Key长度不能超过64个字符")
    private String processDefinitionKey;

    /**
     * 流程实例名称
     */
    @Size(max = 255, message = "流程实例名称长度不能超过255个字符")
    private String name;

    /**
     * 业务Key
     */
    @Size(max = 255, message = "业务Key长度不能超过255个字符")
    private String businessKey;

    /**
     * 流程变量
     */
    private Map<String, Object> variables;

    /**
     * 启动人
     */
    @NotNull(message = "启动人不能为空")
    private Long startedBy;
}
