package com.ride.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 医药论坛视频实体类
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@Entity
@Table(name = "tcm_videos")
public class TcmVideo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "title", nullable = false, length = 200)
    private String title; // 视频标题
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description; // 视频描述
    
    @Column(name = "user_id", nullable = false)
    private Long userId; // 上传用户ID
    
    @Column(name = "category_id", nullable = false)
    private Long categoryId; // 所属板块ID
    
    @Column(name = "file_path", nullable = false, length = 500)
    private String filePath; // 视频文件路径
    
    @Column(name = "thumbnail_path", length = 500)
    private String thumbnailPath; // 缩略图路径
    
    @Column(name = "duration")
    private Integer duration; // 视频时长（秒）
    
    @Column(name = "file_size")
    private Long fileSize; // 文件大小（字节）
    
    @Column(name = "format", length = 20)
    private String format; // 视频格式（如mp4, avi等）
    
    @Column(name = "resolution", length = 50)
    private String resolution; // 分辨率（如1920x1080）
    
    @Column(name = "bitrate")
    private Integer bitrate; // 比特率（kbps）
    
    @Column(name = "codec", length = 50)
    private String codec; // 编解码器
    
    @Column(name = "tags", length = 500)
    private String tags; // 标签，逗号分隔
    
    @Column(name = "view_count", nullable = false)
    private Long viewCount = 0L; // 观看次数
    
    @Column(name = "like_count", nullable = false)
    private Long likeCount = 0L; // 点赞次数
    
    @Column(name = "comment_count", nullable = false)
    private Long commentCount = 0L; // 评论次数
    
    @Column(name = "download_count", nullable = false)
    private Long downloadCount = 0L; // 下载次数
    
    @Column(name = "upload_ip", length = 50)
    private String uploadIp; // 上传IP地址
    
    @Column(name = "status", nullable = false)
    private Integer status = 1; // 状态：0-禁用，1-启用，2-审核中
    
    @Column(name = "is_hot")
    private Boolean isHot = false; // 是否热门
    
    @Column(name = "is_recommended")
    private Boolean isRecommended = false; // 是否推荐
    
    @Column(name = "playback_url", length = 500)
    private String playbackUrl; // 视频播放URL（CDN地址）
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt; // 创建时间
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // 更新时间
    
    @Column(name = "published_at")
    private LocalDateTime publishedAt; // 发布时间
    
    // 无参构造函数
    public TcmVideo() {
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
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public String getThumbnailPath() { return thumbnailPath; }
    public void setThumbnailPath(String thumbnailPath) { this.thumbnailPath = thumbnailPath; }
    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }
    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }
    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }
    public String getResolution() { return resolution; }
    public void setResolution(String resolution) { this.resolution = resolution; }
    public Integer getBitrate() { return bitrate; }
    public void setBitrate(Integer bitrate) { this.bitrate = bitrate; }
    public String getCodec() { return codec; }
    public void setCodec(String codec) { this.codec = codec; }
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
    public Long getViewCount() { return viewCount; }
    public void setViewCount(Long viewCount) { this.viewCount = viewCount; }
    public Long getLikeCount() { return likeCount; }
    public void setLikeCount(Long likeCount) { this.likeCount = likeCount; }
    public Long getCommentCount() { return commentCount; }
    public void setCommentCount(Long commentCount) { this.commentCount = commentCount; }
    public Long getDownloadCount() { return downloadCount; }
    public void setDownloadCount(Long downloadCount) { this.downloadCount = downloadCount; }
    public String getUploadIp() { return uploadIp; }
    public void setUploadIp(String uploadIp) { this.uploadIp = uploadIp; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Boolean getIsHot() { return isHot; }
    public void setIsHot(Boolean isHot) { this.isHot = isHot; }
    public Boolean getIsRecommended() { return isRecommended; }
    public void setIsRecommended(Boolean isRecommended) { this.isRecommended = isRecommended; }
    public String getPlaybackUrl() { return playbackUrl; }
    public void setPlaybackUrl(String playbackUrl) { this.playbackUrl = playbackUrl; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public LocalDateTime getPublishedAt() { return publishedAt; }
    public void setPublishedAt(LocalDateTime publishedAt) { this.publishedAt = publishedAt; }

    // toString方法
    @Override
    public String toString() {
        return "TcmVideo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", userId=" + userId +
                ", categoryId=" + categoryId +
                ", duration=" + duration +
                ", fileSize=" + fileSize +
                ", format='" + format + '\'' +
                ", resolution='" + resolution + '\'' +
                ", viewCount=" + viewCount +
                ", likeCount=" + likeCount +
                ", commentCount=" + commentCount +
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
        TcmVideo that = (TcmVideo) o;
        return java.util.Objects.equals(id, that.id) &&
                java.util.Objects.equals(title, that.title) &&
                java.util.Objects.equals(description, that.description) &&
                java.util.Objects.equals(userId, that.userId) &&
                java.util.Objects.equals(categoryId, that.categoryId) &&
                java.util.Objects.equals(filePath, that.filePath) &&
                java.util.Objects.equals(thumbnailPath, that.thumbnailPath) &&
                java.util.Objects.equals(duration, that.duration) &&
                java.util.Objects.equals(fileSize, that.fileSize) &&
                java.util.Objects.equals(format, that.format) &&
                java.util.Objects.equals(resolution, that.resolution) &&
                java.util.Objects.equals(bitrate, that.bitrate) &&
                java.util.Objects.equals(codec, that.codec) &&
                java.util.Objects.equals(tags, that.tags) &&
                java.util.Objects.equals(viewCount, that.viewCount) &&
                java.util.Objects.equals(likeCount, that.likeCount) &&
                java.util.Objects.equals(commentCount, that.commentCount) &&
                java.util.Objects.equals(downloadCount, that.downloadCount) &&
                java.util.Objects.equals(uploadIp, that.uploadIp) &&
                java.util.Objects.equals(status, that.status) &&
                java.util.Objects.equals(isHot, that.isHot) &&
                java.util.Objects.equals(isRecommended, that.isRecommended) &&
                java.util.Objects.equals(playbackUrl, that.playbackUrl) &&
                java.util.Objects.equals(createdAt, that.createdAt) &&
                java.util.Objects.equals(updatedAt, that.updatedAt) &&
                java.util.Objects.equals(publishedAt, that.publishedAt);
    }

    // hashCode方法
    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, title, description, userId, categoryId, filePath, thumbnailPath, 
                duration, fileSize, format, resolution, bitrate, codec, tags, viewCount, likeCount, 
                commentCount, downloadCount, uploadIp, status, isHot, isRecommended, playbackUrl, 
                createdAt, updatedAt, publishedAt);
    }
}