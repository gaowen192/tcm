package com.ride.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 文章互动实体类（点赞和观看记录）
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@Entity
@Table(name = "tcm_article_interaction", uniqueConstraints = {
    @UniqueConstraint(name = "uk_user_article", columnNames = {"userId", "articleId"})
})
public class TcmArticleInteraction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "article_id", nullable = false)
    private Long articleId; // 文章ID
    
    @Column(name = "user_id", nullable = false)
    private Long userId; // 用户ID
    
    @Column(name = "is_liked", nullable = false, columnDefinition = "tinyint(1) default 0")
    private Boolean isLiked = false; // 是否点赞：false-未点赞，true-已点赞
    
    @Column(name = "liked_at")
    private LocalDateTime likedAt; // 点赞时间
    
    @Column(name = "viewed_at")
    private LocalDateTime viewedAt; // 观看时间
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt; // 创建时间
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // 更新时间
    
    // 无参构造函数
    public TcmArticleInteraction() {
    }
    
    // 有参构造函数
    public TcmArticleInteraction(Long articleId, Long userId) {
        this.articleId = articleId;
        this.userId = userId;
    }
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getter和Setter方法
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getArticleId() { return articleId; }
    public void setArticleId(Long articleId) { this.articleId = articleId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Boolean getIsLiked() { return isLiked; }
    public void setIsLiked(Boolean isLiked) { this.isLiked = isLiked; }
    public LocalDateTime getLikedAt() { return likedAt; }
    public void setLikedAt(LocalDateTime likedAt) { this.likedAt = likedAt; }
    public LocalDateTime getViewedAt() { return viewedAt; }
    public void setViewedAt(LocalDateTime viewedAt) { this.viewedAt = viewedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // toString方法
    @Override
    public String toString() {
        return "TcmArticleInteraction{" +
                "id=" + id +
                ", articleId=" + articleId +
                ", userId=" + userId +
                ", isLiked=" + isLiked +
                ", likedAt=" + likedAt +
                ", viewedAt=" + viewedAt +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    // equals方法
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TcmArticleInteraction that = (TcmArticleInteraction) o;
        return java.util.Objects.equals(id, that.id) &&
                java.util.Objects.equals(articleId, that.articleId) &&
                java.util.Objects.equals(userId, that.userId) &&
                java.util.Objects.equals(isLiked, that.isLiked) &&
                java.util.Objects.equals(likedAt, that.likedAt) &&
                java.util.Objects.equals(viewedAt, that.viewedAt) &&
                java.util.Objects.equals(createdAt, that.createdAt) &&
                java.util.Objects.equals(updatedAt, that.updatedAt);
    }

    // hashCode方法
    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, articleId, userId, isLiked, likedAt, viewedAt, createdAt, updatedAt);
    }
}