package com.flowmaster.workflow.infrastructure.persistence.entity;

import com.flowmaster.common.infrastructure.persistence.BaseJpaEntity;
import com.flowmaster.workflow.domain.model.entity.Task;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 任务JPA实体
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Entity
@Table(name = "wf_task", indexes = {
    @Index(name = "idx_task_instance_id", columnList = "process_instance_id"),
    @Index(name = "idx_task_assignee", columnList = "assignee"),
    @Index(name = "idx_task_status", columnList = "status"),
    @Index(name = "idx_task_due_date", columnList = "due_date"),
    @Index(name = "idx_task_deleted", columnList = "deleted")
})
@Getter
@Setter
@Slf4j
@SQLDelete(sql = "UPDATE wf_task SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class TaskEntity extends BaseJpaEntity {

    /**
     * 任务ID
     */
    @Column(name = "task_id", nullable = false, unique = true, length = 64)
    private String taskId;

    /**
     * 流程实例ID
     */
    @Column(name = "process_instance_id", nullable = false, length = 64)
    private String processInstanceId;

    /**
     * 任务名称
     */
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    /**
     * 任务描述
     */
    @Column(name = "description", length = 1000)
    private String description;

    /**
     * 任务定义Key
     */
    @Column(name = "task_definition_key", length = 64)
    private String taskDefinitionKey;

    /**
     * 任务指派人
     */
    @Column(name = "assignee")
    private Long assignee;

    /**
     * 任务候选人
     */
    @Column(name = "candidate_users", length = 1000)
    private String candidateUsers;

    /**
     * 任务候选组
     */
    @Column(name = "candidate_groups", length = 1000)
    private String candidateGroups;

    /**
     * 任务所有者
     */
    @Column(name = "owner")
    private Long owner;

    /**
     * 任务优先级
     */
    @Column(name = "priority")
    private Integer priority;

    /**
     * 任务状态
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private Task.TaskStatus status;

    /**
     * 任务到期时间
     */
    @Column(name = "due_date")
    private LocalDateTime dueDate;

    /**
     * 任务完成时间
     */
    @Column(name = "complete_time")
    private LocalDateTime completeTime;

    /**
     * 任务变量
     */
    @Lob
    @Column(name = "variables", columnDefinition = "LONGTEXT")
    private String variables;

    /**
     * 任务表单Key
     */
    @Column(name = "form_key", length = 255)
    private String formKey;

    /**
     * 任务删除原因
     */
    @Column(name = "delete_reason", length = 500)
    private String deleteReason;

    /**
     * 转换为领域对象
     *
     * @return 任务实体
     */
    public Task toDomain() {
        Task task = new Task();
        task.setId(this.getId());
        task.setTaskId(com.flowmaster.workflow.domain.model.valueobject.TaskId.of(this.taskId));
        task.setProcessInstanceId(com.flowmaster.workflow.domain.model.valueobject.ProcessInstanceId.of(this.processInstanceId));
        task.setName(this.name);
        task.setDescription(this.description);
        task.setTaskDefinitionKey(this.taskDefinitionKey);
        task.setAssignee(this.assignee);
        task.setCandidateUsers(this.candidateUsers);
        task.setCandidateGroups(this.candidateGroups);
        task.setOwner(this.owner);
        task.setPriority(this.priority);
        task.setStatus(this.status);
        task.setDueDate(this.dueDate);
        task.setCompleteTime(this.completeTime);
        // TODO: 实现变量序列化/反序列化
        // task.setVariables(this.variables);
        task.setFormKey(this.formKey);
        task.setDeleteReason(this.deleteReason);
        task.setCreatedAt(this.getCreatedAt());
        task.setUpdatedAt(this.getUpdatedAt());
        task.setCreatedBy(this.getCreatedBy());
        task.setUpdatedBy(this.getUpdatedBy());
        task.setVersion(this.getVersion());
        task.setDeleted(this.getDeleted());
        return task;
    }

    /**
     * 从领域对象创建
     *
     * @param task 任务实体
     * @return JPA实体
     */
    public static TaskEntity fromDomain(Task task) {
        TaskEntity entity = new TaskEntity();
        entity.setId(task.getId());
        entity.taskId = task.getTaskId().getValue();
        entity.processInstanceId = task.getProcessInstanceId().getValue();
        entity.name = task.getName();
        entity.description = task.getDescription();
        entity.taskDefinitionKey = task.getTaskDefinitionKey();
        entity.assignee = task.getAssignee();
        entity.candidateUsers = task.getCandidateUsers();
        entity.candidateGroups = task.getCandidateGroups();
        entity.owner = task.getOwner();
        entity.priority = task.getPriority();
        entity.status = task.getStatus();
        entity.dueDate = task.getDueDate();
        entity.completeTime = task.getCompleteTime();
        // TODO: 实现变量序列化/反序列化
        // entity.variables = task.getVariables();
        entity.formKey = task.getFormKey();
        entity.deleteReason = task.getDeleteReason();
        entity.setCreatedAt(task.getCreatedAt());
        entity.setUpdatedAt(task.getUpdatedAt());
        entity.setCreatedBy(task.getCreatedBy());
        entity.setUpdatedBy(task.getUpdatedBy());
        entity.setVersion(task.getVersion());
        entity.setDeleted(task.getDeleted());
        return entity;
    }
}
