package com.flowmaster.user.domain.model.valueobject;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 邮箱值对象
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Getter
@Slf4j
public class Email {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );
    private static final int MAX_LENGTH = 128;

    private final String value;

    private Email(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("邮箱不能为空");
        }
        
        String trimmedValue = value.trim().toLowerCase();
        if (trimmedValue.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(String.format("邮箱长度不能超过%d个字符", MAX_LENGTH));
        }
        
        if (!EMAIL_PATTERN.matcher(trimmedValue).matches()) {
            throw new IllegalArgumentException("邮箱格式不正确");
        }
        
        this.value = trimmedValue;
    }

    /**
     * 创建邮箱
     *
     * @param value 邮箱值
     * @return 邮箱
     */
    public static Email of(String value) {
        return new Email(value);
    }

    /**
     * 验证邮箱格式
     *
     * @param email 邮箱
     * @return 是否有效
     */
    public static boolean isValid(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        String trimmed = email.trim().toLowerCase();
        return trimmed.length() <= MAX_LENGTH && 
               EMAIL_PATTERN.matcher(trimmed).matches();
    }

    /**
     * 获取邮箱域名
     *
     * @return 域名
     */
    public String getDomain() {
        int atIndex = value.indexOf('@');
        return atIndex > 0 ? value.substring(atIndex + 1) : "";
    }

    /**
     * 获取邮箱用户名部分
     *
     * @return 用户名部分
     */
    public String getLocalPart() {
        int atIndex = value.indexOf('@');
        return atIndex > 0 ? value.substring(0, atIndex) : value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(value, email.value);
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
