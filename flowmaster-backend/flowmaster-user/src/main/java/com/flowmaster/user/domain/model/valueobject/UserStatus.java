package com.flowmaster.user.domain.model.valueobject;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * 用户状态枚举
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Getter
@Slf4j
public enum UserStatus {

    /**
     * 激活状态
     */
    ACTIVE("ACTIVE", "激活"),

    /**
     * 停用状态
     */
    INACTIVE("INACTIVE", "停用"),

    /**
     * 锁定状态
     */
    LOCKED("LOCKED", "锁定"),

    /**
     * 待激活状态
     */
    PENDING("PENDING", "待激活");

    private final String code;
    private final String description;

    UserStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 根据代码获取状态
     *
     * @param code 状态代码
     * @return 用户状态
     */
    public static UserStatus fromCode(String code) {
        if (code == null) {
            return null;
        }
        
        for (UserStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        
        throw new IllegalArgumentException("无效的用户状态代码: " + code);
    }

    /**
     * 检查状态是否有效
     *
     * @param code 状态代码
     * @return 是否有效
     */
    public static boolean isValid(String code) {
        if (code == null) {
            return false;
        }
        
        for (UserStatus status : values()) {
            if (status.code.equals(code)) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * 是否可以登录
     *
     * @return 是否可以登录
     */
    public boolean canLogin() {
        return this == ACTIVE;
    }

    /**
     * 是否可以操作
     *
     * @return 是否可以操作
     */
    public boolean canOperate() {
        return this == ACTIVE || this == PENDING;
    }

    /**
     * 是否需要激活
     *
     * @return 是否需要激活
     */
    public boolean needsActivation() {
        return this == PENDING;
    }

    @Override
    public String toString() {
        return code;
    }
}
