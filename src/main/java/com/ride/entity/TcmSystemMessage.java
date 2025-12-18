package com.ride.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import jakarta.persistence.*;

import java.util.Date;

/**
 * 中医药系统消息实体类
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@Entity
@Table(name = "tcm_system_messages")
public class TcmSystemMessage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "title", nullable = false, length = 255)
    private String title;
    
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;
    
    @Column(name = "message_type", nullable = false, columnDefinition = "TINYINT DEFAULT 0")
    private Integer messageType = 0;
    
    @Column(name = "target_type", nullable = false, columnDefinition = "TINYINT DEFAULT 0")
    private Integer targetType = 0;
    
    @Column(name = "target_value", length = 255)
    private String targetValue;
    
    @Column(name = "user_id", nullable = true)
    private Long userId;
    
    @Column(name = "is_read", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isRead = false;
    
    @Column(name = "status", nullable = false, columnDefinition = "TINYINT DEFAULT 0")
    private Integer status = 0;
    
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;
    
    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;
    
    // 无参构造函数
    public TcmSystemMessage() {
    }
    
    // 全参构造函数
    public TcmSystemMessage(Long id, String title, String content, Integer messageType, Integer targetType, 
                           String targetValue, Long userId, Boolean isRead, Integer status, Date createdAt, Date updatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.messageType = messageType;
        this.targetType = targetType;
        this.targetValue = targetValue;
        this.userId = userId;
        this.isRead = isRead;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // getter和setter方法
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
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public Integer getMessageType() {
        return messageType;
    }
    
    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }
    
    public Integer getTargetType() {
        return targetType;
    }
    
    public void setTargetType(Integer targetType) {
        this.targetType = targetType;
    }
    
    public String getTargetValue() {
        return targetValue;
    }
    
    public void setTargetValue(String targetValue) {
        this.targetValue = targetValue;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Boolean getIsRead() {
        return isRead;
    }
    
    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    public Date getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}