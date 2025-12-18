package com.ride.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 医药论坛用户关注实体类
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@Entity
@Table(name = "tcm_user_follows", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"follower_id", "following_id"})
})
public class TcmUserFollow {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "follower_id", nullable = false)
    private Long followerId; // 关注者ID
    
    @Column(name = "following_id", nullable = false)
    private Long followingId; // 被关注者ID
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    // 无参构造函数
    public TcmUserFollow() {
    }
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
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
        return "TcmUserFollow{" +
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
        TcmUserFollow that = (TcmUserFollow) o;
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
