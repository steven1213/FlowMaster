package com.flowmaster.workflow.application.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Map;

/**
 * 完成任务命令
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Data
public class CompleteTaskCommand {

    /**
     * 任务ID
     */
    @NotBlank(message = "任务ID不能为空")
    @Size(max = 64, message = "任务ID长度不能超过64个字符")
    private String taskId;

    /**
     * 任务变量
     */
    private Map<String, Object> variables;

    /**
     * 完成原因
     */
    @Size(max = 500, message = "完成原因长度不能超过500个字符")
    private String completeReason;

    /**
     * 操作人
     */
    @NotNull(message = "操作人不能为空")
    private Long operatorId;
}
