package com.flowmaster.user.domain.model.event;

import com.flowmaster.user.domain.model.valueobject.UserId;
import com.flowmaster.user.domain.model.valueobject.UserStatus;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * 用户状态变更事件
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Getter
@Slf4j
public class UserStatusChangedEvent {

    private final UserId userId;
    private final UserStatus oldStatus;
    private final UserStatus newStatus;
    private final LocalDateTime occurredAt;

    public UserStatusChangedEvent(UserId userId, UserStatus oldStatus, UserStatus newStatus) {
        this.userId = userId;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.occurredAt = LocalDateTime.now();
        
        log.info("用户状态变更事件: userId={}, oldStatus={}, newStatus={}", 
                userId.getValue(), oldStatus, newStatus);
    }

    @Override
    public String toString() {
        return "UserStatusChangedEvent{" +
                "userId=" + userId +
                ", oldStatus=" + oldStatus +
                ", newStatus=" + newStatus +
                ", occurredAt=" + occurredAt +
                '}';
    }
}
