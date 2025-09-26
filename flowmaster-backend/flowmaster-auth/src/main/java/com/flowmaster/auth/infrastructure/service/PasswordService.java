package com.flowmaster.auth.infrastructure.service;

import com.flowmaster.auth.domain.model.valueobject.Password;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 密码加密服务实现
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Service
@Slf4j
public class PasswordService {

    private final PasswordEncoder passwordEncoder;

    public PasswordService(@Value("${security.password.encoder.strength:12}") int strength) {
        this.passwordEncoder = new BCryptPasswordEncoder(strength);
    }

    /**
     * 加密密码
     *
     * @param password 原始密码
     * @return 加密后的密码
     */
    public Password encryptPassword(Password password) {
        try {
            String encodedPassword = passwordEncoder.encode(password.getEncodedValue());
            log.debug("密码加密成功");
            return Password.ofEncoded(encodedPassword);
        } catch (Exception e) {
            log.error("密码加密失败: {}", e.getMessage(), e);
            throw new RuntimeException("密码加密失败", e);
        }
    }

    /**
     * 验证密码
     *
     * @param rawPassword 原始密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    public boolean matchesPassword(String rawPassword, String encodedPassword) {
        try {
            boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);
            log.debug("密码验证结果: {}", matches);
            return matches;
        } catch (Exception e) {
            log.error("密码验证异常: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 验证密码
     *
     * @param rawPassword 原始密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    public boolean matchesPassword(Password rawPassword, Password encodedPassword) {
        return matchesPassword(rawPassword.getEncodedValue(), encodedPassword.getEncodedValue());
    }
}
