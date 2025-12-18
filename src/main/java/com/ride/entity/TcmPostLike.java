package com.ride.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 医药论坛帖子点赞实体类
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@Entity
@Table(name = "tcm_post_likes", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "post_id"})
})
public class TcmPostLike {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "post_id", nullable = false)
    private Long postId;
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    // 无参构造函数
    public TcmPostLike() {
    }
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
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
        return "TcmPostLike{" +
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
        TcmPostLike that = (TcmPostLike) o;
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
