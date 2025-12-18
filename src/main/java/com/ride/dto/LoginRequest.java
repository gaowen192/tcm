package com.ride.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 用户登录请求DTO
 * 包含用户名和密码等登录信息
 */
@Schema(description = "用户登录请求")
public class LoginRequest {
    
    /**
     * 用户名（必填）
     */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度必须在3-50个字符之间")
    @Schema(description = "用户名/手机号/邮箱", example = "zhangsan", required = true)
    private String username; // 可以是用户名、手机号或邮箱
    
    /**
     * 密码（必填）
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间")
    @Schema(description = "密码", example = "password123", required = true)
    private String password;

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

    // toString方法
    @Override
    public String toString() {
        return "LoginRequest{" +
                "username='" + username + '\'' +
                ", password='[PROTECTED]'" +
                '}';
    }

    // equals方法
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginRequest that = (LoginRequest) o;
        return java.util.Objects.equals(username, that.username) &&
                java.util.Objects.equals(password, that.password);
    }

    // hashCode方法
    @Override
    public int hashCode() {
        return java.util.Objects.hash(username, password);
    }
}