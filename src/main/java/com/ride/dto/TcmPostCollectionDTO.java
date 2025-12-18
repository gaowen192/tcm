package com.ride.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * 医药论坛帖子收藏数据传输对象
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@Schema(description = "帖子收藏数据传输对象")
public class TcmPostCollectionDTO {
    
    @Schema(description = "收藏ID", example = "1")
    private Long id;
    
    @Schema(description = "用户ID", example = "1")
    private Long userId;
    
    @Schema(description = "帖子ID", example = "1")
    private Long postId;
    
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    // 无参构造函数
    public TcmPostCollectionDTO() {
    }

    // 全参构造函数
    public TcmPostCollectionDTO(Long id, Long userId, Long postId, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.postId = postId;
        this.createdAt = createdAt;
    }

    // Getter和Setter方法
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public Long getPostId() { return postId; }
    public void setPostId(Long postId) { this.postId = postId; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // toString方法
    @Override
    public String toString() {
        return "TcmPostCollectionDTO{" +
                "id=" + id +
                ", userId=" + userId +
                ", postId=" + postId +
                ", createdAt=" + createdAt +
                '}';
    }

    // equals方法
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TcmPostCollectionDTO that = (TcmPostCollectionDTO) o;
        return java.util.Objects.equals(id, that.id) &&
                java.util.Objects.equals(userId, that.userId) &&
                java.util.Objects.equals(postId, that.postId) &&
                java.util.Objects.equals(createdAt, that.createdAt);
    }

    // hashCode方法
    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, userId, postId, createdAt);
    }
}