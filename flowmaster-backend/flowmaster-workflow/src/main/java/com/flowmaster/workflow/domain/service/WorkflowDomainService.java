package com.flowmaster.workflow.domain.service;

import com.flowmaster.workflow.domain.model.aggregate.ProcessDefinition;
import com.flowmaster.workflow.domain.model.aggregate.ProcessInstance;
import com.flowmaster.workflow.domain.model.entity.Task;
import com.flowmaster.workflow.domain.model.valueobject.ProcessDefinitionId;
import com.flowmaster.workflow.domain.model.valueobject.ProcessInstanceId;
import com.flowmaster.workflow.domain.model.valueobject.TaskId;

import java.util.List;
import java.util.Map;

/**
 * 工作流领域服务
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
public interface WorkflowDomainService {

    /**
     * 部署流程定义
     *
     * @param processDefinition 流程定义
     * @return 部署结果
     */
    boolean deployProcessDefinition(ProcessDefinition processDefinition);

    /**
     * 启动流程实例
     *
     * @param processDefinitionId 流程定义ID
     * @param businessKey 业务Key
     * @param variables 流程变量
     * @param startedBy 启动人
     * @return 流程实例
     */
    ProcessInstance startProcessInstance(ProcessDefinitionId processDefinitionId, String businessKey, 
                                       Map<String, Object> variables, Long startedBy);

    /**
     * 完成流程实例
     *
     * @param processInstanceId 流程实例ID
     * @param endReason 结束原因
     */
    void completeProcessInstance(ProcessInstanceId processInstanceId, String endReason);

    /**
     * 取消流程实例
     *
     * @param processInstanceId 流程实例ID
     * @param cancelReason 取消原因
     */
    void cancelProcessInstance(ProcessInstanceId processInstanceId, String cancelReason);

    /**
     * 暂停流程实例
     *
     * @param processInstanceId 流程实例ID
     * @param suspendReason 暂停原因
     */
    void suspendProcessInstance(ProcessInstanceId processInstanceId, String suspendReason);

    /**
     * 恢复流程实例
     *
     * @param processInstanceId 流程实例ID
     */
    void resumeProcessInstance(ProcessInstanceId processInstanceId);

    /**
     * 删除流程实例
     *
     * @param processInstanceId 流程实例ID
     * @param deleteReason 删除原因
     */
    void deleteProcessInstance(ProcessInstanceId processInstanceId, String deleteReason);

    /**
     * 更新流程实例变量
     *
     * @param processInstanceId 流程实例ID
     * @param variables 流程变量
     */
    void updateProcessInstanceVariables(ProcessInstanceId processInstanceId, Map<String, Object> variables);

    /**
     * 完成任务
     *
     * @param taskId 任务ID
     * @param variables 任务变量
     * @param completeReason 完成原因
     */
    void completeTask(TaskId taskId, Map<String, Object> variables, String completeReason);

    /**
     * 分配任务
     *
     * @param taskId 任务ID
     * @param assignee 指派人
     */
    void assignTask(TaskId taskId, Long assignee);

    /**
     * 取消任务
     *
     * @param taskId 任务ID
     * @param cancelReason 取消原因
     */
    void cancelTask(TaskId taskId, String cancelReason);

    /**
     * 暂停任务
     *
     * @param taskId 任务ID
     * @param suspendReason 暂停原因
     */
    void suspendTask(TaskId taskId, String suspendReason);

    /**
     * 恢复任务
     *
     * @param taskId 任务ID
     */
    void resumeTask(TaskId taskId);

    /**
     * 删除任务
     *
     * @param taskId 任务ID
     * @param deleteReason 删除原因
     */
    void deleteTask(TaskId taskId, String deleteReason);

    /**
     * 更新任务变量
     *
     * @param taskId 任务ID
     * @param variables 任务变量
     */
    void updateTaskVariables(TaskId taskId, Map<String, Object> variables);

    /**
     * 获取流程实例的待办任务
     *
     * @param processInstanceId 流程实例ID
     * @return 待办任务列表
     */
    List<Task> getPendingTasks(ProcessInstanceId processInstanceId);

    /**
     * 获取用户的待办任务
     *
     * @param userId 用户ID
     * @return 待办任务列表
     */
    List<Task> getUserPendingTasks(Long userId);

    /**
     * 获取用户的候选任务
     *
     * @param userId 用户ID
     * @return 候选任务列表
     */
    List<Task> getUserCandidateTasks(Long userId);

    /**
     * 检查流程定义是否可以启动
     *
     * @param processDefinitionId 流程定义ID
     * @return 是否可以启动
     */
    boolean canStartProcessInstance(ProcessDefinitionId processDefinitionId);

    /**
     * 检查任务是否可以完成
     *
     * @param taskId 任务ID
     * @return 是否可以完成
     */
    boolean canCompleteTask(TaskId taskId);

    /**
     * 检查用户是否有权限操作任务
     *
     * @param taskId 任务ID
     * @param userId 用户ID
     * @return 是否有权限
     */
    boolean hasTaskPermission(TaskId taskId, Long userId);
}
