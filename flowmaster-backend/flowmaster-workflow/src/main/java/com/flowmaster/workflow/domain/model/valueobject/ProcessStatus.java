package com.flowmaster.workflow.domain.model.valueobject;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * 流程状态值对象
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Getter
@Slf4j
public class ProcessStatus {

    private final Status status;

    private ProcessStatus(Status status) {
        if (status == null) {
            throw new IllegalArgumentException("流程状态不能为空");
        }
        this.status = status;
    }

    /**
     * 创建流程状态
     *
     * @param status 状态枚举
     * @return 流程状态
     */
    public static ProcessStatus of(Status status) {
        return new ProcessStatus(status);
    }

    /**
     * 创建流程状态
     *
     * @param statusName 状态名称
     * @return 流程状态
     */
    public static ProcessStatus of(String statusName) {
        try {
            Status status = Status.valueOf(statusName.toUpperCase());
            return new ProcessStatus(status);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("无效的流程状态: " + statusName);
        }
    }

    /**
     * 验证流程状态是否有效
     *
     * @return 是否有效
     */
    public boolean isValid() {
        return status != null;
    }

    /**
     * 是否已完成
     *
     * @return 是否已完成
     */
    public boolean isCompleted() {
        return status == Status.COMPLETED;
    }

    /**
     * 是否已取消
     *
     * @return 是否已取消
     */
    public boolean isCancelled() {
        return status == Status.CANCELLED;
    }

    /**
     * 是否正在运行
     *
     * @return 是否正在运行
     */
    public boolean isRunning() {
        return status == Status.RUNNING;
    }

    /**
     * 是否已暂停
     *
     * @return 是否已暂停
     */
    public boolean isSuspended() {
        return status == Status.SUSPENDED;
    }

    /**
     * 流程状态枚举
     */
    public enum Status {
        RUNNING("运行中"),
        COMPLETED("已完成"),
        CANCELLED("已取消"),
        SUSPENDED("已暂停");

        private final String description;

        Status(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProcessStatus that = (ProcessStatus) o;
        return status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(status);
    }

    @Override
    public String toString() {
        return "ProcessStatus{" +
                "status=" + status +
                '}';
    }
}
