package com.ride.mapper;

import com.ride.entity.TcmArticleInteraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 文章互动数据访问层（点赞和观看记录）
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@Repository
public interface TcmArticleInteractionRepository extends JpaRepository<TcmArticleInteraction, Long> {
    
    /**
     * 根据用户ID和文章ID查找互动记录
     * 
     * @param userId 用户ID
     * @param articleId 文章ID
     * @return 互动记录
     */
    TcmArticleInteraction findByUserIdAndArticleId(Long userId, Long articleId);
    
    /**
     * 统计文章的点赞数
     * 
     * @param articleId 文章ID
     * @return 点赞数
     */
    long countByArticleIdAndIsLikedTrue(Long articleId);
    
    /**
     * 统计文章的观看数（去重用户）
     * 
     * @param articleId 文章ID
     * @return 观看数
     */
    long countByArticleIdAndViewedAtIsNotNull(Long articleId);
    
    /**
     * 更新点赞状态
     * 
     * @param articleId 文章ID
     * @param userId 用户ID
     * @param isLiked 是否点赞
     */
    @Modifying
    @Query("UPDATE TcmArticleInteraction i SET i.isLiked = :isLiked, i.likedAt = CURRENT_TIMESTAMP WHERE i.userId = :userId AND i.articleId = :articleId")
    void updateLikeStatus(@Param("articleId") Long articleId, @Param("userId") Long userId, @Param("isLiked") Boolean isLiked);
    
    /**
     * 更新观看时间
     * 
     * @param articleId 文章ID
     * @param userId 用户ID
     */
    @Modifying
    @Query("UPDATE TcmArticleInteraction i SET i.viewedAt = CURRENT_TIMESTAMP WHERE i.userId = :userId AND i.articleId = :articleId")
    void updateViewedAt(@Param("articleId") Long articleId, @Param("userId") Long userId);
    
    /**
     * 检查用户是否已点赞文章
     * 
     * @param userId 用户ID
     * @param articleId 文章ID
     * @return 是否已点赞
     */
    @Query("SELECT COUNT(*) > 0 FROM TcmArticleInteraction i WHERE i.userId = :userId AND i.articleId = :articleId AND i.isLiked = true")
    boolean existsByUserIdAndArticleIdAndIsLikedTrue(@Param("userId") Long userId, @Param("articleId") Long articleId);
    
    /**
     * 检查用户是否已观看文章
     * 
     * @param userId 用户ID
     * @param articleId 文章ID
     * @return 是否已观看
     */
    @Query("SELECT COUNT(*) > 0 FROM TcmArticleInteraction i WHERE i.userId = :userId AND i.articleId = :articleId AND i.viewedAt IS NOT NULL")
    boolean existsByUserIdAndArticleIdAndViewedAtIsNotNull(@Param("userId") Long userId, @Param("articleId") Long articleId);
}
