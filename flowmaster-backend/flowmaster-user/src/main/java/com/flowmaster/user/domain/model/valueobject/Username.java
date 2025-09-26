package com.flowmaster.user.domain.model.valueobject;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 用户名字值对象
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Getter
@Slf4j
public class Username {

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{3,20}$");
    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 20;

    private final String value;

    private Username(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("用户名不能为空");
        }
        
        String trimmedValue = value.trim();
        if (trimmedValue.length() < MIN_LENGTH || trimmedValue.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(String.format("用户名长度必须在%d-%d个字符之间", MIN_LENGTH, MAX_LENGTH));
        }
        
        if (!USERNAME_PATTERN.matcher(trimmedValue).matches()) {
            throw new IllegalArgumentException("用户名只能包含字母、数字和下划线");
        }
        
        this.value = trimmedValue;
    }

    /**
     * 创建用户名
     *
     * @param value 用户名值
     * @return 用户名
     */
    public static Username of(String value) {
        return new Username(value);
    }

    /**
     * 验证用户名格式
     *
     * @param username 用户名
     * @return 是否有效
     */
    public static boolean isValid(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        
        String trimmed = username.trim();
        return trimmed.length() >= MIN_LENGTH && 
               trimmed.length() <= MAX_LENGTH && 
               USERNAME_PATTERN.matcher(trimmed).matches();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Username username = (Username) o;
        return Objects.equals(value, username.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
