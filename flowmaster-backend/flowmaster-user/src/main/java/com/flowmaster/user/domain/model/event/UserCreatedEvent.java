package com.flowmaster.user.domain.model.event;

import com.flowmaster.user.domain.model.valueobject.UserId;
import com.flowmaster.user.domain.model.valueobject.Username;
import com.flowmaster.user.domain.model.valueobject.Email;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * 用户创建事件
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Getter
@Slf4j
public class UserCreatedEvent {

    private final UserId userId;
    private final Username username;
    private final Email email;
    private final LocalDateTime occurredAt;

    public UserCreatedEvent(UserId userId, Username username, Email email) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.occurredAt = LocalDateTime.now();
        
        log.info("用户创建事件: userId={}, username={}, email={}", 
                userId.getValue(), username.getValue(), email.getValue());
    }

    @Override
    public String toString() {
        return "UserCreatedEvent{" +
                "userId=" + userId +
                ", username=" + username +
                ", email=" + email +
                ", occurredAt=" + occurredAt +
                '}';
    }
}
