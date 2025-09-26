package com.flowmaster.workflow.infrastructure.persistence.entity;

import com.flowmaster.common.infrastructure.persistence.BaseJpaEntity;
import com.flowmaster.workflow.domain.model.aggregate.ProcessInstance;
import com.flowmaster.workflow.domain.model.valueobject.ProcessStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 流程实例JPA实体
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Entity
@Table(name = "wf_process_instance", indexes = {
    @Index(name = "idx_process_inst_def_id", columnList = "process_definition_id"),
    @Index(name = "idx_process_inst_business_key", columnList = "business_key"),
    @Index(name = "idx_process_inst_started_by", columnList = "started_by"),
    @Index(name = "idx_process_inst_status", columnList = "status"),
    @Index(name = "idx_process_inst_deleted", columnList = "deleted")
})
@Getter
@Setter
@Slf4j
@SQLDelete(sql = "UPDATE wf_process_instance SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class ProcessInstanceEntity extends BaseJpaEntity {

    /**
     * 流程实例ID
     */
    @Column(name = "process_instance_id", nullable = false, unique = true, length = 64)
    private String processInstanceId;

    /**
     * 流程定义ID
     */
    @Column(name = "process_definition_id", nullable = false, length = 64)
    private String processDefinitionId;

    /**
     * 流程实例名称
     */
    @Column(name = "name", length = 255)
    private String name;

    /**
     * 流程实例状态
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ProcessStatus.Status status;

    /**
     * 流程实例启动人
     */
    @Column(name = "started_by")
    private Long startedBy;

    /**
     * 流程实例启动时间
     */
    @Column(name = "start_time")
    private LocalDateTime startTime;

    /**
     * 流程实例结束时间
     */
    @Column(name = "end_time")
    private LocalDateTime endTime;

    /**
     * 流程实例业务Key
     */
    @Column(name = "business_key", length = 255)
    private String businessKey;

    /**
     * 流程实例变量
     */
    @Lob
    @Column(name = "variables", columnDefinition = "LONGTEXT")
    private String variables;

    /**
     * 流程实例父实例ID
     */
    @Column(name = "parent_instance_id", length = 64)
    private String parentInstanceId;

    /**
     * 流程实例删除原因
     */
    @Column(name = "delete_reason", length = 500)
    private String deleteReason;

    /**
     * 转换为领域对象
     *
     * @return 流程实例聚合根
     */
    public ProcessInstance toDomain() {
        ProcessInstance instance = new ProcessInstance();
        instance.setId(this.getId());
        instance.setProcessInstanceId(com.flowmaster.workflow.domain.model.valueobject.ProcessInstanceId.of(this.processInstanceId));
        instance.setProcessDefinitionId(com.flowmaster.workflow.domain.model.valueobject.ProcessDefinitionId.of(this.processDefinitionId));
        instance.setName(this.name);
        instance.setStatus(ProcessStatus.of(this.status));
        instance.setStartedBy(this.startedBy);
        instance.setStartTime(this.startTime);
        instance.setEndTime(this.endTime);
        instance.setBusinessKey(this.businessKey);
        // TODO: 实现变量序列化/反序列化
        // instance.setVariables(this.variables);
        if (this.parentInstanceId != null) {
            instance.setParentInstanceId(com.flowmaster.workflow.domain.model.valueobject.ProcessInstanceId.of(this.parentInstanceId));
        }
        instance.setDeleteReason(this.deleteReason);
        instance.setCreatedAt(this.getCreatedAt());
        instance.setUpdatedAt(this.getUpdatedAt());
        instance.setCreatedBy(this.getCreatedBy());
        instance.setUpdatedBy(this.getUpdatedBy());
        instance.setVersion(this.getVersion());
        instance.setDeleted(this.getDeleted());
        return instance;
    }

    /**
     * 从领域对象创建
     *
     * @param instance 流程实例聚合根
     * @return JPA实体
     */
    public static ProcessInstanceEntity fromDomain(ProcessInstance instance) {
        ProcessInstanceEntity entity = new ProcessInstanceEntity();
        entity.setId(instance.getId());
        entity.processInstanceId = instance.getProcessInstanceId().getValue();
        entity.processDefinitionId = instance.getProcessDefinitionId().getValue();
        entity.name = instance.getName();
        entity.status = instance.getStatus().getStatus();
        entity.startedBy = instance.getStartedBy();
        entity.startTime = instance.getStartTime();
        entity.endTime = instance.getEndTime();
        entity.businessKey = instance.getBusinessKey();
        // TODO: 实现变量序列化/反序列化
        // entity.variables = instance.getVariables();
        if (instance.getParentInstanceId() != null) {
            entity.parentInstanceId = instance.getParentInstanceId().getValue();
        }
        entity.deleteReason = instance.getDeleteReason();
        entity.setCreatedAt(instance.getCreatedAt());
        entity.setUpdatedAt(instance.getUpdatedAt());
        entity.setCreatedBy(instance.getCreatedBy());
        entity.setUpdatedBy(instance.getUpdatedBy());
        entity.setVersion(instance.getVersion());
        entity.setDeleted(instance.getDeleted());
        return entity;
    }
}
