package com.ride.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 医药论坛文章实体类
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@Entity
@Table(name = "tcm_articles")
public class TcmArticle {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "title", nullable = false, length = 200)
    private String title; // 文章标题
    
    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content; // 文章内容
    
    @Column(name = "user_id", nullable = false)
    private Long userId; // 作者ID
    
    @Column(name = "category_id", nullable = false)
    private Long categoryId; // 所属板块ID
    
    @Column(name = "cover_image", length = 500)
    private String coverImage; // 封面图片路径
    
    @Column(name = "tags", length = 500)
    private String tags; // 标签，逗号分隔
    
    @Column(name = "view_count", nullable = false)
    private Long viewCount = 0L; // 阅读次数
    
    @Column(name = "like_count", nullable = false)
    private Long likeCount = 0L; // 点赞次数
    
    @Column(name = "comment_count", nullable = false)
    private Long commentCount = 0L; // 评论次数
    
    @Column(name = "collect_count", nullable = false)
    private Long collectCount = 0L; // 收藏次数
    
    @Column(name = "author_ip", length = 50)
    private String authorIp; // 作者IP地址
    
    @Column(name = "status", nullable = false)
    private Integer status = 1; // 状态：0-禁用，1-启用，2-审核中
    
    @Column(name = "is_hot")
    private Boolean isHot = false; // 是否热门
    
    @Column(name = "is_recommended")
    private Boolean isRecommended = false; // 是否推荐
    
    @Column(name = "is_updated", nullable = false)
    private Integer isUpdated = 0; // 是否更新：0-未更新，1-已更新
    
    @Column(name = "seo_title", length = 200)
    private String seoTitle; // SEO标题
    
    @Column(name = "seo_keywords", length = 500)
    private String seoKeywords; // SEO关键词
    
    @Column(name = "seo_description", length = 500)
    private String seoDescription; // SEO描述
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt; // 创建时间
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // 更新时间
    
    @Column(name = "published_at")
    private LocalDateTime publishedAt; // 发布时间
    
    // 无参构造函数
    public TcmArticle() {
    }
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (publishedAt == null && status == 1) {
            publishedAt = LocalDateTime.now();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        if (status == 1 && publishedAt == null) {
            publishedAt = LocalDateTime.now();
        }
    }

    // Getter和Setter方法
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public String getCoverImage() { return coverImage; }
    public void setCoverImage(String coverImage) { this.coverImage = coverImage; }
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
    public Long getViewCount() { return viewCount; }
    public void setViewCount(Long viewCount) { this.viewCount = viewCount; }
    public Long getLikeCount() { return likeCount; }
    public void setLikeCount(Long likeCount) { this.likeCount = likeCount; }
    public Long getCommentCount() { return commentCount; }
    public void setCommentCount(Long commentCount) { this.commentCount = commentCount; }
    public Long getCollectCount() { return collectCount; }
    public void setCollectCount(Long collectCount) { this.collectCount = collectCount; }
    public String getAuthorIp() { return authorIp; }
    public void setAuthorIp(String authorIp) { this.authorIp = authorIp; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Boolean getIsHot() { return isHot; }
    public void setIsHot(Boolean isHot) { this.isHot = isHot; }
    public Boolean getIsRecommended() { return isRecommended; }
    public void setIsRecommended(Boolean isRecommended) { this.isRecommended = isRecommended; }
    public Integer getIsUpdated() { return isUpdated; }
    public void setIsUpdated(Integer isUpdated) { this.isUpdated = isUpdated; }
    public String getSeoTitle() { return seoTitle; }
    public void setSeoTitle(String seoTitle) { this.seoTitle = seoTitle; }
    public String getSeoKeywords() { return seoKeywords; }
    public void setSeoKeywords(String seoKeywords) { this.seoKeywords = seoKeywords; }
    public String getSeoDescription() { return seoDescription; }
    public void setSeoDescription(String seoDescription) { this.seoDescription = seoDescription; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public LocalDateTime getPublishedAt() { return publishedAt; }
    public void setPublishedAt(LocalDateTime publishedAt) { this.publishedAt = publishedAt; }

    // toString方法
    @Override
    public String toString() {
        return "TcmArticle{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", userId=" + userId +
                ", categoryId=" + categoryId +
                ", viewCount=" + viewCount +
                ", likeCount=" + likeCount +
                ", commentCount=" + commentCount +
                ", status=" + status +
                ", isUpdated=" + isUpdated +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    // equals方法
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TcmArticle that = (TcmArticle) o;
        return java.util.Objects.equals(id, that.id) &&
                java.util.Objects.equals(title, that.title) &&
                java.util.Objects.equals(content, that.content) &&
                java.util.Objects.equals(userId, that.userId) &&
                java.util.Objects.equals(categoryId, that.categoryId) &&
                java.util.Objects.equals(coverImage, that.coverImage) &&
                java.util.Objects.equals(tags, that.tags) &&
                java.util.Objects.equals(viewCount, that.viewCount) &&
                java.util.Objects.equals(likeCount, that.likeCount) &&
                java.util.Objects.equals(commentCount, that.commentCount) &&
                java.util.Objects.equals(collectCount, that.collectCount) &&
                java.util.Objects.equals(authorIp, that.authorIp) &&
                java.util.Objects.equals(status, that.status) &&
                java.util.Objects.equals(isHot, that.isHot) &&
                java.util.Objects.equals(isRecommended, that.isRecommended) &&
                java.util.Objects.equals(isUpdated, that.isUpdated) &&
                java.util.Objects.equals(seoTitle, that.seoTitle) &&
                java.util.Objects.equals(seoKeywords, that.seoKeywords) &&
                java.util.Objects.equals(seoDescription, that.seoDescription) &&
                java.util.Objects.equals(createdAt, that.createdAt) &&
                java.util.Objects.equals(updatedAt, that.updatedAt) &&
                java.util.Objects.equals(publishedAt, that.publishedAt);
    }

    // hashCode方法
    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, title, content, userId, categoryId, coverImage, tags, viewCount, likeCount, 
                commentCount, collectCount, authorIp, status, isHot, isRecommended, isUpdated, seoTitle, seoKeywords, 
                seoDescription, createdAt, updatedAt, publishedAt);
    }
}