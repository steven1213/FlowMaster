package com.flowmaster.user.application.service;

import com.flowmaster.common.response.PageResult;
import com.flowmaster.common.response.Result;
import com.flowmaster.user.application.command.ChangePasswordCommand;
import com.flowmaster.user.application.command.CreateUserCommand;
import com.flowmaster.user.application.command.UpdateUserCommand;
import com.flowmaster.user.application.dto.UserDTO;
import com.flowmaster.user.application.dto.UserProfileDTO;
import com.flowmaster.user.application.query.UserQuery;
import com.flowmaster.user.domain.model.aggregate.User;
import com.flowmaster.user.domain.model.entity.UserProfile;
import com.flowmaster.user.domain.model.valueobject.Email;
import com.flowmaster.user.domain.model.valueobject.Password;
import com.flowmaster.user.domain.model.valueobject.Phone;
import com.flowmaster.user.domain.model.valueobject.UserId;
import com.flowmaster.user.domain.model.valueobject.Username;
import com.flowmaster.user.domain.model.valueobject.UserStatus;
import com.flowmaster.user.domain.service.UserDomainService;
import com.flowmaster.user.infrastructure.repository.UserRepositoryImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用户应用服务
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserApplicationService {

    private final UserDomainService userDomainService;
    private final UserRepositoryImpl userRepository;

    /**
     * 创建用户
     *
     * @param command 创建用户命令
     * @return 创建结果
     */
    public Result<UserDTO> createUser(CreateUserCommand command) {
        try {
            log.info("开始创建用户: username={}", command.getUsername());

            // 创建值对象
            Username username = Username.of(command.getUsername());
            Email email = command.getEmail() != null ? Email.of(command.getEmail()) : null;
            Phone phone = command.getPhone() != null ? Phone.of(command.getPhone()) : null;
            Password password = Password.of(command.getPassword());

            // 检查用户名是否已存在
            if (userRepository.existsByUsername(username)) {
                return Result.fail("用户名已存在");
            }

            // 检查邮箱是否已存在
            if (email != null && userRepository.existsByEmail(email)) {
                return Result.fail("邮箱已存在");
            }

            // 检查手机号是否已存在
            if (phone != null && userRepository.existsByPhone(phone)) {
                return Result.fail("手机号已存在");
            }

            // 创建用户
            User user = User.create(username, email, phone, password);

            // 创建用户资料
            if (command.getNickname() != null || command.getRealName() != null) {
                UserProfile profile = UserProfile.create(command.getNickname(), command.getRealName(), 1L);
                user.setProfile(profile);
            }

            // 保存用户
            User savedUser = userRepository.save(user);

            // 转换为DTO
            UserDTO userDTO = convertToDTO(savedUser);

            log.info("用户创建成功: userId={}, username={}", savedUser.getUserId().getValue(), savedUser.getUsername().getValue());
            return Result.success(userDTO);

        } catch (Exception e) {
            log.error("创建用户失败: username={}, error={}", command.getUsername(), e.getMessage(), e);
            return Result.fail("创建用户失败: " + e.getMessage());
        }
    }

    /**
     * 更新用户
     *
     * @param command 更新用户命令
     * @return 更新结果
     */
    public Result<UserDTO> updateUser(UpdateUserCommand command) {
        try {
            log.info("开始更新用户: userId={}", command.getUserId());

            // 查找用户
            Optional<User> userOpt = userRepository.findById(UserId.of(command.getUserId()));
            if (userOpt.isEmpty()) {
                return Result.fail("用户不存在");
            }

            User user = userOpt.get();

            // 更新基本信息
            Email email = command.getEmail() != null ? Email.of(command.getEmail()) : null;
            Phone phone = command.getPhone() != null ? Phone.of(command.getPhone()) : null;
            user.updateBasicInfo(email, phone, 1L);

            // 更新用户资料
            if (user.getProfile() != null) {
                UserProfile profile = user.getProfile();
                profile.updateBasicInfo(command.getNickname(), command.getRealName(), command.getBio(), 1L);
                profile.updateAvatar(command.getAvatar(), 1L);
                
                // 更新个人信息
                if (command.getGender() != null || command.getBirthday() != null || command.getAddress() != null) {
                    UserProfile.Gender gender = command.getGender() != null ? 
                        UserProfile.Gender.fromCode(command.getGender()) : null;
                    LocalDate birthday = command.getBirthday() != null ? 
                        LocalDate.parse(command.getBirthday()) : null;
                    profile.updatePersonalInfo(gender, birthday, command.getAddress(), 1L);
                }
            } else if (command.getNickname() != null || command.getRealName() != null) {
                // 创建用户资料
                UserProfile profile = UserProfile.create(command.getNickname(), command.getRealName(), 1L);
                user.setProfile(profile);
            }

            // 保存用户
            User savedUser = userRepository.save(user);

            // 转换为DTO
            UserDTO userDTO = convertToDTO(savedUser);

            log.info("用户更新成功: userId={}", savedUser.getUserId().getValue());
            return Result.success(userDTO);

        } catch (Exception e) {
            log.error("更新用户失败: userId={}, error={}", command.getUserId(), e.getMessage(), e);
            return Result.fail("更新用户失败: " + e.getMessage());
        }
    }

    /**
     * 修改密码
     *
     * @param command 修改密码命令
     * @return 修改结果
     */
    public Result<Void> changePassword(ChangePasswordCommand command) {
        try {
            log.info("开始修改密码: userId={}", command.getUserId());

            // 查找用户
            Optional<User> userOpt = userRepository.findById(UserId.of(command.getUserId()));
            if (userOpt.isEmpty()) {
                return Result.fail("用户不存在");
            }

            User user = userOpt.get();

            // 验证旧密码
            if (!user.verifyPassword(command.getOldPassword())) {
                return Result.fail("旧密码不正确");
            }

            // 修改密码
            Password newPassword = Password.of(command.getNewPassword());
            user.changePassword(newPassword, 1L);

            // 保存用户
            userRepository.save(user);

            log.info("密码修改成功: userId={}", user.getUserId().getValue());
            return Result.success();

        } catch (Exception e) {
            log.error("修改密码失败: userId={}, error={}", command.getUserId(), e.getMessage(), e);
            return Result.fail("修改密码失败: " + e.getMessage());
        }
    }

    /**
     * 激活用户
     *
     * @param userId 用户ID
     * @return 激活结果
     */
    public Result<Void> activateUser(String userId) {
        try {
            log.info("开始激活用户: userId={}", userId);

            // 查找用户
            Optional<User> userOpt = userRepository.findById(UserId.of(userId));
            if (userOpt.isEmpty()) {
                return Result.fail("用户不存在");
            }

            User user = userOpt.get();
            user.activate(1L);

            // 保存用户
            userRepository.save(user);

            log.info("用户激活成功: userId={}", userId);
            return Result.success();

        } catch (Exception e) {
            log.error("激活用户失败: userId={}, error={}", userId, e.getMessage(), e);
            return Result.fail("激活用户失败: " + e.getMessage());
        }
    }

    /**
     * 停用用户
     *
     * @param userId 用户ID
     * @return 停用结果
     */
    public Result<Void> deactivateUser(String userId) {
        try {
            log.info("开始停用用户: userId={}", userId);

            // 查找用户
            Optional<User> userOpt = userRepository.findById(UserId.of(userId));
            if (userOpt.isEmpty()) {
                return Result.fail("用户不存在");
            }

            User user = userOpt.get();
            user.deactivate(1L);

            // 保存用户
            userRepository.save(user);

            log.info("用户停用成功: userId={}", userId);
            return Result.success();

        } catch (Exception e) {
            log.error("停用用户失败: userId={}, error={}", userId, e.getMessage(), e);
            return Result.fail("停用用户失败: " + e.getMessage());
        }
    }

    /**
     * 锁定用户
     *
     * @param userId 用户ID
     * @return 锁定结果
     */
    public Result<Void> lockUser(String userId) {
        try {
            log.info("开始锁定用户: userId={}", userId);

            // 查找用户
            Optional<User> userOpt = userRepository.findById(UserId.of(userId));
            if (userOpt.isEmpty()) {
                return Result.fail("用户不存在");
            }

            User user = userOpt.get();
            user.lock(1L);

            // 保存用户
            userRepository.save(user);

            log.info("用户锁定成功: userId={}", userId);
            return Result.success();

        } catch (Exception e) {
            log.error("锁定用户失败: userId={}, error={}", userId, e.getMessage(), e);
            return Result.fail("锁定用户失败: " + e.getMessage());
        }
    }

    /**
     * 解锁用户
     *
     * @param userId 用户ID
     * @return 解锁结果
     */
    public Result<Void> unlockUser(String userId) {
        try {
            log.info("开始解锁用户: userId={}", userId);

            // 查找用户
            Optional<User> userOpt = userRepository.findById(UserId.of(userId));
            if (userOpt.isEmpty()) {
                return Result.fail("用户不存在");
            }

            User user = userOpt.get();
            user.unlock(1L);

            // 保存用户
            userRepository.save(user);

            log.info("用户解锁成功: userId={}", userId);
            return Result.success();

        } catch (Exception e) {
            log.error("解锁用户失败: userId={}, error={}", userId, e.getMessage(), e);
            return Result.fail("解锁用户失败: " + e.getMessage());
        }
    }

    /**
     * 删除用户
     *
     * @param userId 用户ID
     * @return 删除结果
     */
    public Result<Void> deleteUser(String userId) {
        try {
            log.info("开始删除用户: userId={}", userId);

            // 查找用户
            Optional<User> userOpt = userRepository.findById(UserId.of(userId));
            if (userOpt.isEmpty()) {
                return Result.fail("用户不存在");
            }

            User user = userOpt.get();
            userRepository.deleteById(UserId.of(userId));

            log.info("用户删除成功: userId={}", userId);
            return Result.success();

        } catch (Exception e) {
            log.error("删除用户失败: userId={}, error={}", userId, e.getMessage(), e);
            return Result.fail("删除用户失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID查询用户
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    @Transactional(readOnly = true)
    public Result<UserDTO> getUserById(String userId) {
        try {
            log.debug("查询用户: userId={}", userId);

            Optional<User> userOpt = userRepository.findById(UserId.of(userId));
            if (userOpt.isEmpty()) {
                return Result.fail("用户不存在");
            }

            UserDTO userDTO = convertToDTO(userOpt.get());
            return Result.success(userDTO);

        } catch (Exception e) {
            log.error("查询用户失败: userId={}, error={}", userId, e.getMessage(), e);
            return Result.fail("查询用户失败: " + e.getMessage());
        }
    }

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    @Transactional(readOnly = true)
    public Result<UserDTO> getUserByUsername(String username) {
        try {
            log.debug("根据用户名查询用户: username={}", username);

            Optional<User> userOpt = userRepository.findByUsername(Username.of(username));
            if (userOpt.isEmpty()) {
                return Result.fail("用户不存在");
            }

            UserDTO userDTO = convertToDTO(userOpt.get());
            return Result.success(userDTO);

        } catch (Exception e) {
            log.error("根据用户名查询用户失败: username={}, error={}", username, e.getMessage(), e);
            return Result.fail("查询用户失败: " + e.getMessage());
        }
    }

    /**
     * 分页查询用户
     *
     * @param query 查询条件
     * @return 用户分页列表
     */
    @Transactional(readOnly = true)
    public Result<PageResult<UserDTO>> queryUsers(UserQuery query) {
        try {
            log.debug("分页查询用户: query={}", query);

            // 构建分页参数
            Sort sort = Sort.by(Sort.Direction.fromString(query.getSortDirection()), query.getSortBy());
            Pageable pageable = PageRequest.of(query.getPage(), query.getSize(), sort);

            // TODO: 实现复杂查询逻辑，这里先返回空结果
            PageResult<UserDTO> pageResult = PageResult.of(List.of(), 0L, (long) query.getPage(), (long) query.getSize());

            return Result.success(pageResult);

        } catch (Exception e) {
            log.error("分页查询用户失败: query={}, error={}", query, e.getMessage(), e);
            return Result.fail("查询用户失败: " + e.getMessage());
        }
    }

    /**
     * 将领域对象转换为DTO
     *
     * @param user 用户领域对象
     * @return 用户DTO
     */
    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO()
                .setUserId(String.valueOf(user.getUserId().getValue()))
                .setUsername(user.getUsername().getValue())
                .setEmail(user.getEmail() != null ? user.getEmail().getValue() : null)
                .setPhone(user.getPhone() != null ? user.getPhone().getValue() : null)
                .setStatus(user.getStatus().name())
                .setStatusDescription(user.getStatus().getDescription())
                .setCreatedAt(user.getCreatedAt())
                .setUpdatedAt(user.getUpdatedAt())
                .setCreatedBy(user.getCreatedBy())
                .setUpdatedBy(user.getUpdatedBy())
                .setVersion(user.getVersion());

        // 转换用户资料
        if (user.getProfile() != null) {
            UserProfile profile = user.getProfile();
            UserProfileDTO profileDTO = new UserProfileDTO()
                    .setNickname(profile.getNickname())
                    .setRealName(profile.getRealName())
                    .setAvatar(profile.getAvatar())
                    .setGender(profile.getGender() != null ? profile.getGender().getCode() : null)
                    .setGenderDescription(profile.getGender() != null ? profile.getGender().getDescription() : null)
                    .setBirthday(profile.getBirthday())
                    .setAge(profile.getAge())
                    .setBio(profile.getBio())
                    .setAddress(profile.getAddress())
                    .setCreatedAt(profile.getCreatedAt())
                    .setUpdatedAt(profile.getUpdatedAt());
            dto.setProfile(profileDTO);
        }

        return dto;
    }
}
