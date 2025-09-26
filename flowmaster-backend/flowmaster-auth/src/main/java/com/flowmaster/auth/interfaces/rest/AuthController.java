package com.flowmaster.auth.interfaces.rest;

import com.flowmaster.common.response.PageResult;
import com.flowmaster.common.response.Result;
import com.flowmaster.auth.application.command.LoginCommand;
import com.flowmaster.auth.application.command.LogoutCommand;
import com.flowmaster.auth.application.command.RefreshTokenCommand;
import com.flowmaster.auth.application.dto.AuthResponseDTO;
import com.flowmaster.auth.application.dto.SessionInfoDTO;
import com.flowmaster.auth.application.service.AuthApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 认证授权REST控制器
 *
 * @author FlowMaster Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "认证授权", description = "认证授权相关API")
public class AuthController {

    private final AuthApplicationService authApplicationService;

    @Operation(summary = "用户登录", description = "用户登录获取访问令牌")
    @ApiResponse(responseCode = "200", description = "登录成功")
    @ApiResponse(responseCode = "400", description = "请求参数无效或用户名密码错误")
    @PostMapping("/login")
    public ResponseEntity<Result<AuthResponseDTO>> login(
            @Valid @RequestBody LoginCommand command,
            HttpServletRequest request) {
        
        log.info("用户登录请求: username={}, clientIp={}", command.getUsername(), getClientIp(request));
        
        // 设置客户端信息
        LoginCommand loginCommand = new LoginCommand()
                .setUsername(command.getUsername())
                .setPassword(command.getPassword())
                .setClientIp(getClientIp(request))
                .setUserAgent(request.getHeader("User-Agent"))
                .setRememberMe(command.getRememberMe());
        
        Result<AuthResponseDTO> result = authApplicationService.login(loginCommand);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "刷新令牌", description = "使用刷新令牌获取新的访问令牌")
    @ApiResponse(responseCode = "200", description = "刷新成功")
    @ApiResponse(responseCode = "400", description = "刷新令牌无效或已过期")
    @PostMapping("/refresh")
    public ResponseEntity<Result<AuthResponseDTO>> refreshToken(
            @Valid @RequestBody RefreshTokenCommand command,
            HttpServletRequest request) {
        
        log.info("刷新令牌请求: clientIp={}", getClientIp(request));
        
        // 设置客户端信息
        RefreshTokenCommand refreshCommand = new RefreshTokenCommand()
                .setRefreshToken(command.getRefreshToken())
                .setClientIp(getClientIp(request))
                .setUserAgent(request.getHeader("User-Agent"));
        
        Result<AuthResponseDTO> result = authApplicationService.refreshToken(refreshCommand);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "用户登出", description = "用户登出并撤销令牌")
    @ApiResponse(responseCode = "200", description = "登出成功")
    @ApiResponse(responseCode = "400", description = "请求参数无效")
    @PostMapping("/logout")
    public ResponseEntity<Result<Void>> logout(
            @Valid @RequestBody LogoutCommand command,
            HttpServletRequest request) {
        
        log.info("用户登出请求: clientIp={}", getClientIp(request));
        
        // 设置客户端信息
        LogoutCommand logoutCommand = new LogoutCommand()
                .setAccessToken(command.getAccessToken())
                .setRefreshToken(command.getRefreshToken())
                .setClientIp(getClientIp(request))
                .setUserAgent(request.getHeader("User-Agent"));
        
        Result<Void> result = authApplicationService.logout(logoutCommand);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "验证令牌", description = "验证访问令牌是否有效")
    @ApiResponse(responseCode = "200", description = "令牌有效")
    @ApiResponse(responseCode = "400", description = "令牌无效或已过期")
    @GetMapping("/validate")
    public ResponseEntity<Result<Long>> validateToken(
            @Parameter(description = "访问令牌", required = true)
            @RequestHeader("Authorization") String authorization) {
        
        log.info("验证令牌请求");
        
        // 提取Bearer令牌
        String token = extractBearerToken(authorization);
        if (token == null) {
            return ResponseEntity.ok(Result.fail("令牌格式错误"));
        }
        
        Result<Long> result = authApplicationService.validateToken(token);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "获取用户会话列表", description = "获取当前用户的所有会话")
    @ApiResponse(responseCode = "200", description = "获取成功")
    @ApiResponse(responseCode = "401", description = "未授权")
    @GetMapping("/sessions")
    public ResponseEntity<Result<PageResult<SessionInfoDTO>>> getUserSessions(
            @Parameter(description = "页码，从0开始") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "访问令牌", required = true)
            @RequestHeader("Authorization") String authorization) {
        
        log.info("获取用户会话列表请求: page={}, size={}", page, size);
        
        // 提取Bearer令牌
        String token = extractBearerToken(authorization);
        if (token == null) {
            return ResponseEntity.ok(Result.fail("令牌格式错误"));
        }
        
        // 验证令牌获取用户ID
        Result<Long> validateResult = authApplicationService.validateToken(token);
        if (!validateResult.isSuccess()) {
            return ResponseEntity.ok(Result.fail("令牌验证失败"));
        }
        
        Long userId = validateResult.getData();
        Result<PageResult<SessionInfoDTO>> result = authApplicationService.getUserSessions(userId, page, size);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "撤销用户所有会话", description = "撤销当前用户的所有会话")
    @ApiResponse(responseCode = "200", description = "撤销成功")
    @ApiResponse(responseCode = "401", description = "未授权")
    @DeleteMapping("/sessions")
    public ResponseEntity<Result<Void>> revokeAllUserSessions(
            @Parameter(description = "访问令牌", required = true)
            @RequestHeader("Authorization") String authorization) {
        
        log.info("撤销用户所有会话请求");
        
        // 提取Bearer令牌
        String token = extractBearerToken(authorization);
        if (token == null) {
            return ResponseEntity.ok(Result.fail("令牌格式错误"));
        }
        
        // 验证令牌获取用户ID
        Result<Long> validateResult = authApplicationService.validateToken(token);
        if (!validateResult.isSuccess()) {
            return ResponseEntity.ok(Result.fail("令牌验证失败"));
        }
        
        Long userId = validateResult.getData();
        Result<Void> result = authApplicationService.revokeAllUserSessions(userId);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "清理过期会话", description = "清理系统中的过期会话")
    @ApiResponse(responseCode = "200", description = "清理成功")
    @PostMapping("/cleanup")
    public ResponseEntity<Result<Integer>> cleanupExpiredSessions() {
        log.info("清理过期会话请求");
        
        Result<Integer> result = authApplicationService.cleanupExpiredSessions();
        return ResponseEntity.ok(result);
    }

    /**
     * 获取客户端IP地址
     *
     * @param request HTTP请求
     * @return 客户端IP地址
     */
    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }

    /**
     * 从Authorization头中提取Bearer令牌
     *
     * @param authorization Authorization头值
     * @return 令牌字符串
     */
    private String extractBearerToken(String authorization) {
        if (authorization != null && authorization.startsWith("Bearer ")) {
            return authorization.substring(7);
        }
        return null;
    }
}
