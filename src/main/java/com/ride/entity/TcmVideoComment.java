package com.ride.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 医药论坛视频评论实体类
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@Entity
@Table(name = "tcm_video_comments")
public class TcmVideoComment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "video_id", nullable = false)
    private Long videoId; // 关联的视频ID
    
    @Column(name = "user_id", nullable = false)
    private Long userId; // 评论用户ID
    
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content; // 评论内容
    
    @Column(name = "reply_to_comment_id")
    private Long replyToCommentId; // 回复的评论ID，用于嵌套回复
    
    @Column(name = "status", nullable = false)
    private Integer status = 1; // 状态：0-禁用，1-正常，2-审核中
    
    @Column(name = "like_count", nullable = false)
    private Long likeCount = 0L; // 点赞次数
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt; // 创建时间
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // 更新时间
    
    // 无参构造函数
    public TcmVideoComment() {
    }
    
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) {
            createdAt = now;
        }
        if (updatedAt == null) {
            updatedAt = now;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // getter and setter methods
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getVideoId() {
        return videoId;
    }
    
    public void setVideoId(Long videoId) {
        this.videoId = videoId;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public Long getReplyToCommentId() {
        return replyToCommentId;
    }
    
    public void setReplyToCommentId(Long replyToCommentId) {
        this.replyToCommentId = replyToCommentId;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public Long getLikeCount() {
        return likeCount;
    }
    
    public void setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @Override
    public String toString() {
        return "TcmVideoComment{" +
                "id=" + id +
                ", videoId=" + videoId +
                ", userId=" + userId +
                ", content='" + content + '\'' +
                ", replyToCommentId=" + replyToCommentId +
                ", status=" + status +
                ", likeCount=" + likeCount +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        TcmVideoComment that = (TcmVideoComment) o;
        
        return id != null ? id.equals(that.id) : that.id == null;
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}