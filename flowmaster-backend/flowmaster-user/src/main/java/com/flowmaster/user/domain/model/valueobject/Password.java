package com.flowmaster.user.domain.model.valueobject;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$"
    );
    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 20;
    
    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    private final String encodedValue;

    private Password(String encodedValue) {
        if (encodedValue == null || encodedValue.trim().isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }
        this.encodedValue = encodedValue;
    }

    /**
     * 创建密码（从明文密码）
     *
     * @param plainPassword 明文密码
     * @return 密码
     */
    public static Password of(String plainPassword) {
        return ofPlainText(plainPassword);
    }

    /**
     * 创建密码（从明文密码）
     *
     * @param plainPassword 明文密码
     * @return 密码
     */
    public static Password ofPlainText(String plainPassword) {
        if (plainPassword == null || plainPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }
        
        String trimmedPassword = plainPassword.trim();
        if (trimmedPassword.length() < MIN_LENGTH || trimmedPassword.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(String.format("密码长度必须在%d-%d个字符之间", MIN_LENGTH, MAX_LENGTH));
        }
        
        if (!PASSWORD_PATTERN.matcher(trimmedPassword).matches()) {
            throw new IllegalArgumentException("密码必须包含大小写字母、数字和特殊字符");
        }
        
        String encodedPassword = PASSWORD_ENCODER.encode(trimmedPassword);
        return new Password(encodedPassword);
    }

    /**
     * 创建密码（从已加密密码）
     *
     * @param encodedPassword 已加密密码
     * @return 密码
     */
    public static Password ofEncoded(String encodedPassword) {
        return new Password(encodedPassword);
    }

    /**
     * 验证密码格式
     *
     * @param password 密码
     * @return 是否有效
     */
    public static boolean isValid(String password) {
        if (password == null || password.trim().isEmpty()) {
            return false;
        }
        
        String trimmed = password.trim();
        return trimmed.length() >= MIN_LENGTH && 
               trimmed.length() <= MAX_LENGTH && 
               PASSWORD_PATTERN.matcher(trimmed).matches();
    }

    /**
     * 验证密码是否匹配
     *
     * @param plainPassword 明文密码
     * @return 是否匹配
     */
    public boolean matches(String plainPassword) {
        if (plainPassword == null) {
            return false;
        }
        return PASSWORD_ENCODER.matches(plainPassword, this.encodedValue);
    }

    /**
     * 获取加密后的密码
     *
     * @return 加密后的密码
     */
    public String getHashedPassword() {
        return this.encodedValue;
    }

    /**
     * 生成随机密码
     *
     * @param length 密码长度
     * @return 随机密码
     */
    public static String generateRandomPassword(int length) {
        if (length < MIN_LENGTH || length > MAX_LENGTH) {
            throw new IllegalArgumentException(String.format("密码长度必须在%d-%d之间", MIN_LENGTH, MAX_LENGTH));
        }
        
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@$!%*?&";
        StringBuilder password = new StringBuilder();
        
        // 确保包含各种字符类型
        password.append("ABCDEFGHIJKLMNOPQRSTUVWXYZ".charAt((int) (Math.random() * 26))); // 大写字母
        password.append("abcdefghijklmnopqrstuvwxyz".charAt((int) (Math.random() * 26))); // 小写字母
        password.append("0123456789".charAt((int) (Math.random() * 10))); // 数字
        password.append("@$!%*?&".charAt((int) (Math.random() * 7))); // 特殊字符
        
        // 填充剩余长度
        for (int i = 4; i < length; i++) {
            password.append(chars.charAt((int) (Math.random() * chars.length())));
        }
        
        // 打乱顺序
        char[] passwordArray = password.toString().toCharArray();
        for (int i = passwordArray.length - 1; i > 0; i--) {
            int j = (int) (Math.random() * (i + 1));
            char temp = passwordArray[i];
            passwordArray[i] = passwordArray[j];
            passwordArray[j] = temp;
        }
        
        return new String(passwordArray);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Password password = (Password) o;
        return Objects.equals(encodedValue, password.encodedValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(encodedValue);
    }

    @Override
    public String toString() {
        return "Password{encodedValue='[PROTECTED]'}";
    }
}
