package com.ride.service;

import com.ride.entity.TcmArticleInteraction;

/**
 * 文章互动服务层接口（点赞和观看记录）
 * 
 * @author 千行团队
 * @version 1.0.0
 */
public interface TcmArticleInteractionService {
    
    /**
     * 点赞或取消点赞文章
     * 
     * @param articleId 文章ID
     * @param userId 用户ID
     * @param isLiked 是否点赞：true-点赞，false-取消点赞
     * @return 互动记录
     */
    TcmArticleInteraction toggleLike(Long articleId, Long userId, boolean isLiked);
    
    /**
     * 记录文章观看
     * 
     * @param articleId 文章ID
     * @param userId 用户ID
     * @return 互动记录
     */
    TcmArticleInteraction recordView(Long articleId, Long userId);
    
    /**
     * 查询用户是否点赞文章
     * 
     * @param articleId 文章ID
     * @param userId 用户ID
     * @return 是否已点赞
     */
    boolean isArticleLikedByUser(Long articleId, Long userId);
    
    /**
     * 查询用户是否观看文章
     * 
     * @param articleId 文章ID
     * @param userId 用户ID
     * @return 是否已观看
     */
    boolean isArticleViewedByUser(Long articleId, Long userId);
    
    /**
     * 获取文章点赞数
     * 
     * @param articleId 文章ID
     * @return 点赞数
     */
    long getArticleLikeCount(Long articleId);
    
    /**
     * 获取文章观看数
     * 
     * @param articleId 文章ID
     * @return 观看数
     */
    long getArticleViewCount(Long articleId);
    
    /**
     * 获取用户对文章的互动记录
     * 
     * @param articleId 文章ID
     * @param userId 用户ID
     * @return 互动记录
     */
    TcmArticleInteraction getInteractionByUserAndArticle(Long articleId, Long userId);
}
