package com.flowmaster.user.domain.model.valueobject;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 手机号值对象
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Getter
@Slf4j
public class Phone {

    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");
    private static final Pattern INTERNATIONAL_PHONE_PATTERN = Pattern.compile("^\\+?[1-9]\\d{1,14}$");

    private final String value;

    private Phone(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("手机号不能为空");
        }
        
        String trimmedValue = value.trim();
        
        // 支持中国手机号和国际手机号
        if (!PHONE_PATTERN.matcher(trimmedValue).matches() && 
            !INTERNATIONAL_PHONE_PATTERN.matcher(trimmedValue).matches()) {
            throw new IllegalArgumentException("手机号格式不正确");
        }
        
        this.value = trimmedValue;
    }

    /**
     * 创建手机号
     *
     * @param value 手机号值
     * @return 手机号
     */
    public static Phone of(String value) {
        return new Phone(value);
    }

    /**
     * 验证手机号格式
     *
     * @param phone 手机号
     * @return 是否有效
     */
    public static boolean isValid(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        
        String trimmed = phone.trim();
        return PHONE_PATTERN.matcher(trimmed).matches() || 
               INTERNATIONAL_PHONE_PATTERN.matcher(trimmed).matches();
    }

    /**
     * 是否为中国手机号
     *
     * @return 是否为中国手机号
     */
    public boolean isChinesePhone() {
        return PHONE_PATTERN.matcher(value).matches();
    }

    /**
     * 获取手机号运营商
     *
     * @return 运营商
     */
    public String getCarrier() {
        if (!isChinesePhone()) {
            return "未知";
        }
        
        String prefix = value.substring(0, 3);
        switch (prefix) {
            case "130": case "131": case "132": case "133": case "134": case "135": 
            case "136": case "137": case "138": case "139": case "147": case "148": 
            case "150": case "151": case "152": case "153": case "155": case "156": 
            case "157": case "158": case "159": case "172": case "178": case "182": 
            case "183": case "184": case "187": case "188": case "195": case "197": 
            case "198":
                return "中国移动";
            case "145": case "146": case "166": case "167": case "171": case "175": 
            case "176": case "185": case "186": case "196":
                return "中国联通";
            case "149": case "173": case "174": case "177": case "180": case "181": 
            case "189": case "191": case "193": case "199":
                return "中国电信";
            default:
                return "未知";
        }
    }

    /**
     * 脱敏显示
     *
     * @return 脱敏后的手机号
     */
    public String getMaskedValue() {
        if (value.length() < 7) {
            return value;
        }
        
        if (isChinesePhone()) {
            return value.substring(0, 3) + "****" + value.substring(7);
        } else {
            return value.substring(0, 3) + "****" + value.substring(value.length() - 3);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Phone phone = (Phone) o;
        return Objects.equals(value, phone.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
