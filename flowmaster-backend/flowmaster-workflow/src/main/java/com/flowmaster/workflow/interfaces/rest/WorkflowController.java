package com.flowmaster.workflow.interfaces.rest;

import com.flowmaster.common.response.PageResult;
import com.flowmaster.common.response.Result;
import com.flowmaster.workflow.application.command.*;
import com.flowmaster.workflow.application.dto.ProcessDefinitionDTO;
import com.flowmaster.workflow.application.dto.ProcessInstanceDTO;
import com.flowmaster.workflow.application.dto.TaskDTO;
import com.flowmaster.workflow.application.query.ProcessDefinitionQuery;
import com.flowmaster.workflow.application.query.ProcessInstanceQuery;
import com.flowmaster.workflow.application.query.TaskQuery;
import com.flowmaster.workflow.application.service.WorkflowApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 工作流引擎REST控制器
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/v1/workflow")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "工作流引擎", description = "工作流引擎相关API")
public class WorkflowController {

    private final WorkflowApplicationService workflowApplicationService;

    /**
     * 部署流程定义
     */
    @PostMapping("/process-definitions/deploy")
    @Operation(summary = "部署流程定义", description = "部署新的流程定义到工作流引擎")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "部署成功"),
        @ApiResponse(responseCode = "400", description = "请求参数错误"),
        @ApiResponse(responseCode = "500", description = "系统内部错误")
    })
    public ResponseEntity<Result<ProcessDefinitionDTO>> deployProcessDefinition(
            @Valid @RequestBody DeployProcessDefinitionCommand command) {
        log.info("部署流程定义请求: name={}, key={}", command.getName(), command.getKey());
        Result<ProcessDefinitionDTO> result = workflowApplicationService.deployProcessDefinition(command);
        return ResponseEntity.ok(result);
    }

    /**
     * 启动流程实例
     */
    @PostMapping("/process-instances/start")
    @Operation(summary = "启动流程实例", description = "根据流程定义启动新的流程实例")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "启动成功"),
        @ApiResponse(responseCode = "400", description = "请求参数错误"),
        @ApiResponse(responseCode = "500", description = "系统内部错误")
    })
    public ResponseEntity<Result<ProcessInstanceDTO>> startProcessInstance(
            @Valid @RequestBody StartProcessInstanceCommand command) {
        log.info("启动流程实例请求: processDefinitionKey={}, businessKey={}", 
                command.getProcessDefinitionKey(), command.getBusinessKey());
        Result<ProcessInstanceDTO> result = workflowApplicationService.startProcessInstance(command);
        return ResponseEntity.ok(result);
    }

    /**
     * 完成流程实例
     */
    @PostMapping("/process-instances/{processInstanceId}/complete")
    @Operation(summary = "完成流程实例", description = "完成指定的流程实例")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "完成成功"),
        @ApiResponse(responseCode = "400", description = "请求参数错误"),
        @ApiResponse(responseCode = "500", description = "系统内部错误")
    })
    public ResponseEntity<Result<Void>> completeProcessInstance(
            @Parameter(description = "流程实例ID") @PathVariable String processInstanceId,
            @Valid @RequestBody CompleteProcessInstanceCommand command) {
        log.info("完成流程实例请求: processInstanceId={}", processInstanceId);
        command.setProcessInstanceId(processInstanceId);
        Result<Void> result = workflowApplicationService.completeProcessInstance(command);
        return ResponseEntity.ok(result);
    }

    /**
     * 完成任务
     */
    @PostMapping("/tasks/{taskId}/complete")
    @Operation(summary = "完成任务", description = "完成指定的任务")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "完成成功"),
        @ApiResponse(responseCode = "400", description = "请求参数错误"),
        @ApiResponse(responseCode = "500", description = "系统内部错误")
    })
    public ResponseEntity<Result<Void>> completeTask(
            @Parameter(description = "任务ID") @PathVariable String taskId,
            @Valid @RequestBody CompleteTaskCommand command) {
        log.info("完成任务请求: taskId={}", taskId);
        command.setTaskId(taskId);
        Result<Void> result = workflowApplicationService.completeTask(command);
        return ResponseEntity.ok(result);
    }

    /**
     * 分配任务
     */
    @PostMapping("/tasks/{taskId}/assign")
    @Operation(summary = "分配任务", description = "将任务分配给指定用户")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "分配成功"),
        @ApiResponse(responseCode = "400", description = "请求参数错误"),
        @ApiResponse(responseCode = "500", description = "系统内部错误")
    })
    public ResponseEntity<Result<Void>> assignTask(
            @Parameter(description = "任务ID") @PathVariable String taskId,
            @Valid @RequestBody AssignTaskCommand command) {
        log.info("分配任务请求: taskId={}, assignee={}", taskId, command.getAssignee());
        command.setTaskId(taskId);
        Result<Void> result = workflowApplicationService.assignTask(command);
        return ResponseEntity.ok(result);
    }

    /**
     * 查询流程定义
     */
    @GetMapping("/process-definitions")
    @Operation(summary = "查询流程定义", description = "分页查询流程定义列表")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "400", description = "请求参数错误"),
        @ApiResponse(responseCode = "500", description = "系统内部错误")
    })
    public ResponseEntity<Result<PageResult<ProcessDefinitionDTO>>> queryProcessDefinitions(
            @Parameter(description = "流程定义Key") @RequestParam(required = false) String key,
            @Parameter(description = "流程定义名称") @RequestParam(required = false) String name,
            @Parameter(description = "流程定义分类") @RequestParam(required = false) String category,
            @Parameter(description = "流程定义状态") @RequestParam(required = false) String status,
            @Parameter(description = "是否启用") @RequestParam(required = false) Boolean enabled,
            @Parameter(description = "标签") @RequestParam(required = false) String tags,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.debug("查询流程定义请求: key={}, name={}, category={}, status={}, enabled={}, tags={}", 
                 key, name, category, status, enabled, tags);
        
        ProcessDefinitionQuery query = new ProcessDefinitionQuery();
        query.setKey(key);
        query.setName(name);
        query.setCategory(category);
        query.setStatus(status);
        query.setEnabled(enabled);
        query.setTags(tags);
        query.setPage(pageable.getPageNumber());
        query.setSize(pageable.getPageSize());
        query.setSortBy(pageable.getSort().toString().split(":")[0]);
        query.setSortDirection(pageable.getSort().toString().contains("DESC") ? "desc" : "asc");
        
        Result<PageResult<ProcessDefinitionDTO>> result = workflowApplicationService.queryProcessDefinitions(query);
        return ResponseEntity.ok(result);
    }

    /**
     * 查询流程实例
     */
    @GetMapping("/process-instances")
    @Operation(summary = "查询流程实例", description = "分页查询流程实例列表")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "400", description = "请求参数错误"),
        @ApiResponse(responseCode = "500", description = "系统内部错误")
    })
    public ResponseEntity<Result<PageResult<ProcessInstanceDTO>>> queryProcessInstances(
            @Parameter(description = "流程定义Key") @RequestParam(required = false) String processDefinitionKey,
            @Parameter(description = "流程实例名称") @RequestParam(required = false) String name,
            @Parameter(description = "业务Key") @RequestParam(required = false) String businessKey,
            @Parameter(description = "流程实例状态") @RequestParam(required = false) String status,
            @Parameter(description = "启动人") @RequestParam(required = false) Long startedBy,
            @PageableDefault(size = 20, sort = "startTime", direction = Sort.Direction.DESC) Pageable pageable) {
        log.debug("查询流程实例请求: processDefinitionKey={}, name={}, businessKey={}, status={}, startedBy={}", 
                 processDefinitionKey, name, businessKey, status, startedBy);
        
        ProcessInstanceQuery query = new ProcessInstanceQuery();
        query.setProcessDefinitionKey(processDefinitionKey);
        query.setName(name);
        query.setBusinessKey(businessKey);
        query.setStatus(status);
        query.setStartedBy(startedBy);
        query.setPage(pageable.getPageNumber());
        query.setSize(pageable.getPageSize());
        query.setSortBy(pageable.getSort().toString().split(":")[0]);
        query.setSortDirection(pageable.getSort().toString().contains("DESC") ? "desc" : "asc");
        
        Result<PageResult<ProcessInstanceDTO>> result = workflowApplicationService.queryProcessInstances(query);
        return ResponseEntity.ok(result);
    }

    /**
     * 查询任务
     */
    @GetMapping("/tasks")
    @Operation(summary = "查询任务", description = "分页查询任务列表")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "400", description = "请求参数错误"),
        @ApiResponse(responseCode = "500", description = "系统内部错误")
    })
    public ResponseEntity<Result<PageResult<TaskDTO>>> queryTasks(
            @Parameter(description = "流程实例ID") @RequestParam(required = false) String processInstanceId,
            @Parameter(description = "任务名称") @RequestParam(required = false) String name,
            @Parameter(description = "任务定义Key") @RequestParam(required = false) String taskDefinitionKey,
            @Parameter(description = "指派人") @RequestParam(required = false) Long assignee,
            @Parameter(description = "任务状态") @RequestParam(required = false) String status,
            @Parameter(description = "是否过期") @RequestParam(required = false) Boolean overdue,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.debug("查询任务请求: processInstanceId={}, name={}, taskDefinitionKey={}, assignee={}, status={}, overdue={}", 
                 processInstanceId, name, taskDefinitionKey, assignee, status, overdue);
        
        TaskQuery query = new TaskQuery();
        query.setProcessInstanceId(processInstanceId);
        query.setName(name);
        query.setTaskDefinitionKey(taskDefinitionKey);
        query.setAssignee(assignee);
        query.setStatus(status);
        query.setOverdue(overdue);
        query.setPage(pageable.getPageNumber());
        query.setSize(pageable.getPageSize());
        query.setSortBy(pageable.getSort().toString().split(":")[0]);
        query.setSortDirection(pageable.getSort().toString().contains("DESC") ? "desc" : "asc");
        
        Result<PageResult<TaskDTO>> result = workflowApplicationService.queryTasks(query);
        return ResponseEntity.ok(result);
    }

    /**
     * 获取用户待办任务
     */
    @GetMapping("/tasks/pending/{userId}")
    @Operation(summary = "获取用户待办任务", description = "获取指定用户的待办任务列表")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "400", description = "请求参数错误"),
        @ApiResponse(responseCode = "500", description = "系统内部错误")
    })
    public ResponseEntity<Result<PageResult<TaskDTO>>> getUserPendingTasks(
            @Parameter(description = "用户ID") @PathVariable Long userId,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.debug("获取用户待办任务请求: userId={}", userId);
        Result<PageResult<TaskDTO>> result = workflowApplicationService.getUserPendingTasks(userId, pageable);
        return ResponseEntity.ok(result);
    }
}
