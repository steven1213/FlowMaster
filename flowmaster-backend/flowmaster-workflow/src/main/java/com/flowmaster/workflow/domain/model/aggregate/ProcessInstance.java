package com.flowmaster.workflow.domain.model.aggregate;

import com.flowmaster.common.domain.BaseDomainEntity;
import com.flowmaster.workflow.domain.model.valueobject.ProcessDefinitionId;
import com.flowmaster.workflow.domain.model.valueobject.ProcessInstanceId;
import com.flowmaster.workflow.domain.model.valueobject.ProcessStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 流程实例聚合根
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Getter
@Setter
@Slf4j
public class ProcessInstance extends BaseDomainEntity {

    /**
     * 流程实例ID
     */
    private ProcessInstanceId processInstanceId;

    /**
     * 流程定义ID
     */
    private ProcessDefinitionId processDefinitionId;

    /**
     * 流程实例名称
     */
    private String name;

    /**
     * 流程实例状态
     */
    private ProcessStatus status;

    /**
     * 流程实例启动人
     */
    private Long startedBy;

    /**
     * 流程实例启动时间
     */
    private LocalDateTime startTime;

    /**
     * 流程实例结束时间
     */
    private LocalDateTime endTime;

    /**
     * 流程实例业务Key
     */
    private String businessKey;

    /**
     * 流程实例变量
     */
    private Map<String, Object> variables;

    /**
     * 流程实例父实例ID
     */
    private ProcessInstanceId parentInstanceId;

    /**
     * 流程实例删除原因
     */
    private String deleteReason;

    /**
     * 领域事件列表
     */
    private final List<Object> domainEvents = new ArrayList<>();

    /**
     * 创建流程实例
     *
     * @param processInstanceId 流程实例ID
     * @param processDefinitionId 流程定义ID
     * @param name 流程实例名称
     * @param businessKey 业务Key
     * @param variables 流程变量
     * @param startedBy 启动人
     * @return 流程实例
     */
    public static ProcessInstance create(ProcessInstanceId processInstanceId, ProcessDefinitionId processDefinitionId, 
                                       String name, String businessKey, Map<String, Object> variables, Long startedBy) {
        ProcessInstance instance = new ProcessInstance();
        instance.processInstanceId = processInstanceId;
        instance.processDefinitionId = processDefinitionId;
        instance.name = name;
        instance.status = ProcessStatus.of(ProcessStatus.Status.RUNNING);
        instance.businessKey = businessKey;
        instance.variables = variables;
        instance.startedBy = startedBy;
        instance.startTime = LocalDateTime.now();
        
        instance.addDomainEvent(new ProcessInstanceStartedEvent(instance));
        return instance;
    }

    /**
     * 完成流程实例
     *
     * @param endReason 结束原因
     */
    public void complete(String endReason) {
        this.status = ProcessStatus.of(ProcessStatus.Status.COMPLETED);
        this.endTime = LocalDateTime.now();
        this.deleteReason = endReason;
        
        addDomainEvent(new ProcessInstanceCompletedEvent(this));
    }

    /**
     * 取消流程实例
     *
     * @param cancelReason 取消原因
     */
    public void cancel(String cancelReason) {
        this.status = ProcessStatus.of(ProcessStatus.Status.CANCELLED);
        this.endTime = LocalDateTime.now();
        this.deleteReason = cancelReason;
        
        addDomainEvent(new ProcessInstanceCancelledEvent(this));
    }

    /**
     * 暂停流程实例
     *
     * @param suspendReason 暂停原因
     */
    public void suspend(String suspendReason) {
        this.status = ProcessStatus.of(ProcessStatus.Status.SUSPENDED);
        this.deleteReason = suspendReason;
        
        addDomainEvent(new ProcessInstanceSuspendedEvent(this));
    }

    /**
     * 恢复流程实例
     */
    public void resume() {
        this.status = ProcessStatus.of(ProcessStatus.Status.RUNNING);
        this.deleteReason = null;
        
        addDomainEvent(new ProcessInstanceResumedEvent(this));
    }

    /**
     * 删除流程实例
     *
     * @param deleteReason 删除原因
     */
    public void delete(String deleteReason) {
        setDeleted(true);
        this.deleteReason = deleteReason;
        
        addDomainEvent(new ProcessInstanceDeletedEvent(this));
    }

    /**
     * 更新流程变量
     *
     * @param variables 新的流程变量
     */
    public void updateVariables(Map<String, Object> variables) {
        this.variables = variables;
        
        addDomainEvent(new ProcessInstanceVariablesUpdatedEvent(this));
    }

    /**
     * 检查流程实例是否正在运行
     *
     * @return 是否正在运行
     */
    public boolean isRunning() {
        return status.isRunning();
    }

    /**
     * 检查流程实例是否已完成
     *
     * @return 是否已完成
     */
    public boolean isCompleted() {
        return status.isCompleted();
    }

    /**
     * 检查流程实例是否已取消
     *
     * @return 是否已取消
     */
    public boolean isCancelled() {
        return status.isCancelled();
    }

    /**
     * 检查流程实例是否已暂停
     *
     * @return 是否已暂停
     */
    public boolean isSuspended() {
        return status.isSuspended();
    }

    /**
     * 检查流程实例是否已删除
     *
     * @return 是否已删除
     */
    public boolean isDeleted() {
        return getDeleted() != null && getDeleted();
    }

    /**
     * 添加领域事件
     *
     * @param event 领域事件
     */
    private void addDomainEvent(Object event) {
        domainEvents.add(event);
    }

    /**
     * 获取领域事件
     *
     * @return 领域事件列表
     */
    @DomainEvents
    public List<Object> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    /**
     * 清理领域事件
     */
    @AfterDomainEventPublication
    public void clearDomainEvents() {
        domainEvents.clear();
    }

    // 领域事件类
    public static class ProcessInstanceStartedEvent {
        private final ProcessInstance processInstance;
        
        public ProcessInstanceStartedEvent(ProcessInstance processInstance) {
            this.processInstance = processInstance;
        }
        
        public ProcessInstance getProcessInstance() {
            return processInstance;
        }
    }

    public static class ProcessInstanceCompletedEvent {
        private final ProcessInstance processInstance;
        
        public ProcessInstanceCompletedEvent(ProcessInstance processInstance) {
            this.processInstance = processInstance;
        }
        
        public ProcessInstance getProcessInstance() {
            return processInstance;
        }
    }

    public static class ProcessInstanceCancelledEvent {
        private final ProcessInstance processInstance;
        
        public ProcessInstanceCancelledEvent(ProcessInstance processInstance) {
            this.processInstance = processInstance;
        }
        
        public ProcessInstance getProcessInstance() {
            return processInstance;
        }
    }

    public static class ProcessInstanceSuspendedEvent {
        private final ProcessInstance processInstance;
        
        public ProcessInstanceSuspendedEvent(ProcessInstance processInstance) {
            this.processInstance = processInstance;
        }
        
        public ProcessInstance getProcessInstance() {
            return processInstance;
        }
    }

    public static class ProcessInstanceResumedEvent {
        private final ProcessInstance processInstance;
        
        public ProcessInstanceResumedEvent(ProcessInstance processInstance) {
            this.processInstance = processInstance;
        }
        
        public ProcessInstance getProcessInstance() {
            return processInstance;
        }
    }

    public static class ProcessInstanceDeletedEvent {
        private final ProcessInstance processInstance;
        
        public ProcessInstanceDeletedEvent(ProcessInstance processInstance) {
            this.processInstance = processInstance;
        }
        
        public ProcessInstance getProcessInstance() {
            return processInstance;
        }
    }

    public static class ProcessInstanceVariablesUpdatedEvent {
        private final ProcessInstance processInstance;
        
        public ProcessInstanceVariablesUpdatedEvent(ProcessInstance processInstance) {
            this.processInstance = processInstance;
        }
        
        public ProcessInstance getProcessInstance() {
            return processInstance;
        }
    }
}
