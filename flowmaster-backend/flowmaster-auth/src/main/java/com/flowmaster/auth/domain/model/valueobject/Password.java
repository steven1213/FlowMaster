package com.flowmaster.auth.domain.model.valueobject;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 密码值对象
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Getter
@Slf4j
public class Password {

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 128;

    private String value;

    private Password(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }
        String trimmedValue = value.trim();
        if (trimmedValue.length() < MIN_LENGTH || trimmedValue.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(String.format("密码长度必须在%d-%d个字符之间", MIN_LENGTH, MAX_LENGTH));
        }
        if (!PASSWORD_PATTERN.matcher(trimmedValue).matches()) {
            throw new IllegalArgumentException("密码必须包含至少一个小写字母、一个大写字母、一个数字和一个特殊字符");
        }
        this.value = trimmedValue;
    }

    /**
     * 创建密码
     *
     * @param value 密码值
     * @return 密码
     */
    public static Password of(String value) {
        return new Password(value);
    }

    /**
     * 创建已编码的密码
     *
     * @param encodedValue 已编码的密码值
     * @return 密码
     */
    public static Password ofEncoded(String encodedValue) {
        if (encodedValue == null || encodedValue.trim().isEmpty()) {
            throw new IllegalArgumentException("已编码密码不能为空");
        }
        Password password = new Password("dummy"); // 临时创建，用于存储编码值
        password.value = encodedValue.trim();
        return password;
    }

    /**
     * 验证密码是否有效
     *
     * @return 是否有效
     */
    public boolean isValid() {
        return value != null && 
               !value.trim().isEmpty() && 
               value.length() >= MIN_LENGTH && 
               value.length() <= MAX_LENGTH && 
               PASSWORD_PATTERN.matcher(value).matches();
    }

    /**
     * 获取已编码的密码
     *
     * @return 已编码的密码
     */
    public String getEncodedValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Password password = (Password) o;
        return Objects.equals(value, password.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Password{" +
                "value='[PROTECTED]'" +
                '}';
    }
}
