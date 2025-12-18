package com.ride.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 系统通知实体类
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@Entity
@Table(name = "tcm_notifications")
public class TcmNotification {
    
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "type", nullable = false, length = 50)
    private String type;
    
    @Column(name = "title", nullable = false, length = 255)
    private String title;
    
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;
    
    @Column(name = "related_id")
    private Long relatedId;

    @Column(name = "related_type", length = 50)
    private String relatedType;

    @Column(name = "from_user_id", nullable = false)
    private Long fromUserId;
    
    @Column(name = "is_read")
    private Boolean isRead;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "read_at")
    private LocalDateTime readAt;
    
    // 无参构造函数
    public TcmNotification() {
    }
    
    /**
     * 自动设置创建时间
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        // 默认未读
        if (isRead == null) {
            isRead = false;
        }
    }
    
    // Getter和Setter方法
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Long getRelatedId() { return relatedId; }
    public void setRelatedId(Long relatedId) { this.relatedId = relatedId; }
    public String getRelatedType() { return relatedType; }
    public void setRelatedType(String relatedType) { this.relatedType = relatedType; }
    public Long getFromUserId() { return fromUserId; }
    public void setFromUserId(Long fromUserId) { this.fromUserId = fromUserId; }
    public Boolean getIsRead() { return isRead; }
    public void setIsRead(Boolean isRead) { this.isRead = isRead; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getReadAt() { return readAt; }
    public void setReadAt(LocalDateTime readAt) { this.readAt = readAt; }
    
    // toString方法
    @Override
    public String toString() {
        return "TcmNotification{" +
                "id=" + id +
                ", userId=" + userId +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", relatedId=" + relatedId +
                ", relatedType='" + relatedType + '\'' +
                ", fromUserId=" + fromUserId +
                ", isRead=" + isRead +
                ", createdAt=" + createdAt +
                ", readAt=" + readAt +
                '}';
    }
    
    // equals方法
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TcmNotification that = (TcmNotification) o;
        return java.util.Objects.equals(id, that.id) &&
                java.util.Objects.equals(userId, that.userId) &&
                java.util.Objects.equals(type, that.type) &&
                java.util.Objects.equals(title, that.title) &&
                java.util.Objects.equals(content, that.content) &&
                java.util.Objects.equals(relatedId, that.relatedId) &&
                java.util.Objects.equals(relatedType, that.relatedType) &&
                java.util.Objects.equals(fromUserId, that.fromUserId) &&
                java.util.Objects.equals(isRead, that.isRead) &&
                java.util.Objects.equals(createdAt, that.createdAt) &&
                java.util.Objects.equals(readAt, that.readAt);
    }
    
    // hashCode方法
    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, userId, type, title, content, relatedId, 
                                    relatedType, fromUserId, isRead, createdAt, readAt);
    }
}