package com.ride.dto;

import java.time.LocalDateTime;

/**
 * 医药论坛视频DTO类，包含用户名称信息
 * 
 * @author 千行团队
 * @version 1.0.0
 */

public class TcmVideoDTO {
    
    private Long id;
    private String title; // 视频标题
    private String description; // 视频描述
    private Long userId; // 上传用户ID
    private String userName; // 上传用户名称
    private Long categoryId; // 所属板块ID
    private String filePath; // 视频文件路径
    private String thumbnailPath; // 缩略图路径
    private Integer duration; // 视频时长（秒）
    private Long fileSize; // 文件大小（字节）
    private String format; // 视频格式（如mp4, avi等）
    private String resolution; // 分辨率（如1920x1080）
    private Integer bitrate; // 比特率（kbps）
    private String codec; // 编解码器
    private String tags; // 标签，逗号分隔
    private Long viewCount; // 观看次数
    private Long likeCount; // 点赞次数
    private Long commentCount; // 评论次数
    private Long downloadCount; // 下载次数
    private String uploadIp; // 上传IP地址
    private Integer status; // 状态：0-禁用，1-启用，2-审核中
    private Boolean isHot; // 是否热门
    private Boolean isRecommended; // 是否推荐
    private String playbackUrl; // 视频播放URL（CDN地址）
    private LocalDateTime createdAt; // 创建时间
    private LocalDateTime updatedAt; // 更新时间
    private LocalDateTime publishedAt; // 发布时间

    // 无参构造函数
    public TcmVideoDTO() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public Integer getBitrate() {
        return bitrate;
    }

    public void setBitrate(Integer bitrate) {
        this.bitrate = bitrate;
    }

    public String getCodec() {
        return codec;
    }

    public void setCodec(String codec) {
        this.codec = codec;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Long getViewCount() {
        return viewCount;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }

    public Long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }

    public Long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Long commentCount) {
        this.commentCount = commentCount;
    }

    public Long getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Long downloadCount) {
        this.downloadCount = downloadCount;
    }

    public String getUploadIp() {
        return uploadIp;
    }

    public void setUploadIp(String uploadIp) {
        this.uploadIp = uploadIp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getIsHot() {
        return isHot;
    }

    public void setIsHot(Boolean isHot) {
        this.isHot = isHot;
    }

    public Boolean getIsRecommended() {
        return isRecommended;
    }

    public void setIsRecommended(Boolean isRecommended) {
        this.isRecommended = isRecommended;
    }

    public String getPlaybackUrl() {
        return playbackUrl;
    }

    public void setPlaybackUrl(String playbackUrl) {
        this.playbackUrl = playbackUrl;
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

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    @Override
    public String toString() {
        return "TcmVideoDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", categoryId=" + categoryId +
                ", filePath='" + filePath + '\'' +
                ", thumbnailPath='" + thumbnailPath + '\'' +
                ", duration=" + duration +
                ", fileSize=" + fileSize +
                ", format='" + format + '\'' +
                ", resolution='" + resolution + '\'' +
                ", bitrate=" + bitrate +
                ", codec='" + codec + '\'' +
                ", tags='" + tags + '\'' +
                ", viewCount=" + viewCount +
                ", likeCount=" + likeCount +
                ", commentCount=" + commentCount +
                ", downloadCount=" + downloadCount +
                ", uploadIp='" + uploadIp + '\'' +
                ", status=" + status +
                ", isHot=" + isHot +
                ", isRecommended=" + isRecommended +
                ", playbackUrl='" + playbackUrl + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", publishedAt=" + publishedAt +
                '}';
    }
}