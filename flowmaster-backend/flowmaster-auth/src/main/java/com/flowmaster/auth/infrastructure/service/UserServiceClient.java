package com.flowmaster.auth.infrastructure.service;

import com.flowmaster.auth.domain.model.valueobject.Password;
import com.flowmaster.auth.domain.model.valueobject.Username;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户服务客户端
 * 用于与用户管理服务通信，验证用户凭据
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceClient {

    private final RestTemplate restTemplate;
    private final PasswordService passwordService;

    @Value("${user.service.url:http://localhost:8081/user-service}")
    private String userServiceUrl;

    /**
     * 验证用户凭据
     *
     * @param username 用户名
     * @param password 密码
     * @return 用户ID，验证失败返回null
     */
    public Long validateCredentials(Username username, Password password) {
        try {
            log.debug("验证用户凭据: username={}", username.getValue());

            // 构建请求
            Map<String, Object> request = new HashMap<>();
            request.put("username", username.getValue());
            request.put("password", password.getEncodedValue());

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

            // 调用用户服务验证接口
            String url = userServiceUrl + "/api/v1/users/validate-credentials";
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> body = response.getBody();
                Boolean success = (Boolean) body.get("success");
                if (success != null && success) {
                    Map<String, Object> data = (Map<String, Object>) body.get("data");
                    if (data != null && data.containsKey("userId")) {
                        Long userId = Long.valueOf(data.get("userId").toString());
                        log.debug("用户凭据验证成功: username={}, userId={}", username.getValue(), userId);
                        return userId;
                    }
                }
            }

            log.warn("用户凭据验证失败: username={}", username.getValue());
            return null;

        } catch (Exception e) {
            log.error("用户凭据验证异常: username={}, error={}", username.getValue(), e.getMessage(), e);
            return null;
        }
    }

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    public Map<String, Object> getUserByUsername(Username username) {
        try {
            log.debug("获取用户信息: username={}", username.getValue());

            String url = userServiceUrl + "/api/v1/users/username/" + username.getValue();
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> body = response.getBody();
                Boolean success = (Boolean) body.get("success");
                if (success != null && success) {
                    Map<String, Object> data = (Map<String, Object>) body.get("data");
                    log.debug("获取用户信息成功: username={}", username.getValue());
                    return data;
                }
            }

            log.warn("获取用户信息失败: username={}", username.getValue());
            return null;

        } catch (Exception e) {
            log.error("获取用户信息异常: username={}, error={}", username.getValue(), e.getMessage(), e);
            return null;
        }
    }

    /**
     * 验证用户密码
     *
     * @param username 用户名
     * @param rawPassword 原始密码
     * @return 用户ID，验证失败返回null
     */
    public Long validateUserPassword(Username username, String rawPassword) {
        try {
            // 获取用户信息
            Map<String, Object> userInfo = getUserByUsername(username);
            if (userInfo == null) {
                return null;
            }

            // 获取用户密码哈希
            String passwordHash = (String) userInfo.get("password");
            if (passwordHash == null) {
                log.warn("用户密码哈希为空: username={}", username.getValue());
                return null;
            }

            // 验证密码
            boolean matches = passwordService.matchesPassword(rawPassword, passwordHash);
            if (matches) {
                Long userId = Long.valueOf(userInfo.get("userId").toString());
                log.debug("用户密码验证成功: username={}, userId={}", username.getValue(), userId);
                return userId;
            } else {
                log.warn("用户密码验证失败: username={}", username.getValue());
                return null;
            }

        } catch (Exception e) {
            log.error("用户密码验证异常: username={}, error={}", username.getValue(), e.getMessage(), e);
            return null;
        }
    }
}
