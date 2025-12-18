package com.ride.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 医药论坛回复实体类
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@Entity
@Table(name = "tcm_replies")
public class TcmReply {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "post_id", nullable = false)
    private Long postId;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "parent_id")
    private Long parentId = 0L; // 父回复ID，0为顶级回复
    
    @Column(name = "reply_to_user_id")
    private Long replyToUserId = 0L; // 被回复的用户ID
    
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;
    
    @Column(name = "like_count", nullable = false)
    private Long likeCount = 0L;
    
    @Column(name = "status", nullable = false)
    private Integer status = 1; // 0-已删除，1-正常
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // 无参构造函数
    public TcmReply() {
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
    public Long getPostId() { return postId; }
    public void setPostId(Long postId) { this.postId = postId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }
    public Long getReplyToUserId() { return replyToUserId; }
    public void setReplyToUserId(Long replyToUserId) { this.replyToUserId = replyToUserId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Long getLikeCount() { return likeCount; }
    public void setLikeCount(Long likeCount) { this.likeCount = likeCount; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // toString方法
    @Override
    public String toString() {
        return "TcmReply{" +
                "id=" + id +
                ", postId=" + postId +
                ", userId=" + userId +
                ", parentId=" + parentId +
                ", replyToUserId=" + replyToUserId +
                ", content='" + content + '\'' +
                ", likeCount=" + likeCount +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    // equals方法
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TcmReply tcmReply = (TcmReply) o;
        return java.util.Objects.equals(id, tcmReply.id) &&
                java.util.Objects.equals(postId, tcmReply.postId) &&
                java.util.Objects.equals(userId, tcmReply.userId) &&
                java.util.Objects.equals(parentId, tcmReply.parentId) &&
                java.util.Objects.equals(replyToUserId, tcmReply.replyToUserId) &&
                java.util.Objects.equals(content, tcmReply.content) &&
                java.util.Objects.equals(likeCount, tcmReply.likeCount) &&
                java.util.Objects.equals(status, tcmReply.status) &&
                java.util.Objects.equals(createdAt, tcmReply.createdAt) &&
                java.util.Objects.equals(updatedAt, tcmReply.updatedAt);
    }

    // hashCode方法
    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, postId, userId, parentId, replyToUserId, content, likeCount, 
                                    status, createdAt, updatedAt);
    }
}
