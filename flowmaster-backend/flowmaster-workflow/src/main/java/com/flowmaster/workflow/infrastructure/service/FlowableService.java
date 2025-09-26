package com.flowmaster.workflow.infrastructure.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Flowable工作流引擎服务
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FlowableService {

    private final ProcessEngine processEngine;
    private final RepositoryService repositoryService;
    private final RuntimeService runtimeService;
    private final TaskService taskService;

    /**
     * 部署流程定义
     *
     * @param name 部署名称
     * @param category 分类
     * @param inputStream 流程定义文件流
     * @return 部署对象
     */
    public Deployment deployProcessDefinition(String name, String category, InputStream inputStream) {
        log.info("部署流程定义: name={}, category={}", name, category);
        try {
            Deployment deployment = repositoryService.createDeployment()
                .name(name)
                .category(category)
                .addInputStream(name + ".bpmn20.xml", inputStream)
                .deploy();
            
            log.info("流程定义部署成功: deploymentId={}", deployment.getId());
            return deployment;
        } catch (Exception e) {
            log.error("流程定义部署失败: name={}, error={}", name, e.getMessage(), e);
            throw new RuntimeException("流程定义部署失败", e);
        }
    }

    /**
     * 启动流程实例
     *
     * @param processDefinitionKey 流程定义Key
     * @param businessKey 业务Key
     * @param variables 流程变量
     * @return 流程实例
     */
    public ProcessInstance startProcessInstance(String processDefinitionKey, String businessKey, Map<String, Object> variables) {
        log.info("启动流程实例: processDefinitionKey={}, businessKey={}", processDefinitionKey, businessKey);
        try {
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variables);
            log.info("流程实例启动成功: processInstanceId={}", processInstance.getId());
            return processInstance;
        } catch (Exception e) {
            log.error("流程实例启动失败: processDefinitionKey={}, error={}", processDefinitionKey, e.getMessage(), e);
            throw new RuntimeException("流程实例启动失败", e);
        }
    }

    /**
     * 完成流程实例
     *
     * @param processInstanceId 流程实例ID
     * @param deleteReason 删除原因
     */
    public void deleteProcessInstance(String processInstanceId, String deleteReason) {
        log.info("删除流程实例: processInstanceId={}, deleteReason={}", processInstanceId, deleteReason);
        try {
            runtimeService.deleteProcessInstance(processInstanceId, deleteReason);
            log.info("流程实例删除成功: processInstanceId={}", processInstanceId);
        } catch (Exception e) {
            log.error("流程实例删除失败: processInstanceId={}, error={}", processInstanceId, e.getMessage(), e);
            throw new RuntimeException("流程实例删除失败", e);
        }
    }

    /**
     * 暂停流程实例
     *
     * @param processInstanceId 流程实例ID
     */
    public void suspendProcessInstance(String processInstanceId) {
        log.info("暂停流程实例: processInstanceId={}", processInstanceId);
        try {
            runtimeService.suspendProcessInstanceById(processInstanceId);
            log.info("流程实例暂停成功: processInstanceId={}", processInstanceId);
        } catch (Exception e) {
            log.error("流程实例暂停失败: processInstanceId={}, error={}", processInstanceId, e.getMessage(), e);
            throw new RuntimeException("流程实例暂停失败", e);
        }
    }

    /**
     * 恢复流程实例
     *
     * @param processInstanceId 流程实例ID
     */
    public void activateProcessInstance(String processInstanceId) {
        log.info("恢复流程实例: processInstanceId={}", processInstanceId);
        try {
            runtimeService.activateProcessInstanceById(processInstanceId);
            log.info("流程实例恢复成功: processInstanceId={}", processInstanceId);
        } catch (Exception e) {
            log.error("流程实例恢复失败: processInstanceId={}, error={}", processInstanceId, e.getMessage(), e);
            throw new RuntimeException("流程实例恢复失败", e);
        }
    }

    /**
     * 更新流程实例变量
     *
     * @param processInstanceId 流程实例ID
     * @param variables 流程变量
     */
    public void updateProcessInstanceVariables(String processInstanceId, Map<String, Object> variables) {
        log.info("更新流程实例变量: processInstanceId={}", processInstanceId);
        try {
            runtimeService.setVariables(processInstanceId, variables);
            log.info("流程实例变量更新成功: processInstanceId={}", processInstanceId);
        } catch (Exception e) {
            log.error("流程实例变量更新失败: processInstanceId={}, error={}", processInstanceId, e.getMessage(), e);
            throw new RuntimeException("流程实例变量更新失败", e);
        }
    }

    /**
     * 完成任务
     *
     * @param taskId 任务ID
     * @param variables 任务变量
     */
    public void completeTask(String taskId, Map<String, Object> variables) {
        log.info("完成任务: taskId={}", taskId);
        try {
            taskService.complete(taskId, variables);
            log.info("任务完成成功: taskId={}", taskId);
        } catch (Exception e) {
            log.error("任务完成失败: taskId={}, error={}", taskId, e.getMessage(), e);
            throw new RuntimeException("任务完成失败", e);
        }
    }

    /**
     * 分配任务
     *
     * @param taskId 任务ID
     * @param assignee 指派人
     */
    public void assignTask(String taskId, String assignee) {
        log.info("分配任务: taskId={}, assignee={}", taskId, assignee);
        try {
            taskService.setAssignee(taskId, assignee);
            log.info("任务分配成功: taskId={}", taskId);
        } catch (Exception e) {
            log.error("任务分配失败: taskId={}, error={}", taskId, e.getMessage(), e);
            throw new RuntimeException("任务分配失败", e);
        }
    }

    /**
     * 获取流程实例的待办任务
     *
     * @param processInstanceId 流程实例ID
     * @return 任务列表
     */
    public List<Task> getProcessInstanceTasks(String processInstanceId) {
        log.debug("获取流程实例待办任务: processInstanceId={}", processInstanceId);
        try {
            List<Task> tasks = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .active()
                .list();
            log.debug("获取流程实例待办任务成功: processInstanceId={}, taskCount={}", processInstanceId, tasks.size());
            return tasks;
        } catch (Exception e) {
            log.error("获取流程实例待办任务失败: processInstanceId={}, error={}", processInstanceId, e.getMessage(), e);
            throw new RuntimeException("获取流程实例待办任务失败", e);
        }
    }

    /**
     * 获取用户的待办任务
     *
     * @param userId 用户ID
     * @return 任务列表
     */
    public List<Task> getUserTasks(String userId) {
        log.debug("获取用户待办任务: userId={}", userId);
        try {
            List<Task> tasks = taskService.createTaskQuery()
                .taskAssignee(userId)
                .active()
                .list();
            log.debug("获取用户待办任务成功: userId={}, taskCount={}", userId, tasks.size());
            return tasks;
        } catch (Exception e) {
            log.error("获取用户待办任务失败: userId={}, error={}", userId, e.getMessage(), e);
            throw new RuntimeException("获取用户待办任务失败", e);
        }
    }

    /**
     * 获取用户的候选任务
     *
     * @param userId 用户ID
     * @return 任务列表
     */
    public List<Task> getUserCandidateTasks(String userId) {
        log.debug("获取用户候选任务: userId={}", userId);
        try {
            List<Task> tasks = taskService.createTaskQuery()
                .taskCandidateUser(userId)
                .active()
                .list();
            log.debug("获取用户候选任务成功: userId={}, taskCount={}", userId, tasks.size());
            return tasks;
        } catch (Exception e) {
            log.error("获取用户候选任务失败: userId={}, error={}", userId, e.getMessage(), e);
            throw new RuntimeException("获取用户候选任务失败", e);
        }
    }

    /**
     * 检查流程定义是否存在
     *
     * @param processDefinitionKey 流程定义Key
     * @return 是否存在
     */
    public boolean processDefinitionExists(String processDefinitionKey) {
        try {
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processDefinitionKey)
                .latestVersion()
                .singleResult();
            return processDefinition != null;
        } catch (Exception e) {
            log.error("检查流程定义是否存在失败: processDefinitionKey={}, error={}", processDefinitionKey, e.getMessage(), e);
            return false;
        }
    }

    /**
     * 检查流程实例是否存在
     *
     * @param processInstanceId 流程实例ID
     * @return 是否存在
     */
    public boolean processInstanceExists(String processInstanceId) {
        try {
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
            return processInstance != null;
        } catch (Exception e) {
            log.error("检查流程实例是否存在失败: processInstanceId={}, error={}", processInstanceId, e.getMessage(), e);
            return false;
        }
    }

    /**
     * 检查任务是否存在
     *
     * @param taskId 任务ID
     * @return 是否存在
     */
    public boolean taskExists(String taskId) {
        try {
            Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();
            return task != null;
        } catch (Exception e) {
            log.error("检查任务是否存在失败: taskId={}, error={}", taskId, e.getMessage(), e);
            return false;
        }
    }
}
