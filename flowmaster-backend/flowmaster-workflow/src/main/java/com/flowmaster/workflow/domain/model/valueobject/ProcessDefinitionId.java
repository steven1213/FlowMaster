package com.flowmaster.workflow.domain.model.valueobject;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 流程定义ID值对象
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Getter
@Slf4j
public class ProcessDefinitionId {

    private static final Pattern ID_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]+$");
    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 64;

    private final String value;

    private ProcessDefinitionId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("流程定义ID不能为空");
        }
        String trimmedValue = value.trim();
        if (trimmedValue.length() < MIN_LENGTH || trimmedValue.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(String.format("流程定义ID长度必须在%d-%d个字符之间", MIN_LENGTH, MAX_LENGTH));
        }
        if (!ID_PATTERN.matcher(trimmedValue).matches()) {
            throw new IllegalArgumentException("流程定义ID只能包含字母、数字、下划线和连字符");
        }
        this.value = trimmedValue;
    }

    /**
     * 创建流程定义ID
     *
     * @param value 流程定义ID值
     * @return 流程定义ID
     */
    public static ProcessDefinitionId of(String value) {
        return new ProcessDefinitionId(value);
    }

    /**
     * 验证流程定义ID是否有效
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
        ProcessDefinitionId that = (ProcessDefinitionId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "ProcessDefinitionId{" +
                "value='" + value + '\'' +
                '}';
    }
}
