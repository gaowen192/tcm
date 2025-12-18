package com.ride.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * 医药论坛回复数据传输对象
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@Schema(description = "回复数据传输对象")
public class TcmReplyDTO {
    
    @Schema(description = "回复ID", example = "1")
    private Long id;
    
    @Schema(description = "帖子ID", example = "1")
    private Long postId;
    
    @Schema(description = "用户ID", example = "1")
    private Long userId;
    
    @Schema(description = "用户名", example = "user123")
    private String userName;
    
    @Schema(description = "父回复ID", example = "0")
    private Long parentId;
    
    @Schema(description = "被回复用户ID", example = "0")
    private Long replyToUserId;
    
    @Schema(description = "回复内容")
    private String content;
    
    @Schema(description = "点赞数量", example = "0")
    private Integer likeCount;
    
    @Schema(description = "状态：0-禁用，1-启用", example = "1")
    private Integer status;
    
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
    
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;

    // 无参构造函数
    public TcmReplyDTO() {
    }

    // 全参构造函数
    public TcmReplyDTO(Long id, Long postId, Long userId, String userName, Long parentId, Long replyToUserId, 
                       String content, Integer likeCount, Integer status, 
                       LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.userName = userName;
        this.parentId = parentId;
        this.replyToUserId = replyToUserId;
        this.content = content;
        this.likeCount = likeCount;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getter和Setter方法
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getPostId() { return postId; }
    public void setPostId(Long postId) { this.postId = postId; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    
    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }
    
    public Long getReplyToUserId() { return replyToUserId; }
    public void setReplyToUserId(Long replyToUserId) { this.replyToUserId = replyToUserId; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public Integer getLikeCount() { return likeCount; }
    public void setLikeCount(Integer likeCount) { this.likeCount = likeCount; }
    
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // toString方法
    @Override
    public String toString() {
        return "TcmReplyDTO{" +
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
        TcmReplyDTO that = (TcmReplyDTO) o;
        return java.util.Objects.equals(id, that.id) &&
                java.util.Objects.equals(postId, that.postId) &&
                java.util.Objects.equals(userId, that.userId) &&
                java.util.Objects.equals(parentId, that.parentId) &&
                java.util.Objects.equals(replyToUserId, that.replyToUserId) &&
                java.util.Objects.equals(content, that.content) &&
                java.util.Objects.equals(likeCount, that.likeCount) &&
                java.util.Objects.equals(status, that.status) &&
                java.util.Objects.equals(createdAt, that.createdAt) &&
                java.util.Objects.equals(updatedAt, that.updatedAt);
    }

    // hashCode方法
    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, postId, userId, parentId, replyToUserId, 
                                    content, likeCount, status, createdAt, updatedAt);
    }
}