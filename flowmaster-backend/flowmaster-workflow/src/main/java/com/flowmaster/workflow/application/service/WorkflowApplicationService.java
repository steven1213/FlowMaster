package com.flowmaster.workflow.application.service;

import com.flowmaster.common.response.PageResult;
import com.flowmaster.common.response.Result;
import com.flowmaster.common.response.ResultCode;
import com.flowmaster.workflow.application.command.*;
import com.flowmaster.workflow.application.dto.ProcessDefinitionDTO;
import com.flowmaster.workflow.application.dto.ProcessInstanceDTO;
import com.flowmaster.workflow.application.dto.TaskDTO;
import com.flowmaster.workflow.application.query.ProcessDefinitionQuery;
import com.flowmaster.workflow.application.query.ProcessInstanceQuery;
import com.flowmaster.workflow.application.query.TaskQuery;
import com.flowmaster.workflow.domain.model.aggregate.ProcessDefinition;
import com.flowmaster.workflow.domain.model.aggregate.ProcessInstance;
import com.flowmaster.workflow.domain.model.entity.Task;
import com.flowmaster.workflow.domain.model.valueobject.ProcessDefinitionId;
import com.flowmaster.workflow.domain.model.valueobject.ProcessInstanceId;
import com.flowmaster.workflow.domain.model.valueobject.TaskId;
import com.flowmaster.workflow.domain.repository.ProcessDefinitionRepository;
import com.flowmaster.workflow.domain.repository.ProcessInstanceRepository;
import com.flowmaster.workflow.domain.repository.TaskRepository;
import com.flowmaster.workflow.domain.service.WorkflowDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 工作流应用服务
 * 处理工作流相关的用例
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WorkflowApplicationService {

    private final WorkflowDomainService workflowDomainService;
    private final ProcessDefinitionRepository processDefinitionRepository;
    private final ProcessInstanceRepository processInstanceRepository;
    private final TaskRepository taskRepository;

    /**
     * 部署流程定义
     *
     * @param command 部署命令
     * @return 流程定义DTO
     */
    @Transactional
    public Result<ProcessDefinitionDTO> deployProcessDefinition(DeployProcessDefinitionCommand command) {
        log.info("部署流程定义请求: name={}, key={}", command.getName(), command.getKey());
        try {
            // 1. 创建流程定义聚合根
            ProcessDefinitionId processDefinitionId = ProcessDefinitionId.of(command.getKey() + "_" + System.currentTimeMillis());
            ProcessDefinition processDefinition = ProcessDefinition.create(
                processDefinitionId,
                command.getName(),
                command.getKey(),
                1, // 初始版本
                command.getCategory(),
                command.getDescription(),
                command.getXmlContent(),
                command.getDeployedBy()
            );

            // 2. 部署到Flowable引擎
            boolean deployed = workflowDomainService.deployProcessDefinition(processDefinition);
            if (!deployed) {
                return Result.fail("流程定义部署失败");
            }

            // 3. 保存到数据库
            processDefinition.activate(); // 激活流程定义
            ProcessDefinition savedDefinition = processDefinitionRepository.save(processDefinition);
            
            log.info("流程定义部署成功: processDefinitionId={}", savedDefinition.getProcessDefinitionId().getValue());
            return Result.success(toProcessDefinitionDTO(savedDefinition));
        } catch (Exception e) {
            log.error("部署流程定义异常: name={}, error={}", command.getName(), e.getMessage(), e);
            return Result.fail(ResultCode.SYSTEM_ERROR.getMessage());
        }
    }

    /**
     * 启动流程实例
     *
     * @param command 启动命令
     * @return 流程实例DTO
     */
    @Transactional
    public Result<ProcessInstanceDTO> startProcessInstance(StartProcessInstanceCommand command) {
        log.info("启动流程实例请求: processDefinitionKey={}, businessKey={}", 
                command.getProcessDefinitionKey(), command.getBusinessKey());
        try {
            // 1. 查找流程定义
            ProcessDefinitionId processDefinitionId = ProcessDefinitionId.of(command.getProcessDefinitionKey());
            Optional<ProcessDefinition> definitionOpt = processDefinitionRepository.findById(processDefinitionId);
            if (definitionOpt.isEmpty()) {
                return Result.fail("流程定义不存在");
            }

            ProcessDefinition definition = definitionOpt.get();
            if (!definition.canStart()) {
                return Result.fail("流程定义不可用，无法启动");
            }

            // 2. 启动流程实例
            ProcessInstanceId processInstanceId = ProcessInstanceId.of("inst_" + System.currentTimeMillis());
            ProcessInstance processInstance = workflowDomainService.startProcessInstance(
                processDefinitionId,
                command.getBusinessKey(),
                command.getVariables(),
                command.getStartedBy()
            );

            // 3. 保存到数据库
            ProcessInstance savedInstance = processInstanceRepository.save(processInstance);
            
            log.info("流程实例启动成功: processInstanceId={}", savedInstance.getProcessInstanceId().getValue());
            return Result.success(toProcessInstanceDTO(savedInstance));
        } catch (Exception e) {
            log.error("启动流程实例异常: processDefinitionKey={}, error={}", 
                     command.getProcessDefinitionKey(), e.getMessage(), e);
            return Result.fail(ResultCode.SYSTEM_ERROR.getMessage());
        }
    }

    /**
     * 完成流程实例
     *
     * @param command 完成命令
     * @return 成功结果
     */
    @Transactional
    public Result<Void> completeProcessInstance(CompleteProcessInstanceCommand command) {
        log.info("完成流程实例请求: processInstanceId={}", command.getProcessInstanceId());
        try {
            // 1. 查找流程实例
            ProcessInstanceId processInstanceId = ProcessInstanceId.of(command.getProcessInstanceId());
            Optional<ProcessInstance> instanceOpt = processInstanceRepository.findById(processInstanceId);
            if (instanceOpt.isEmpty()) {
                return Result.fail("流程实例不存在");
            }

            ProcessInstance instance = instanceOpt.get();
            if (!instance.isRunning()) {
                return Result.fail("流程实例不在运行状态，无法完成");
            }

            // 2. 完成流程实例
            workflowDomainService.completeProcessInstance(processInstanceId, command.getEndReason());

            // 3. 更新数据库
            instance.complete(command.getEndReason());
            processInstanceRepository.save(instance);
            
            log.info("流程实例完成成功: processInstanceId={}", command.getProcessInstanceId());
            return Result.success();
        } catch (Exception e) {
            log.error("完成流程实例异常: processInstanceId={}, error={}", 
                     command.getProcessInstanceId(), e.getMessage(), e);
            return Result.fail(ResultCode.SYSTEM_ERROR.getMessage());
        }
    }

    /**
     * 完成任务
     *
     * @param command 完成命令
     * @return 成功结果
     */
    @Transactional
    public Result<Void> completeTask(CompleteTaskCommand command) {
        log.info("完成任务请求: taskId={}", command.getTaskId());
        try {
            // 1. 查找任务
            TaskId taskId = TaskId.of(command.getTaskId());
            Optional<Task> taskOpt = taskRepository.findById(taskId);
            if (taskOpt.isEmpty()) {
                return Result.fail("任务不存在");
            }

            Task task = taskOpt.get();
            if (task.isCompleted()) {
                return Result.fail("任务已完成，无法重复完成");
            }

            // 2. 检查权限
            if (!workflowDomainService.hasTaskPermission(taskId, command.getOperatorId())) {
                return Result.fail("无权限操作此任务");
            }

            // 3. 完成任务
            workflowDomainService.completeTask(taskId, command.getVariables(), command.getCompleteReason());

            // 4. 更新数据库
            task.complete(command.getCompleteReason());
            taskRepository.save(task);
            
            log.info("任务完成成功: taskId={}", command.getTaskId());
            return Result.success();
        } catch (Exception e) {
            log.error("完成任务异常: taskId={}, error={}", 
                     command.getTaskId(), e.getMessage(), e);
            return Result.fail(ResultCode.SYSTEM_ERROR.getMessage());
        }
    }

    /**
     * 分配任务
     *
     * @param command 分配命令
     * @return 成功结果
     */
    @Transactional
    public Result<Void> assignTask(AssignTaskCommand command) {
        log.info("分配任务请求: taskId={}, assignee={}", command.getTaskId(), command.getAssignee());
        try {
            // 1. 查找任务
            TaskId taskId = TaskId.of(command.getTaskId());
            Optional<Task> taskOpt = taskRepository.findById(taskId);
            if (taskOpt.isEmpty()) {
                return Result.fail("任务不存在");
            }

            Task task = taskOpt.get();
            if (task.isCompleted()) {
                return Result.fail("任务已完成，无法分配");
            }

            // 2. 分配任务
            workflowDomainService.assignTask(taskId, command.getAssignee());

            // 3. 更新数据库
            task.assign(command.getAssignee());
            taskRepository.save(task);
            
            log.info("任务分配成功: taskId={}, assignee={}", command.getTaskId(), command.getAssignee());
            return Result.success();
        } catch (Exception e) {
            log.error("分配任务异常: taskId={}, error={}", 
                     command.getTaskId(), e.getMessage(), e);
            return Result.fail(ResultCode.SYSTEM_ERROR.getMessage());
        }
    }

    /**
     * 查询流程定义
     *
     * @param query 查询条件
     * @return 流程定义分页结果
     */
    @Transactional(readOnly = true)
    public Result<PageResult<ProcessDefinitionDTO>> queryProcessDefinitions(ProcessDefinitionQuery query) {
        log.debug("查询流程定义请求: query={}", query);
        try {
            // 构建分页参数
            Sort sort = Sort.by(Sort.Direction.fromString(query.getSortDirection()), query.getSortBy());
            Pageable pageable = PageRequest.of(query.getPage(), query.getSize(), sort);

            // 执行查询
            Page<ProcessDefinition> definitionPage = processDefinitionRepository.findAll(pageable);

            // 转换为DTO
            List<ProcessDefinitionDTO> definitions = definitionPage.getContent().stream()
                .map(this::toProcessDefinitionDTO)
                .collect(Collectors.toList());

            PageResult<ProcessDefinitionDTO> result = PageResult.of(
                definitions,
                definitionPage.getTotalElements(),
                (long) definitionPage.getNumber(),
                (long) definitionPage.getSize()
            );

            return Result.success(result);
        } catch (Exception e) {
            log.error("查询流程定义异常: error={}", e.getMessage(), e);
            return Result.fail(ResultCode.SYSTEM_ERROR.getMessage());
        }
    }

    /**
     * 查询流程实例
     *
     * @param query 查询条件
     * @return 流程实例分页结果
     */
    @Transactional(readOnly = true)
    public Result<PageResult<ProcessInstanceDTO>> queryProcessInstances(ProcessInstanceQuery query) {
        log.debug("查询流程实例请求: query={}", query);
        try {
            // 构建分页参数
            Sort sort = Sort.by(Sort.Direction.fromString(query.getSortDirection()), query.getSortBy());
            Pageable pageable = PageRequest.of(query.getPage(), query.getSize(), sort);

            // 执行查询
            Page<ProcessInstance> instancePage = processInstanceRepository.findByDeletedFalse(pageable);

            // 转换为DTO
            List<ProcessInstanceDTO> instances = instancePage.getContent().stream()
                .map(this::toProcessInstanceDTO)
                .collect(Collectors.toList());

            PageResult<ProcessInstanceDTO> result = PageResult.of(
                instances,
                instancePage.getTotalElements(),
                (long) instancePage.getNumber(),
                (long) instancePage.getSize()
            );

            return Result.success(result);
        } catch (Exception e) {
            log.error("查询流程实例异常: error={}", e.getMessage(), e);
            return Result.fail(ResultCode.SYSTEM_ERROR.getMessage());
        }
    }

    /**
     * 查询任务
     *
     * @param query 查询条件
     * @return 任务分页结果
     */
    @Transactional(readOnly = true)
    public Result<PageResult<TaskDTO>> queryTasks(TaskQuery query) {
        log.debug("查询任务请求: query={}", query);
        try {
            // 构建分页参数
            Sort sort = Sort.by(Sort.Direction.fromString(query.getSortDirection()), query.getSortBy());
            Pageable pageable = PageRequest.of(query.getPage(), query.getSize(), sort);

            // 执行查询
            Page<Task> taskPage = taskRepository.findByDeletedFalse(pageable);

            // 转换为DTO
            List<TaskDTO> tasks = taskPage.getContent().stream()
                .map(this::toTaskDTO)
                .collect(Collectors.toList());

            PageResult<TaskDTO> result = PageResult.of(
                tasks,
                taskPage.getTotalElements(),
                (long) taskPage.getNumber(),
                (long) taskPage.getSize()
            );

            return Result.success(result);
        } catch (Exception e) {
            log.error("查询任务异常: error={}", e.getMessage(), e);
            return Result.fail(ResultCode.SYSTEM_ERROR.getMessage());
        }
    }

    /**
     * 获取用户待办任务
     *
     * @param userId 用户ID
     * @param pageable 分页参数
     * @return 任务分页结果
     */
    @Transactional(readOnly = true)
    public Result<PageResult<TaskDTO>> getUserPendingTasks(Long userId, Pageable pageable) {
        log.debug("获取用户待办任务请求: userId={}", userId);
        try {
            Page<Task> taskPage = taskRepository.findPendingTasksByUser(userId, pageable);

            List<TaskDTO> tasks = taskPage.getContent().stream()
                .map(this::toTaskDTO)
                .collect(Collectors.toList());

            PageResult<TaskDTO> result = PageResult.of(
                tasks,
                taskPage.getTotalElements(),
                (long) taskPage.getNumber(),
                (long) taskPage.getSize()
            );

            return Result.success(result);
        } catch (Exception e) {
            log.error("获取用户待办任务异常: userId={}, error={}", userId, e.getMessage(), e);
            return Result.fail(ResultCode.SYSTEM_ERROR.getMessage());
        }
    }

    // DTO转换方法
    private ProcessDefinitionDTO toProcessDefinitionDTO(ProcessDefinition definition) {
        ProcessDefinitionDTO dto = new ProcessDefinitionDTO();
        dto.setId(definition.getId());
        dto.setProcessDefinitionId(definition.getProcessDefinitionId().getValue());
        dto.setName(definition.getName());
        dto.setKey(definition.getKey());
        dto.setVersion(definition.getVersion());
        dto.setCategory(definition.getCategory());
        dto.setDescription(definition.getDescription());
        dto.setStatus(definition.getStatus().name());
        dto.setStatusDescription(definition.getStatus().getDescription());
        dto.setXmlContent(definition.getXmlContent());
        dto.setImageContent(definition.getImageContent());
        dto.setDeploymentTime(definition.getDeploymentTime());
        dto.setDeployedBy(definition.getDeployedBy());
        dto.setTags(definition.getTags());
        dto.setEnabled(definition.getEnabled());
        dto.setCreatedAt(definition.getCreatedAt());
        dto.setUpdatedAt(definition.getUpdatedAt());
        dto.setCreatedBy(definition.getCreatedBy());
        dto.setUpdatedBy(definition.getUpdatedBy());
        return dto;
    }

    private ProcessInstanceDTO toProcessInstanceDTO(ProcessInstance instance) {
        ProcessInstanceDTO dto = new ProcessInstanceDTO();
        dto.setId(instance.getId());
        dto.setProcessInstanceId(instance.getProcessInstanceId().getValue());
        dto.setProcessDefinitionId(instance.getProcessDefinitionId().getValue());
        dto.setName(instance.getName());
        dto.setStatus(instance.getStatus().getStatus().name());
        dto.setStatusDescription(instance.getStatus().getStatus().getDescription());
        dto.setStartedBy(instance.getStartedBy());
        dto.setStartTime(instance.getStartTime());
        dto.setEndTime(instance.getEndTime());
        dto.setBusinessKey(instance.getBusinessKey());
        dto.setVariables(instance.getVariables());
        if (instance.getParentInstanceId() != null) {
            dto.setParentInstanceId(instance.getParentInstanceId().getValue());
        }
        dto.setDeleteReason(instance.getDeleteReason());
        dto.setCreatedAt(instance.getCreatedAt());
        dto.setUpdatedAt(instance.getUpdatedAt());
        dto.setCreatedBy(instance.getCreatedBy());
        dto.setUpdatedBy(instance.getUpdatedBy());
        return dto;
    }

    private TaskDTO toTaskDTO(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setTaskId(task.getTaskId().getValue());
        dto.setProcessInstanceId(task.getProcessInstanceId().getValue());
        dto.setName(task.getName());
        dto.setDescription(task.getDescription());
        dto.setTaskDefinitionKey(task.getTaskDefinitionKey());
        dto.setAssignee(task.getAssignee());
        dto.setCandidateUsers(task.getCandidateUsers());
        dto.setCandidateGroups(task.getCandidateGroups());
        dto.setOwner(task.getOwner());
        dto.setPriority(task.getPriority());
        dto.setStatus(task.getStatus().name());
        dto.setStatusDescription(task.getStatus().getDescription());
        dto.setDueDate(task.getDueDate());
        dto.setCompleteTime(task.getCompleteTime());
        dto.setVariables(task.getVariables());
        dto.setFormKey(task.getFormKey());
        dto.setDeleteReason(task.getDeleteReason());
        dto.setOverdue(task.isOverdue());
        dto.setCreatedAt(task.getCreatedAt());
        dto.setUpdatedAt(task.getUpdatedAt());
        dto.setCreatedBy(task.getCreatedBy());
        dto.setUpdatedBy(task.getUpdatedBy());
        return dto;
    }
}
