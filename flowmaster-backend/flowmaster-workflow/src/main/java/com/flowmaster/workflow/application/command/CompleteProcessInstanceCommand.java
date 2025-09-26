package com.flowmaster.workflow.application.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 完成流程实例命令
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Data
public class CompleteProcessInstanceCommand {

    /**
     * 流程实例ID
     */
    @NotBlank(message = "流程实例ID不能为空")
    @Size(max = 64, message = "流程实例ID长度不能超过64个字符")
    private String processInstanceId;

    /**
     * 结束原因
     */
    @Size(max = 500, message = "结束原因长度不能超过500个字符")
    private String endReason;

    /**
     * 操作人
     */
    @NotNull(message = "操作人不能为空")
    private Long operatorId;
}
