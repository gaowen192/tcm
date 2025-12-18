package com.ride.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 医药论坛帖子修改历史实体类
 * 记录帖子的上一次全部信息，版本号从1开始
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@Entity
@Table(name = "tcm_post_history")
public class TcmPostHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "post_id", nullable = false)
    private Long postId;
    
    @Column(name = "version", nullable = false)
    private Integer version; // 版本号，从1开始
    
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
    
    @Column(name = "status", nullable = false)
    private Integer status = 1; // 0-已删除，1-正常，2-已锁定
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "last_reply_time")
    private LocalDateTime lastReplyTime;
    
    @Column(name = "history_created_at", updatable = false)
    private LocalDateTime historyCreatedAt;
    
    // 无参构造函数
    public TcmPostHistory() {
    }
    
    // 从TcmPost对象创建历史记录的构造函数
    public TcmPostHistory(TcmPost post, Integer version) {
        this.postId = post.getId();
        this.version = version;
        this.title = post.getTitle();
        this.content = post.getContent();
        this.summary = post.getSummary();
        this.userId = post.getUserId();
        this.categoryId = post.getCategoryId();
        this.tags = post.getTags();
        this.viewCount = post.getViewCount();
        this.replyCount = post.getReplyCount();
        this.likeCount = post.getLikeCount();
        this.collectCount = post.getCollectCount();
        this.isTop = post.getIsTop();
        this.isEssence = post.getIsEssence();
        this.isHot = post.getIsHot();
        this.status = post.getStatus();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
        this.lastReplyTime = post.getLastReplyTime();
    }
    
    @PrePersist
    protected void onCreate() {
        historyCreatedAt = LocalDateTime.now();
    }

    // Getter和Setter方法
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPostId() { return postId; }
    public void setPostId(Long postId) { this.postId = postId; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
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
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public LocalDateTime getLastReplyTime() { return lastReplyTime; }
    public void setLastReplyTime(LocalDateTime lastReplyTime) { this.lastReplyTime = lastReplyTime; }
    public LocalDateTime getHistoryCreatedAt() { return historyCreatedAt; }
    public void setHistoryCreatedAt(LocalDateTime historyCreatedAt) { this.historyCreatedAt = historyCreatedAt; }

    // toString方法
    @Override
    public String toString() {
        return "TcmPostHistory{" +
                "id=" + id +
                ", postId=" + postId +
                ", version=" + version +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", summary='" + summary + '\'' +
                ", userId=" + userId +
                ", categoryId=" + categoryId +
                ", tags='" + tags + '\'' +
                ", viewCount=" + viewCount +
                ", replyCount=" + replyCount +
                ", likeCount=" + likeCount +
                ", collectCount=" + collectCount +
                ", isTop=" + isTop +
                ", isEssence=" + isEssence +
                ", isHot=" + isHot +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", lastReplyTime=" + lastReplyTime +
                ", historyCreatedAt=" + historyCreatedAt +
                '}';
    }

    // equals方法
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TcmPostHistory that = (TcmPostHistory) o;
        return java.util.Objects.equals(id, that.id) &&
                java.util.Objects.equals(postId, that.postId) &&
                java.util.Objects.equals(version, that.version) &&
                java.util.Objects.equals(title, that.title) &&
                java.util.Objects.equals(content, that.content) &&
                java.util.Objects.equals(summary, that.summary) &&
                java.util.Objects.equals(userId, that.userId) &&
                java.util.Objects.equals(categoryId, that.categoryId) &&
                java.util.Objects.equals(tags, that.tags) &&
                java.util.Objects.equals(viewCount, that.viewCount) &&
                java.util.Objects.equals(replyCount, that.replyCount) &&
                java.util.Objects.equals(likeCount, that.likeCount) &&
                java.util.Objects.equals(collectCount, that.collectCount) &&
                java.util.Objects.equals(isTop, that.isTop) &&
                java.util.Objects.equals(isEssence, that.isEssence) &&
                java.util.Objects.equals(isHot, that.isHot) &&
                java.util.Objects.equals(status, that.status) &&
                java.util.Objects.equals(createdAt, that.createdAt) &&
                java.util.Objects.equals(updatedAt, that.updatedAt) &&
                java.util.Objects.equals(lastReplyTime, that.lastReplyTime) &&
                java.util.Objects.equals(historyCreatedAt, that.historyCreatedAt);
    }

    // hashCode方法
    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, postId, version, title, content, summary, userId, categoryId, tags, 
                                    viewCount, replyCount, likeCount, collectCount, isTop, isEssence, isHot, 
                                    status, createdAt, updatedAt, lastReplyTime, historyCreatedAt);
    }
}