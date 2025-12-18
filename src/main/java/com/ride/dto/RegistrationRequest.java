package com.ride.dto;

import com.ride.validation.AtLeastOneContactRequired;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

/**
 * 用户注册请求DTO
 * 包含用户名、密码、手机号、邮箱等注册信息
 */
@AtLeastOneContactRequired
@Schema(description = "用户注册请求")
public class RegistrationRequest {
    
    /**
     * 用户名（必填）
     */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名长度必须在3-20个字符之间")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "用户名只能包含字母、数字和下划线")
    @Schema(description = "用户名", example = "zhangsan", required = true)
    private String username;
    
    /**
     * 密码（必填）
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d@$!%*?&]+$", 
             message = "密码必须包含大小写字母和数字")
    @Schema(description = "密码", example = "password123", required = true)
    private String password;
    
    /**
     * 确认密码（必填，需与密码一致）
     */
    @NotBlank(message = "确认密码不能为空")
    @Schema(description = "确认密码", example = "password123", required = true)
    private String confirmPassword;
    
    /**
     * 手机号（可选）
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$|^$", message = "请输入正确的手机号格式")
    @Schema(description = "手机号", example = "13800138000", required = false)
    private String phone;
    
    /**
     * 手机验证码（如果提供了手机号则为必填）
     */
    @Pattern(regexp = "^\\d{6}$|^$", message = "手机验证码必须是6位数字")
    @Schema(description = "手机验证码", example = "123456", required = false)
    private String phoneVerificationCode;
    
    /**
     * 邮箱地址（可选）
     */
    @Email(message = "请输入正确的邮箱格式")
    @Size(max = 100, message = "邮箱地址不能超过100个字符")
    @Schema(description = "邮箱地址", example = "user@example.com", required = false)
    private String email;
    
    /**
     * 邮箱验证码（如果提供了邮箱则为必填）
     */
    @Pattern(regexp = "^\\d{6}$|^", message = "邮箱验证码必须是6位数字")
    @Schema(description = "邮箱验证码", example = "123456", required = false)
    private String emailVerificationCode;
    
    /**
     * 用户类型（0-普通用户，1-医生）
     */
    @NotNull(message = "用户类型不能为空")
    @Min(value = 0, message = "用户类型必须为0或1")
    @Max(value = 1, message = "用户类型必须为0或1")
    @Schema(description = "用户类型（0-普通用户，1-医生）", example = "0", required = true)
    private Integer userType;

    // Getter和Setter方法
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoneVerificationCode() {
        return phoneVerificationCode;
    }

    public void setPhoneVerificationCode(String phoneVerificationCode) {
        this.phoneVerificationCode = phoneVerificationCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailVerificationCode() {
        return emailVerificationCode;
    }

    public void setEmailVerificationCode(String emailVerificationCode) {
        this.emailVerificationCode = emailVerificationCode;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    // toString方法
    @Override
    public String toString() {
        return "RegistrationRequest{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                ", phone='" + phone + '\'' +
                ", phoneVerificationCode='" + phoneVerificationCode + '\'' +
                ", email='" + email + '\'' +
                ", emailVerificationCode='" + emailVerificationCode + '\'' +
                ", userType=" + userType +
                '}';
    }

    // equals方法
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegistrationRequest that = (RegistrationRequest) o;
        return java.util.Objects.equals(username, that.username) &&
                java.util.Objects.equals(password, that.password) &&
                java.util.Objects.equals(confirmPassword, that.confirmPassword) &&
                java.util.Objects.equals(phone, that.phone) &&
                java.util.Objects.equals(phoneVerificationCode, that.phoneVerificationCode) &&
                java.util.Objects.equals(email, that.email) &&
                java.util.Objects.equals(emailVerificationCode, that.emailVerificationCode) &&
                java.util.Objects.equals(userType, that.userType);
    }

    // hashCode方法
    @Override
    public int hashCode() {
        return java.util.Objects.hash(username, password, confirmPassword, phone, 
                                    phoneVerificationCode, email, emailVerificationCode, userType);
    }
}