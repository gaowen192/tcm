package com.ride.service.impl;

import com.ride.entity.TcmArticle;
import com.ride.entity.TcmArticleInteraction;
import com.ride.mapper.TcmArticleInteractionRepository;
import com.ride.mapper.TcmArticleRepository;
import com.ride.service.TcmArticleInteractionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 文章互动服务层实现类（点赞和观看记录）
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@Service
public class TcmArticleInteractionServiceImpl implements TcmArticleInteractionService {
    
    private static final Logger logger = LoggerFactory.getLogger(TcmArticleInteractionServiceImpl.class);
    
    @Autowired
    private TcmArticleInteractionRepository tcmArticleInteractionRepository;
    
    @Autowired
    private TcmArticleRepository tcmArticleRepository;
    
    @Override
    @Transactional
    public TcmArticleInteraction toggleLike(Long articleId, Long userId, boolean isLiked) {
        logger.info("===============Toggle like for article: {}, user: {}, isLiked: {}", articleId, userId, isLiked);
        
        // 检查文章是否存在
        TcmArticle article = tcmArticleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article not found with id: " + articleId));
        
        // 查找或创建互动记录
        TcmArticleInteraction interaction = tcmArticleInteractionRepository.findByUserIdAndArticleId(userId, articleId);
        if (interaction == null) {
            interaction = new TcmArticleInteraction(articleId, userId);
        }
        
        boolean wasLiked = interaction.getIsLiked();
        
        // 更新点赞状态和时间
        interaction.setIsLiked(isLiked);
        interaction.setLikedAt(isLiked ? LocalDateTime.now() : null);
        
        // 更新文章的点赞数
        if (isLiked && !wasLiked) {
            // 点赞
            tcmArticleRepository.incrementLikeCount(articleId);
            logger.info("===============Incremented like count for article: {}", articleId);
        } else if (!isLiked && wasLiked) {
            // 取消点赞
            tcmArticleRepository.decrementLikeCount(articleId);
            logger.info("===============Decremented like count for article: {}", articleId);
        }
        
        return tcmArticleInteractionRepository.save(interaction);
    }
    
    @Override
    @Transactional
    public TcmArticleInteraction recordView(Long articleId, Long userId) {
        logger.info("===============Record view for article: {}, user: {}", articleId, userId);
        
        // 检查文章是否存在
        TcmArticle article = tcmArticleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article not found with id: " + articleId));
        
        // 查找或创建互动记录
        TcmArticleInteraction interaction = tcmArticleInteractionRepository.findByUserIdAndArticleId(userId, articleId);
        if (interaction == null) {
            interaction = new TcmArticleInteraction(articleId, userId);
        }
        
        // 更新观看时间
        interaction.setViewedAt(LocalDateTime.now());
        
        // 更新文章的观看数
        tcmArticleRepository.incrementViewCount(articleId);
        logger.info("===============Incremented view count for article: {}", articleId);
        
        return tcmArticleInteractionRepository.save(interaction);
    }
    
    @Override
    public boolean isArticleLikedByUser(Long articleId, Long userId) {
        logger.info("===============Check if article: {} is liked by user: {}", articleId, userId);
        return tcmArticleInteractionRepository.existsByUserIdAndArticleIdAndIsLikedTrue(userId, articleId);
    }
    
    @Override
    public boolean isArticleViewedByUser(Long articleId, Long userId) {
        logger.info("===============Check if article: {} is viewed by user: {}", articleId, userId);
        return tcmArticleInteractionRepository.existsByUserIdAndArticleIdAndViewedAtIsNotNull(userId, articleId);
    }
    
    @Override
    public long getArticleLikeCount(Long articleId) {
        logger.info("===============Get like count for article: {}", articleId);
        return tcmArticleInteractionRepository.countByArticleIdAndIsLikedTrue(articleId);
    }
    
    @Override
    public long getArticleViewCount(Long articleId) {
        logger.info("===============Get view count for article: {}", articleId);
        return tcmArticleInteractionRepository.countByArticleIdAndViewedAtIsNotNull(articleId);
    }
    
    @Override
    public TcmArticleInteraction getInteractionByUserAndArticle(Long articleId, Long userId) {
        logger.info("===============Get interaction for article: {} and user: {}", articleId, userId);
        return tcmArticleInteractionRepository.findByUserIdAndArticleId(userId, articleId);
    }
}
