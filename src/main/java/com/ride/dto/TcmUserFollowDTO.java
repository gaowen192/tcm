package com.ride.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * 医药论坛用户关注数据传输对象
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@Schema(description = "用户关注数据传输对象")
public class TcmUserFollowDTO {
    
    @Schema(description = "关注ID", example = "1")
    private Long id;
    
    @Schema(description = "关注者ID", example = "1")
    private Long followerId;
    
    @Schema(description = "被关注者ID", example = "2")
    private Long followingId;
    
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    // 无参构造函数
    public TcmUserFollowDTO() {
    }

    // 全参构造函数
    public TcmUserFollowDTO(Long id, Long followerId, Long followingId, LocalDateTime createdAt) {
        this.id = id;
        this.followerId = followerId;
        this.followingId = followingId;
        this.createdAt = createdAt;
    }

    // Getter和Setter方法
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getFollowerId() { return followerId; }
    public void setFollowerId(Long followerId) { this.followerId = followerId; }
    
    public Long getFollowingId() { return followingId; }
    public void setFollowingId(Long followingId) { this.followingId = followingId; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // toString方法
    @Override
    public String toString() {
        return "TcmUserFollowDTO{" +
                "id=" + id +
                ", followerId=" + followerId +
                ", followingId=" + followingId +
                ", createdAt=" + createdAt +
                '}';
    }

    // equals方法
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TcmUserFollowDTO that = (TcmUserFollowDTO) o;
        return java.util.Objects.equals(id, that.id) &&
                java.util.Objects.equals(followerId, that.followerId) &&
                java.util.Objects.equals(followingId, that.followingId) &&
                java.util.Objects.equals(createdAt, that.createdAt);
    }

    // hashCode方法
    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, followerId, followingId, createdAt);
    }
}