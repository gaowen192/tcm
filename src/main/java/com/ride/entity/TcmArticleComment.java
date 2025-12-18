package com.ride.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 文章评论实体类
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@Entity
@Table(name = "tcm_article_comment")
public class TcmArticleComment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "article_id", nullable = false)
    private Long articleId; // 文章ID
    
    @Column(name = "user_id", nullable = false)
    private Long userId; // 评论用户ID
    
    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content; // 评论内容
    
    @Column(name = "parent_id", nullable = false)
    private Long parentId = 0L; // 父评论ID，0表示顶级评论
    
    @Column(name = "status", nullable = false)
    private Integer status = 1; // 状态：0-禁用，1-启用
    
    @Column(name = "like_count", nullable = false)
    private Long likeCount = 0L; // 点赞数
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt; // 创建时间
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // 更新时间
    
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt; // 删除时间
    
    // 无参构造函数
    public TcmArticleComment() {
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
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Long getLikeCount() { return likeCount; }
    public void setLikeCount(Long likeCount) { this.likeCount = likeCount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public LocalDateTime getDeletedAt() { return deletedAt; }
    public void setDeletedAt(LocalDateTime deletedAt) { this.deletedAt = deletedAt; }

    // toString方法
    @Override
    public String toString() {
        return "TcmArticleComment{" +
                "id=" + id +
                ", articleId=" + articleId +
                ", userId=" + userId +
                ", content='" + content + '\'' +
                ", parentId=" + parentId +
                ", status=" + status +
                ", likeCount=" + likeCount +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    // equals方法
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TcmArticleComment that = (TcmArticleComment) o;
        return java.util.Objects.equals(id, that.id) &&
                java.util.Objects.equals(articleId, that.articleId) &&
                java.util.Objects.equals(userId, that.userId) &&
                java.util.Objects.equals(content, that.content) &&
                java.util.Objects.equals(parentId, that.parentId) &&
                java.util.Objects.equals(status, that.status) &&
                java.util.Objects.equals(likeCount, that.likeCount) &&
                java.util.Objects.equals(createdAt, that.createdAt) &&
                java.util.Objects.equals(updatedAt, that.updatedAt) &&
                java.util.Objects.equals(deletedAt, that.deletedAt);
    }

    // hashCode方法
    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, articleId, userId, content, parentId, status, likeCount, createdAt, updatedAt, deletedAt);
    }
}