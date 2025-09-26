package com.flowmaster.user.domain.model.entity;

import com.flowmaster.common.domain.BaseDomainEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 用户资料实体
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Getter
@Setter
@Slf4j
public class UserProfile extends BaseDomainEntity {

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 性别
     */
    private Gender gender;

    /**
     * 生日
     */
    private LocalDate birthday;

    /**
     * 个人简介
     */
    private String bio;

    /**
     * 地址
     */
    private String address;

    /**
     * 私有构造函数
     */
    private UserProfile() {
        super();
    }

    /**
     * 创建用户资料
     *
     * @param nickname 昵称
     * @param realName 真实姓名
     * @param createdBy 创建人ID
     * @return 用户资料
     */
    public static UserProfile create(String nickname, String realName, Long createdBy) {
        UserProfile profile = new UserProfile();
        profile.nickname = nickname;
        profile.realName = realName;
        
        // 调用父类的创建方法
        profile.create(createdBy);
        
        return profile;
    }

    /**
     * 更新基本信息
     *
     * @param nickname 昵称
     * @param realName 真实姓名
     * @param bio      个人简介
     * @param updatedBy 更新人ID
     */
    public void updateBasicInfo(String nickname, String realName, String bio, Long updatedBy) {
        this.nickname = nickname;
        this.realName = realName;
        this.bio = bio;
        
        // 调用父类的更新方法
        this.update(updatedBy);
        
        log.info("更新用户基本信息: nickname={}, realName={}", nickname, realName);
    }

    /**
     * 更新头像
     *
     * @param avatar 头像URL
     * @param updatedBy 更新人ID
     */
    public void updateAvatar(String avatar, Long updatedBy) {
        this.avatar = avatar;
        
        // 调用父类的更新方法
        this.update(updatedBy);
        
        log.info("更新用户头像: avatar={}", avatar);
    }

    /**
     * 更新个人信息
     *
     * @param gender   性别
     * @param birthday 生日
     * @param address  地址
     * @param updatedBy 更新人ID
     */
    public void updatePersonalInfo(Gender gender, LocalDate birthday, String address, Long updatedBy) {
        this.gender = gender;
        this.birthday = birthday;
        this.address = address;
        
        // 调用父类的更新方法
        this.update(updatedBy);
        
        log.info("更新用户个人信息: gender={}, birthday={}, address={}", gender, birthday, address);
    }

    /**
     * 获取年龄
     *
     * @return 年龄
     */
    public Integer getAge() {
        if (birthday == null) {
            return null;
        }
        
        LocalDate now = LocalDate.now();
        int age = now.getYear() - birthday.getYear();
        
        if (now.getMonthValue() < birthday.getMonthValue() ||
            (now.getMonthValue() == birthday.getMonthValue() && now.getDayOfMonth() < birthday.getDayOfMonth())) {
            age--;
        }
        
        return age;
    }

    /**
     * 性别枚举
     */
    public enum Gender {
        MALE("MALE", "男"),
        FEMALE("FEMALE", "女"),
        OTHER("OTHER", "其他"),
        UNKNOWN("UNKNOWN", "未知");

        private final String code;
        private final String description;

        Gender(String code, String description) {
            this.code = code;
            this.description = description;
        }

        public static Gender fromCode(String code) {
            if (code == null) {
                return UNKNOWN;
            }
            
            for (Gender gender : values()) {
                if (gender.code.equals(code)) {
                    return gender;
                }
            }
            
            return UNKNOWN;
        }

        public String getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return code;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserProfile that = (UserProfile) o;
        return Objects.equals(nickname, that.nickname) &&
               Objects.equals(realName, that.realName) &&
               Objects.equals(avatar, that.avatar) &&
               gender == that.gender &&
               Objects.equals(birthday, that.birthday) &&
               Objects.equals(bio, that.bio) &&
               Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nickname, realName, avatar, gender, birthday, bio, address);
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "nickname='" + nickname + '\'' +
                ", realName='" + realName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", gender=" + gender +
                ", birthday=" + birthday +
                ", bio='" + bio + '\'' +
                ", address='" + address + '\'' +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                '}';
    }
}
