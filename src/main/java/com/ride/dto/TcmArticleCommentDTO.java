package com.ride.dto;

import java.time.LocalDateTime;

/**
 * 文章评论DTO类，包含用户名称信息
 * 
 * @author 千行团队
 * @version 1.0.0
 */
public class TcmArticleCommentDTO {
    
    private Long id;
    private Long articleId; // 文章ID
    private String articleTitle; // 文章标题
    private Long userId; // 评论用户ID
    private String userName; // 评论用户名称
    private String content; // 评论内容
    private Long parentId; // 父评论ID，0表示顶级评论
    private String parentUserName; // 父评论用户名称
    private Integer status; // 状态：0-禁用，1-启用
    private Long likeCount; // 点赞数
    private LocalDateTime createdAt; // 创建时间
    private LocalDateTime updatedAt; // 更新时间
    
    // 无参构造函数
    public TcmArticleCommentDTO() {
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getArticleId() { return articleId; }
    public void setArticleId(Long articleId) { this.articleId = articleId; }
    public String getArticleTitle() { return articleTitle; }
    public void setArticleTitle(String articleTitle) { this.articleTitle = articleTitle; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }
    public String getParentUserName() { return parentUserName; }
    public void setParentUserName(String parentUserName) { this.parentUserName = parentUserName; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Long getLikeCount() { return likeCount; }
    public void setLikeCount(Long likeCount) { this.likeCount = likeCount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "TcmArticleCommentDTO{" +
                "id=" + id +
                ", articleId=" + articleId +
                ", articleTitle='" + articleTitle + '\'' +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", content='" + content + '\'' +
                ", parentId=" + parentId +
                ", status=" + status +
                ", likeCount=" + likeCount +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}