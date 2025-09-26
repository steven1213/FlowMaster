package com.flowmaster.workflow.domain.model.valueobject;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 任务ID值对象
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Getter
@Slf4j
public class TaskId {

    private static final Pattern ID_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]+$");
    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 64;

    private final String value;

    private TaskId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("任务ID不能为空");
        }
        String trimmedValue = value.trim();
        if (trimmedValue.length() < MIN_LENGTH || trimmedValue.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(String.format("任务ID长度必须在%d-%d个字符之间", MIN_LENGTH, MAX_LENGTH));
        }
        if (!ID_PATTERN.matcher(trimmedValue).matches()) {
            throw new IllegalArgumentException("任务ID只能包含字母、数字、下划线和连字符");
        }
        this.value = trimmedValue;
    }

    /**
     * 创建任务ID
     *
     * @param value 任务ID值
     * @return 任务ID
     */
    public static TaskId of(String value) {
        return new TaskId(value);
    }

    /**
     * 验证任务ID是否有效
     *
     * @return 是否有效
     */
    public boolean isValid() {
        return value != null && 
               !value.trim().isEmpty() && 
               value.length() >= MIN_LENGTH && 
               value.length() <= MAX_LENGTH && 
               ID_PATTERN.matcher(value).matches();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskId taskId = (TaskId) o;
        return Objects.equals(value, taskId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "TaskId{" +
                "value='" + value + '\'' +
                '}';
    }
}
