package com.flowmaster.workflow.application.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 分配任务命令
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Data
public class AssignTaskCommand {

    /**
     * 任务ID
     */
    @NotBlank(message = "任务ID不能为空")
    @Size(max = 64, message = "任务ID长度不能超过64个字符")
    private String taskId;

    /**
     * 指派人
     */
    @NotNull(message = "指派人不能为空")
    private Long assignee;

    /**
     * 操作人
     */
    @NotNull(message = "操作人不能为空")
    private Long operatorId;
}
