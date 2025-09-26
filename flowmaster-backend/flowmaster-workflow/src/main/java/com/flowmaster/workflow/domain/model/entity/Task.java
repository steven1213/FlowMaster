package com.flowmaster.workflow.domain.model.entity;

import com.flowmaster.common.domain.BaseDomainEntity;
import com.flowmaster.workflow.domain.model.valueobject.ProcessInstanceId;
import com.flowmaster.workflow.domain.model.valueobject.TaskId;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 任务实体
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Getter
@Setter
@Slf4j
public class Task extends BaseDomainEntity {

    /**
     * 任务ID
     */
    private TaskId taskId;

    /**
     * 流程实例ID
     */
    private ProcessInstanceId processInstanceId;

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
    private TaskStatus status;

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
     * 任务删除原因
     */
    private String deleteReason;

    /**
     * 任务状态枚举
     */
    public enum TaskStatus {
        CREATED("已创建"),
        ASSIGNED("已分配"),
        IN_PROGRESS("进行中"),
        COMPLETED("已完成"),
        CANCELLED("已取消"),
        SUSPENDED("已暂停");

        private final String description;

        TaskStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 创建任务
     *
     * @param taskId 任务ID
     * @param processInstanceId 流程实例ID
     * @param name 任务名称
     * @param description 任务描述
     * @param taskDefinitionKey 任务定义Key
     * @param assignee 任务指派人
     * @param priority 任务优先级
     * @param dueDate 任务到期时间
     * @param variables 任务变量
     * @return 任务
     */
    public static Task create(TaskId taskId, ProcessInstanceId processInstanceId, String name, 
                            String description, String taskDefinitionKey, Long assignee, 
                            Integer priority, LocalDateTime dueDate, Map<String, Object> variables) {
        Task task = new Task();
        task.taskId = taskId;
        task.processInstanceId = processInstanceId;
        task.name = name;
        task.description = description;
        task.taskDefinitionKey = taskDefinitionKey;
        task.assignee = assignee;
        task.priority = priority;
        task.dueDate = dueDate;
        task.variables = variables;
        task.status = TaskStatus.CREATED;
        
        return task;
    }

    /**
     * 分配任务
     *
     * @param assignee 指派人
     */
    public void assign(Long assignee) {
        this.assignee = assignee;
        this.status = TaskStatus.ASSIGNED;
    }

    /**
     * 开始任务
     */
    public void start() {
        this.status = TaskStatus.IN_PROGRESS;
    }

    /**
     * 完成任务
     *
     * @param completeReason 完成原因
     */
    public void complete(String completeReason) {
        this.status = TaskStatus.COMPLETED;
        this.completeTime = LocalDateTime.now();
        this.deleteReason = completeReason;
    }

    /**
     * 取消任务
     *
     * @param cancelReason 取消原因
     */
    public void cancel(String cancelReason) {
        this.status = TaskStatus.CANCELLED;
        this.deleteReason = cancelReason;
    }

    /**
     * 暂停任务
     *
     * @param suspendReason 暂停原因
     */
    public void suspend(String suspendReason) {
        this.status = TaskStatus.SUSPENDED;
        this.deleteReason = suspendReason;
    }

    /**
     * 删除任务
     *
     * @param deleteReason 删除原因
     */
    public void delete(String deleteReason) {
        setDeleted(true);
        this.deleteReason = deleteReason;
    }

    /**
     * 更新任务变量
     *
     * @param variables 新的任务变量
     */
    public void updateVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    /**
     * 检查任务是否已完成
     *
     * @return 是否已完成
     */
    public boolean isCompleted() {
        return status == TaskStatus.COMPLETED;
    }

    /**
     * 检查任务是否已取消
     *
     * @return 是否已取消
     */
    public boolean isCancelled() {
        return status == TaskStatus.CANCELLED;
    }

    /**
     * 检查任务是否已暂停
     *
     * @return 是否已暂停
     */
    public boolean isSuspended() {
        return status == TaskStatus.SUSPENDED;
    }

    /**
     * 检查任务是否已删除
     *
     * @return 是否已删除
     */
    public boolean isDeleted() {
        return getDeleted() != null && getDeleted();
    }

    /**
     * 检查任务是否已过期
     *
     * @return 是否已过期
     */
    public boolean isOverdue() {
        return dueDate != null && LocalDateTime.now().isAfter(dueDate) && !isCompleted();
    }
}
