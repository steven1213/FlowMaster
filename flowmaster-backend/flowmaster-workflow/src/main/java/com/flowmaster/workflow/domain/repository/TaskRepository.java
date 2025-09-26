package com.flowmaster.workflow.domain.repository;

import com.flowmaster.workflow.domain.model.entity.Task;
import com.flowmaster.workflow.domain.model.valueobject.ProcessInstanceId;
import com.flowmaster.workflow.domain.model.valueobject.TaskId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 任务仓储接口
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
public interface TaskRepository {

    /**
     * 保存任务
     *
     * @param task 任务
     * @return 保存后的任务
     */
    Task save(Task task);

    /**
     * 根据ID查找任务
     *
     * @param taskId 任务ID
     * @return 任务
     */
    Optional<Task> findById(TaskId taskId);

    /**
     * 根据流程实例ID查找任务
     *
     * @param processInstanceId 流程实例ID
     * @return 任务列表
     */
    List<Task> findByProcessInstanceId(ProcessInstanceId processInstanceId);

    /**
     * 根据指派人查找任务
     *
     * @param assignee 指派人
     * @param pageable 分页参数
     * @return 任务分页结果
     */
    Page<Task> findByAssignee(Long assignee, Pageable pageable);

    /**
     * 查找用户的待办任务
     *
     * @param userId 用户ID
     * @param pageable 分页参数
     * @return 任务分页结果
     */
    Page<Task> findPendingTasksByUser(Long userId, Pageable pageable);

    /**
     * 查找用户的候选任务
     *
     * @param userId 用户ID
     * @param pageable 分页参数
     * @return 任务分页结果
     */
    Page<Task> findCandidateTasksByUser(Long userId, Pageable pageable);

    /**
     * 查找已过期的任务
     *
     * @param currentTime 当前时间
     * @return 任务列表
     */
    List<Task> findOverdueTasks(LocalDateTime currentTime);

    /**
     * 删除任务
     *
     * @param taskId 任务ID
     */
    void deleteById(TaskId taskId);

    /**
     * 查找所有未删除的任务
     *
     * @param pageable 分页参数
     * @return 任务分页结果
     */
    Page<Task> findByDeletedFalse(Pageable pageable);

    /**
     * 统计任务数量
     *
     * @return 任务数量
     */
    long count();
}
