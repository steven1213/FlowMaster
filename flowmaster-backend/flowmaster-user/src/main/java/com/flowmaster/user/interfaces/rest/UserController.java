package com.flowmaster.user.interfaces.rest;

import com.flowmaster.common.response.PageResult;
import com.flowmaster.common.response.Result;
import com.flowmaster.user.application.command.ChangePasswordCommand;
import com.flowmaster.user.application.command.CreateUserCommand;
import com.flowmaster.user.application.command.UpdateUserCommand;
import com.flowmaster.user.application.dto.UserDTO;
import com.flowmaster.user.application.query.UserQuery;
import com.flowmaster.user.application.service.UserApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理REST控制器
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "用户管理", description = "用户管理相关API")
public class UserController {

    private final UserApplicationService userApplicationService;

    /**
     * 创建用户
     *
     * @param command 创建用户命令
     * @return 创建结果
     */
    @PostMapping
    @Operation(summary = "创建用户", description = "创建新用户")
    public ResponseEntity<Result<UserDTO>> createUser(@Valid @RequestBody CreateUserCommand command) {
        log.info("创建用户请求: username={}", command.getUsername());
        Result<UserDTO> result = userApplicationService.createUser(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /**
     * 根据ID获取用户
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    @GetMapping("/{userId}")
    @Operation(summary = "获取用户", description = "根据用户ID获取用户信息")
    public ResponseEntity<Result<UserDTO>> getUserById(
            @Parameter(description = "用户ID", required = true)
            @PathVariable String userId) {
        log.info("获取用户请求: userId={}", userId);
        Result<UserDTO> result = userApplicationService.getUserById(userId);
        return ResponseEntity.ok(result);
    }

    /**
     * 根据用户名获取用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    @GetMapping("/username/{username}")
    @Operation(summary = "根据用户名获取用户", description = "根据用户名获取用户信息")
    public ResponseEntity<Result<UserDTO>> getUserByUsername(
            @Parameter(description = "用户名", required = true)
            @PathVariable String username) {
        log.info("根据用户名获取用户请求: username={}", username);
        Result<UserDTO> result = userApplicationService.getUserByUsername(username);
        return ResponseEntity.ok(result);
    }

    /**
     * 更新用户
     *
     * @param userId  用户ID
     * @param command 更新用户命令
     * @return 更新结果
     */
    @PutMapping("/{userId}")
    @Operation(summary = "更新用户", description = "更新用户信息")
    public ResponseEntity<Result<UserDTO>> updateUser(
            @Parameter(description = "用户ID", required = true)
            @PathVariable String userId,
            @Valid @RequestBody UpdateUserCommand command) {
        log.info("更新用户请求: userId={}", userId);
        // 设置用户ID到命令中
        UpdateUserCommand updatedCommand = new UpdateUserCommand()
                .setUserId(userId)
                .setEmail(command.getEmail())
                .setPhone(command.getPhone())
                .setNickname(command.getNickname())
                .setRealName(command.getRealName())
                .setAvatar(command.getAvatar())
                .setGender(command.getGender())
                .setBirthday(command.getBirthday())
                .setBio(command.getBio())
                .setAddress(command.getAddress());
        
        Result<UserDTO> result = userApplicationService.updateUser(updatedCommand);
        return ResponseEntity.ok(result);
    }

    /**
     * 修改密码
     *
     * @param userId  用户ID
     * @param command 修改密码命令
     * @return 修改结果
     */
    @PutMapping("/{userId}/password")
    @Operation(summary = "修改密码", description = "修改用户密码")
    public ResponseEntity<Result<Void>> changePassword(
            @Parameter(description = "用户ID", required = true)
            @PathVariable String userId,
            @Valid @RequestBody ChangePasswordCommand command) {
        log.info("修改密码请求: userId={}", userId);
        // 设置用户ID到命令中
        ChangePasswordCommand updatedCommand = new ChangePasswordCommand()
                .setUserId(userId)
                .setOldPassword(command.getOldPassword())
                .setNewPassword(command.getNewPassword());
        
        Result<Void> result = userApplicationService.changePassword(updatedCommand);
        return ResponseEntity.ok(result);
    }

    /**
     * 激活用户
     *
     * @param userId 用户ID
     * @return 操作结果
     */
    @PutMapping("/{userId}/activate")
    @Operation(summary = "激活用户", description = "激活用户账户")
    public ResponseEntity<Result<Void>> activateUser(
            @Parameter(description = "用户ID", required = true)
            @PathVariable String userId) {
        log.info("激活用户请求: userId={}", userId);
        Result<Void> result = userApplicationService.activateUser(userId);
        return ResponseEntity.ok(result);
    }

    /**
     * 停用用户
     *
     * @param userId 用户ID
     * @return 操作结果
     */
    @PutMapping("/{userId}/deactivate")
    @Operation(summary = "停用用户", description = "停用用户账户")
    public ResponseEntity<Result<Void>> deactivateUser(
            @Parameter(description = "用户ID", required = true)
            @PathVariable String userId) {
        log.info("停用用户请求: userId={}", userId);
        Result<Void> result = userApplicationService.deactivateUser(userId);
        return ResponseEntity.ok(result);
    }

    /**
     * 锁定用户
     *
     * @param userId 用户ID
     * @return 操作结果
     */
    @PutMapping("/{userId}/lock")
    @Operation(summary = "锁定用户", description = "锁定用户账户")
    public ResponseEntity<Result<Void>> lockUser(
            @Parameter(description = "用户ID", required = true)
            @PathVariable String userId) {
        log.info("锁定用户请求: userId={}", userId);
        Result<Void> result = userApplicationService.lockUser(userId);
        return ResponseEntity.ok(result);
    }

    /**
     * 解锁用户
     *
     * @param userId 用户ID
     * @return 操作结果
     */
    @PutMapping("/{userId}/unlock")
    @Operation(summary = "解锁用户", description = "解锁用户账户")
    public ResponseEntity<Result<Void>> unlockUser(
            @Parameter(description = "用户ID", required = true)
            @PathVariable String userId) {
        log.info("解锁用户请求: userId={}", userId);
        Result<Void> result = userApplicationService.unlockUser(userId);
        return ResponseEntity.ok(result);
    }

    /**
     * 删除用户
     *
     * @param userId 用户ID
     * @return 操作结果
     */
    @DeleteMapping("/{userId}")
    @Operation(summary = "删除用户", description = "逻辑删除用户")
    public ResponseEntity<Result<Void>> deleteUser(
            @Parameter(description = "用户ID", required = true)
            @PathVariable String userId) {
        log.info("删除用户请求: userId={}", userId);
        Result<Void> result = userApplicationService.deleteUser(userId);
        return ResponseEntity.ok(result);
    }

    /**
     * 分页查询用户
     *
     * @param username     用户名关键字
     * @param email        邮箱关键字
     * @param phone        手机号关键字
     * @param status       用户状态
     * @param page         页码
     * @param size         每页大小
     * @param sortBy       排序字段
     * @param sortDirection 排序方向
     * @return 分页结果
     */
    @GetMapping
    @Operation(summary = "分页查询用户", description = "分页查询用户列表")
    public ResponseEntity<Result<PageResult<UserDTO>>> searchUsers(
            @Parameter(description = "用户名关键字")
            @RequestParam(required = false) String username,
            @Parameter(description = "邮箱关键字")
            @RequestParam(required = false) String email,
            @Parameter(description = "手机号关键字")
            @RequestParam(required = false) String phone,
            @Parameter(description = "用户状态")
            @RequestParam(required = false) String status,
            @Parameter(description = "页码", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "排序字段", example = "createdAt")
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "排序方向", example = "desc")
            @RequestParam(defaultValue = "desc") String sortDirection) {
        
        log.info("分页查询用户请求: username={}, email={}, phone={}, status={}, page={}, size={}", 
                username, email, phone, status, page, size);
        
        // 构建分页参数
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        // 构建查询对象
        UserQuery query = new UserQuery()
                .setUsername(username)
                .setEmail(email)
                .setPhone(phone)
                .setStatus(status)
                .setPage(page)
                .setSize(size)
                .setSortBy(sortBy)
                .setSortDirection(sortDirection);
        
        Result<PageResult<UserDTO>> result = userApplicationService.queryUsers(query);
        return ResponseEntity.ok(result);
    }
}
