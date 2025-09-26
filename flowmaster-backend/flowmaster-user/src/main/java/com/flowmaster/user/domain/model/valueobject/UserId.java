package com.flowmaster.user.domain.model.valueobject;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 用户ID值对象
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Getter
@Slf4j
public class UserId {

    private static final Pattern USER_ID_PATTERN = Pattern.compile("^[0-9]{1,19}$");

    private final Long value;

    private UserId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("用户ID不能为空或小于等于0");
        }
        this.value = value;
    }

    /**
     * 创建用户ID
     *
     * @param value ID值
     * @return 用户ID
     */
    public static UserId of(Long value) {
        return new UserId(value);
    }

    /**
     * 创建用户ID（从字符串）
     *
     * @param value ID值字符串
     * @return 用户ID
     */
    public static UserId of(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        try {
            return new UserId(Long.parseLong(value.trim()));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("用户ID格式不正确: " + value);
        }
    }

    /**
     * 生成新的用户ID
     *
     * @return 用户ID
     */
    public static UserId generate() {
        // 这里可以使用雪花算法或其他ID生成策略
        long id = System.currentTimeMillis();
        return new UserId(id);
    }

    /**
     * 验证用户ID格式
     *
     * @param userId 用户ID字符串
     * @return 是否有效
     */
    public static boolean isValid(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return false;
        }
        return USER_ID_PATTERN.matcher(userId.trim()).matches();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserId userId = (UserId) o;
        return Objects.equals(value, userId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
