package com.ride.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * 医药论坛用户角色数据传输对象
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@Schema(description = "用户角色数据传输对象")
public class TcmUserRoleDTO {
    
    @Schema(description = "用户角色ID", example = "1")
    private Long id;
    
    @Schema(description = "用户ID", example = "1")
    private Long userId;
    
    @Schema(description = "角色类型：admin-管理员, moderator-版主", example = "admin")
    private String roleType;
    
    @Schema(description = "板块ID", example = "1")
    private Long boardId;
    
    @Schema(description = "授予者ID", example = "1")
    private Long grantedBy;
    
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
    
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;

    // 无参构造函数
    public TcmUserRoleDTO() {
    }

    // 全参构造函数
    public TcmUserRoleDTO(Long id, Long userId, String roleType, Long boardId, 
                         Long grantedBy, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.roleType = roleType;
        this.boardId = boardId;
        this.grantedBy = grantedBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getter和Setter方法
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public String getRoleType() { return roleType; }
    public void setRoleType(String roleType) { this.roleType = roleType; }
    
    public Long getBoardId() { return boardId; }
    public void setBoardId(Long boardId) { this.boardId = boardId; }
    
    public Long getGrantedBy() { return grantedBy; }
    public void setGrantedBy(Long grantedBy) { this.grantedBy = grantedBy; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // toString方法
    @Override
    public String toString() {
        return "TcmUserRoleDTO{" +
                "id=" + id +
                ", userId=" + userId +
                ", roleType='" + roleType + '\'' +
                ", boardId=" + boardId +
                ", grantedBy=" + grantedBy +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    // equals方法
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TcmUserRoleDTO that = (TcmUserRoleDTO) o;
        return java.util.Objects.equals(id, that.id) &&
                java.util.Objects.equals(userId, that.userId) &&
                java.util.Objects.equals(roleType, that.roleType) &&
                java.util.Objects.equals(boardId, that.boardId) &&
                java.util.Objects.equals(grantedBy, that.grantedBy) &&
                java.util.Objects.equals(createdAt, that.createdAt) &&
                java.util.Objects.equals(updatedAt, that.updatedAt);
    }

    // hashCode方法
    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, userId, roleType, boardId, grantedBy, createdAt, updatedAt);
    }
}