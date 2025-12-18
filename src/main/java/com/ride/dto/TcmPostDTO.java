package com.ride.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * 医药论坛帖子数据传输对象
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@Schema(description = "帖子数据传输对象")
public class TcmPostDTO {
    
    @Schema(description = "帖子ID", example = "1")
    private Long id;
    
    @Schema(description = "帖子标题", example = "中医药在现代医学中的应用")
    private String title;
    
    @Schema(description = "帖子内容")
    private String content;
    
    @Schema(description = "帖子摘要", example = "本文将探讨中医药在现代医学中的应用现状...")
    private String summary;
    
    @Schema(description = "用户ID", example = "1")
    private Long userId;
    
    @Schema(description = "板块ID", example = "1")
    private Long categoryId;
    
    @Schema(description = "标签", example = "中医药,现代医学,治疗")
    private String tags;
    
    @Schema(description = "浏览数量", example = "0")
    private Long viewCount;
    
    @Schema(description = "回复数量", example = "0")
    private Long replyCount;
    
    @Schema(description = "点赞数量", example = "0")
    private Long likeCount;
    
    @Schema(description = "收藏数量", example = "0")
    private Long collectCount;
    
    @Schema(description = "是否置顶：0-否，1-是", example = "0")
    private Integer isTop;
    
    @Schema(description = "是否精华：0-否，1-是", example = "0")
    private Integer isEssence;
    
    @Schema(description = "是否热门：0-否，1-是", example = "0")
    private Integer isHot;
    
    @Schema(description = "是否更新：0-未更新，1-已更新", example = "0")
    private Integer isUpdated;
    
    @Schema(description = "状态：0-已删除，1-正常，2-已锁定", example = "1")
    private Integer status;
    
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
    
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;
    
    @Schema(description = "最后回复时间")
    private LocalDateTime lastReplyTime;

    // 无参构造函数
    public TcmPostDTO() {
    }

    // 全参构造函数
    public TcmPostDTO(Long id, String title, String content, String summary, Long userId, Long categoryId, 
                     String tags, Long viewCount, Long replyCount, Long likeCount, Long collectCount, 
                     Integer isTop, Integer isEssence, Integer isHot, Integer isUpdated, Integer status, 
                     LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime lastReplyTime) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.summary = summary;
        this.userId = userId;
        this.categoryId = categoryId;
        this.tags = tags;
        this.viewCount = viewCount;
        this.replyCount = replyCount;
        this.likeCount = likeCount;
        this.collectCount = collectCount;
        this.isTop = isTop;
        this.isEssence = isEssence;
        this.isHot = isHot;
        this.isUpdated = isUpdated;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.lastReplyTime = lastReplyTime;
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
        return "TcmPostDTO{" +
                "id=" + id +
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
        TcmPostDTO that = (TcmPostDTO) o;
        return java.util.Objects.equals(id, that.id) &&
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
                java.util.Objects.equals(isUpdated, that.isUpdated) &&
                java.util.Objects.equals(status, that.status) &&
                java.util.Objects.equals(createdAt, that.createdAt) &&
                java.util.Objects.equals(updatedAt, that.updatedAt) &&
                java.util.Objects.equals(lastReplyTime, that.lastReplyTime);
    }

    // hashCode方法
    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, title, content, summary, userId, categoryId, tags, viewCount, 
                                    replyCount, likeCount, collectCount, isTop, isEssence, isHot, isUpdated, status, 
                                    createdAt, updatedAt, lastReplyTime);
    }
}