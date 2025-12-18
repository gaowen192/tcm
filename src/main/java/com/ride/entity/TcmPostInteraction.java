package com.ride.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * 帖子互动记录实体类
 * 记录用户对帖子的点赞和观看行为
 */
@Entity
@Table(name = "tcm_post_interaction", uniqueConstraints = {
        @UniqueConstraint(name = "uk_user_post_type", columnNames = {"user_id", "post_id", "interaction_type"})
})
public class TcmPostInteraction {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户ID
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * 帖子ID
     */
    @Column(name = "post_id", nullable = false)
    private Long postId;

    /**
     * 互动类型：LIKE(点赞)、WATCH(观看)
     */
    @Column(name = "interaction_type", nullable = false)
    private String interactionType;

    /**
     * 互动时间
     */
    @Column(name = "interaction_date", nullable = false)
    private LocalDateTime interactionDate;

    /**
     * 是否删除：0-未删除，1-已删除
     */
    @Column(name = "is_deleted", nullable = false, columnDefinition = "TINYINT DEFAULT 0")
    private Integer isDeleted;

    /**
     * 创建时间
     */
    @Column(name = "created_time", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @Column(name = "updated_time", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedTime;

    // Default constructor
    public TcmPostInteraction() {
    }

    // All-args constructor
    public TcmPostInteraction(Long id, Long userId, Long postId, String interactionType, LocalDateTime interactionDate, Integer isDeleted, LocalDateTime createdTime, LocalDateTime updatedTime) {
        this.id = id;
        this.userId = userId;
        this.postId = postId;
        this.interactionType = interactionType;
        this.interactionDate = interactionDate;
        this.isDeleted = isDeleted;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getInteractionType() {
        return interactionType;
    }

    public void setInteractionType(String interactionType) {
        this.interactionType = interactionType;
    }

    public LocalDateTime getInteractionDate() {
        return interactionDate;
    }

    public void setInteractionDate(LocalDateTime interactionDate) {
        this.interactionDate = interactionDate;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }
}