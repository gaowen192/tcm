package com.ride.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 医药论坛帖子实体类
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@Entity
@Table(name = "tcm_posts")
public class TcmPost {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "title", nullable = false, length = 200)
    private String title;
    
    @Column(name = "content", nullable = false, columnDefinition = "LONGTEXT")
    private String content;
    
    @Column(name = "summary", length = 500)
    private String summary;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "category_id", nullable = false)
    private Long categoryId;
    
    @Column(name = "tags", length = 500)
    private String tags;
    
    @Column(name = "view_count", nullable = false)
    private Long viewCount = 0L;
    
    @Column(name = "reply_count", nullable = false)
    private Long replyCount = 0L;
    
    @Column(name = "like_count", nullable = false)
    private Long likeCount = 0L;
    
    @Column(name = "collect_count", nullable = false)
    private Long collectCount = 0L;
    
    @Column(name = "is_top", nullable = false)
    private Integer isTop = 0; // 0-否，1-是
    
    @Column(name = "is_essence", nullable = false)
    private Integer isEssence = 0; // 0-否，1-是
    
    @Column(name = "is_hot", nullable = false)
    private Integer isHot = 0; // 0-否，1-是
    
    @Column(name = "is_updated", nullable = false)
    private Integer isUpdated = 0; // 0-未更新，1-已更新
    
    @Column(name = "status", nullable = false)
    private Integer status = 1; // 0-已删除，1-正常，2-已锁定
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "last_reply_time")
    private LocalDateTime lastReplyTime;
    
    // 无参构造函数
    public TcmPost() {
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
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
    public Long getViewCount() { return viewCount; }
    public void setViewCount(Long viewCount) { this.viewCount = viewCount; }
    public Long getReplyCount() { return replyCount; }
    public void setReplyCount(Long replyCount) { this.replyCount = replyCount; }
    public Long getLikeCount() { return likeCount; }
    public void setLikeCount(Long likeCount) { this.likeCount = likeCount; }
    public Long getCollectCount() { return collectCount; }
    public void setCollectCount(Long collectCount) { this.collectCount = collectCount; }
    public Integer getIsTop() { return isTop; }
    public void setIsTop(Integer isTop) { this.isTop = isTop; }
    public Integer getIsEssence() { return isEssence; }
    public void setIsEssence(Integer isEssence) { this.isEssence = isEssence; }
    public Integer getIsHot() { return isHot; }
    public void setIsHot(Integer isHot) { this.isHot = isHot; }
    public Integer getIsUpdated() { return isUpdated; }
    public void setIsUpdated(Integer isUpdated) { this.isUpdated = isUpdated; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public LocalDateTime getLastReplyTime() { return lastReplyTime; }
    public void setLastReplyTime(LocalDateTime lastReplyTime) { this.lastReplyTime = lastReplyTime; }

    // toString方法
    @Override
    public String toString() {
        return "TcmPost{" +
                "id=" + id +
                ", title='" + title + "'" +
                ", content='" + content + "'" +
                ", summary='" + summary + "'" +
                ", userId=" + userId +
                ", categoryId=" + categoryId +
                ", tags='" + tags + "'" +
                ", viewCount=" + viewCount +
                ", replyCount=" + replyCount +
                ", likeCount=" + likeCount +
                ", collectCount=" + collectCount +
                ", isTop=" + isTop +
                ", isEssence=" + isEssence +
                ", isHot=" + isHot +
                ", isUpdated=" + isUpdated +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", lastReplyTime=" + lastReplyTime +
                '}';
    }

    // equals方法
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TcmPost tcmPost = (TcmPost) o;
        return java.util.Objects.equals(id, tcmPost.id) &&
                java.util.Objects.equals(title, tcmPost.title) &&
                java.util.Objects.equals(content, tcmPost.content) &&
                java.util.Objects.equals(summary, tcmPost.summary) &&
                java.util.Objects.equals(userId, tcmPost.userId) &&
                java.util.Objects.equals(categoryId, tcmPost.categoryId) &&
                java.util.Objects.equals(tags, tcmPost.tags) &&
                java.util.Objects.equals(viewCount, tcmPost.viewCount) &&
                java.util.Objects.equals(replyCount, tcmPost.replyCount) &&
                java.util.Objects.equals(likeCount, tcmPost.likeCount) &&
                java.util.Objects.equals(collectCount, tcmPost.collectCount) &&
                java.util.Objects.equals(isTop, tcmPost.isTop) &&
                java.util.Objects.equals(isEssence, tcmPost.isEssence) &&
                java.util.Objects.equals(isHot, tcmPost.isHot) &&
                java.util.Objects.equals(isUpdated, tcmPost.isUpdated) &&
                java.util.Objects.equals(status, tcmPost.status) &&
                java.util.Objects.equals(createdAt, tcmPost.createdAt) &&
                java.util.Objects.equals(updatedAt, tcmPost.updatedAt) &&
                java.util.Objects.equals(lastReplyTime, tcmPost.lastReplyTime);
    }

    // hashCode方法
    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, title, content, summary, userId, categoryId, tags, viewCount, 
                                    replyCount, likeCount, collectCount, isTop, isEssence, isHot, isUpdated, status, 
                                    createdAt, updatedAt, lastReplyTime);
    }
}