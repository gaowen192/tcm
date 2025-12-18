package com.ride.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * 用户登录响应DTO
 * 包含登录成功后的用户信息和令牌等
 */
@Schema(description = "用户登录响应")
public class LoginResponse {
    
    @Schema(description = "用户ID", example = "1")
    private Long userId;
    
    @Schema(description = "用户名", example = "zhangsan")
    private String username;
    
    @Schema(description = "用户昵称/真实姓名", example = "张三")
    private String realName;
    
    @Schema(description = "用户头像", example = "https://example.com/avatar.jpg")
    private String avatar;
    
    @Schema(description = "用户角色", example = "USER")
    private String role;
    
    @Schema(description = "访问令牌", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String accessToken;
    
    @Schema(description = "令牌过期时间")
    private LocalDateTime tokenExpireTime;
    
    @Schema(description = "用户状态(0-禁用，1-正常)", example = "1", allowableValues = {"0", "1"})
    private Integer status;
    
    @Schema(description = "用户类型(0-普通用户，1-医生)", example = "0", allowableValues = {"0", "1"})
    private Integer userType;

    // Getter和Setter方法
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public LocalDateTime getTokenExpireTime() {
        return tokenExpireTime;
    }

    public void setTokenExpireTime(LocalDateTime tokenExpireTime) {
        this.tokenExpireTime = tokenExpireTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
        return "LoginResponse{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", realName='" + realName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", role='" + role + '\'' +
                ", accessToken='[PROTECTED]'" +
                ", tokenExpireTime=" + tokenExpireTime +
                ", status=" + status +
                ", userType=" + userType +
                '}';
    }
}
