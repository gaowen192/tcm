package com.ride.dto;

import java.time.LocalDateTime;

/**
 * 医药论坛板块数据传输对象
 * 
 * @author 千行团队
 * @version 1.0.0
 */
public class TcmCategoryDTO {
    
    private Long id;
    private String name;
    private String description;
    private String icon;
    private Long parentId; // 父板块ID，0为顶级板块
    private Integer sortOrder;
    private Long postCount;
    private Long topicCount;
    private Integer status; // 0-禁用，1-启用
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getter和Setter方法
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Long getPostCount() {
        return postCount;
    }

    public void setPostCount(Long postCount) {
        this.postCount = postCount;
    }

    public Long getTopicCount() {
        return topicCount;
    }

    public void setTopicCount(Long topicCount) {
        this.topicCount = topicCount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    // toString方法
    @Override
    public String toString() {
        return "TcmCategoryDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", icon='" + icon + '\'' +
                ", parentId=" + parentId +
                ", sortOrder=" + sortOrder +
                ", postCount=" + postCount +
                ", topicCount=" + topicCount +
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
        TcmCategoryDTO that = (TcmCategoryDTO) o;
        return java.util.Objects.equals(id, that.id) &&
                java.util.Objects.equals(name, that.name) &&
                java.util.Objects.equals(description, that.description) &&
                java.util.Objects.equals(icon, that.icon) &&
                java.util.Objects.equals(parentId, that.parentId) &&
                java.util.Objects.equals(sortOrder, that.sortOrder) &&
                java.util.Objects.equals(postCount, that.postCount) &&
                java.util.Objects.equals(topicCount, that.topicCount) &&
                java.util.Objects.equals(status, that.status) &&
                java.util.Objects.equals(createdAt, that.createdAt) &&
                java.util.Objects.equals(updatedAt, that.updatedAt);
    }

    // hashCode方法
    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, name, description, icon, parentId, sortOrder, postCount, 
                                    topicCount, status, createdAt, updatedAt);
    }
}
