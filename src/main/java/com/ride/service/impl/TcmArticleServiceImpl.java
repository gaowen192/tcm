package com.ride.service.impl;

import com.ride.dto.TcmArticleDTO;
import com.ride.entity.TcmArticle;
import com.ride.entity.TcmArticleHistory;
import com.ride.mapper.TcmArticleRepository;
import com.ride.mapper.TcmArticleHistoryRepository;
import com.ride.service.TcmArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 医药论坛文章服务层实现类
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@Service
public class TcmArticleServiceImpl implements TcmArticleService {
    
    private static final Logger logger = LoggerFactory.getLogger(TcmArticleServiceImpl.class);
    
    @Autowired
    private TcmArticleRepository tcmArticleRepository;
    
    @Autowired
    private TcmArticleHistoryRepository tcmArticleHistoryRepository;
    
    @Override
    @Transactional
    public TcmArticle createArticle(TcmArticleDTO articleDTO) {
        logger.info("===============Creating article: {}", articleDTO.getTitle());
        
        TcmArticle article = new TcmArticle();
        BeanUtils.copyProperties(articleDTO, article);
        
        // 设置默认值
        if (article.getStatus() == null) {
            article.setStatus(1); // 默认状态为启用
        }
        if (article.getViewCount() == null) {
            article.setViewCount(0L);
        }
        if (article.getLikeCount() == null) {
            article.setLikeCount(0L);
        }
        if (article.getCommentCount() == null) {
            article.setCommentCount(0L);
        }
        if (article.getCollectCount() == null) {
            article.setCollectCount(0L);
        }
        if (article.getIsHot() == null) {
            article.setIsHot(false);
        }
        if (article.getIsRecommended() == null) {
            article.setIsRecommended(false);
        }
        
        return tcmArticleRepository.save(article);
    }
    
    @Override
    public TcmArticle getArticleById(Long id) {
        logger.info("===============Getting article by id: {}", id);
        return tcmArticleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found with id: " + id));
    }
    
    @Override
    @Transactional
    public TcmArticle updateArticle(Long id, TcmArticleDTO articleDTO) {
        logger.info("===============Updating article: {}", id);
        
        TcmArticle existingArticle = getArticleById(id);
        
        // 保存历史记录
        Integer latestVersion = tcmArticleHistoryRepository.findLatestVersionByArticleId(id);
        Integer newVersion = latestVersion == null ? 1 : latestVersion + 1;
        TcmArticleHistory history = new TcmArticleHistory(existingArticle, newVersion);
        tcmArticleHistoryRepository.save(history);
        logger.info("===============Saved article history for article: {}, version: {}", id, newVersion);
        
        // 复制属性，不覆盖null值
        BeanUtils.copyProperties(articleDTO, existingArticle, "id", "createdAt");
        
        // 设置为已更新
        existingArticle.setIsUpdated(1);
        
        return tcmArticleRepository.save(existingArticle);
    }
    
    @Override
    @Transactional
    public boolean deleteArticle(Long id) {
        logger.info("===============Deleting article: {}", id);
        
        if (tcmArticleRepository.existsById(id)) {
            tcmArticleRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    @Override
    public List<TcmArticle> getArticlesByUserId(Long userId) {
        logger.info("===============Getting articles by user id: {}", userId);
        return tcmArticleRepository.findByUserId(userId);
    }
    
    @Override
    public Map<String, Object> getArticlesByUserIdWithPagination(Long userId, int page, int pageSize) {
        logger.info("===============Getting articles by user id with pagination: userId={}, page={}, pageSize={}", 
                userId, page, pageSize);
        
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<TcmArticle> articlePage = tcmArticleRepository.findByUserId(userId, pageable);
        
        Map<String, Object> result = new HashMap<>();
        result.put("articles", articlePage.getContent());
        result.put("totalElements", articlePage.getTotalElements());
        result.put("totalPages", articlePage.getTotalPages());
        result.put("currentPage", page);
        result.put("pageSize", pageSize);
        
        return result;
    }
    
    @Override
    public List<TcmArticle> getArticlesByCategoryId(Long categoryId) {
        logger.info("===============Getting articles by category id: {}", categoryId);
        return tcmArticleRepository.findByCategoryId(categoryId);
    }
    
    @Override
    public Map<String, Object> getArticlesByCategoryIdWithPagination(Long categoryId, Pageable pageable) {
        logger.info("===============Getting articles by category id with pagination: categoryId={}, page={}, pageSize={}", 
                categoryId, pageable.getPageNumber() + 1, pageable.getPageSize());
        
        Page<TcmArticle> articlePage = tcmArticleRepository.findByCategoryId(categoryId, pageable);
        
        Map<String, Object> result = new HashMap<>();
        result.put("articles", articlePage.getContent());
        result.put("totalElements", articlePage.getTotalElements());
        result.put("totalPages", articlePage.getTotalPages());
        result.put("currentPage", pageable.getPageNumber() + 1);
        result.put("pageSize", pageable.getPageSize());
        
        return result;
    }
    
    @Override
    public List<TcmArticle> getArticlesByStatus(Integer status) {
        logger.info("===============Getting articles by status: {}", status);
        return tcmArticleRepository.findByStatus(status);
    }
    
    @Override
    public Map<String, Object> getArticlesByStatusWithPagination(Integer status, int page, int pageSize) {
        logger.info("===============Getting articles by status with pagination: status={}, page={}, pageSize={}", 
                status, page, pageSize);
        
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<TcmArticle> articlePage = tcmArticleRepository.findByStatus(status, pageable);
        
        Map<String, Object> result = new HashMap<>();
        result.put("articles", articlePage.getContent());
        result.put("totalElements", articlePage.getTotalElements());
        result.put("totalPages", articlePage.getTotalPages());
        result.put("currentPage", page);
        result.put("pageSize", pageSize);
        
        return result;
    }
    
    @Override
    public List<TcmArticle> getArticlesByUserIdAndStatus(Long userId, Integer status) {
        logger.info("===============Getting articles by user id and status: userId={}, status={}", userId, status);
        return tcmArticleRepository.findByUserIdAndStatus(userId, status);
    }
    
    @Override
    public Map<String, Object> getAllArticlesWithPagination(int page, int pageSize) {
        logger.info("===============Getting all articles with pagination: page={}, pageSize={}", page, pageSize);
        
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<TcmArticle> articlePage = tcmArticleRepository.findAll(pageable);
        
        Map<String, Object> result = new HashMap<>();
        result.put("articles", articlePage.getContent());
        result.put("totalElements", articlePage.getTotalElements());
        result.put("totalPages", articlePage.getTotalPages());
        result.put("currentPage", page);
        result.put("pageSize", pageSize);
        
        return result;
    }
    
    @Override
    public List<TcmArticle> searchArticlesByTitle(String title) {
        logger.info("===============Searching articles by title: {}", title);
        return tcmArticleRepository.findByTitleLike(title);
    }
    
    @Override
    public List<TcmArticle> searchArticlesByTag(String tag) {
        logger.info("===============Searching articles by tag: {}", tag);
        return tcmArticleRepository.findByTag(tag);
    }
    
    @Override
    public Map<String, Object> searchArticles(String title, int page, int pageSize) {
        logger.info("===============Searching articles with title={}, page={}, pageSize={}", title, page, pageSize);
        
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<TcmArticle> articlePage = tcmArticleRepository.searchArticles(title, pageable);
        
        Map<String, Object> result = new HashMap<>();
        result.put("articles", articlePage.getContent());
        result.put("totalElements", articlePage.getTotalElements());
        result.put("totalPages", articlePage.getTotalPages());
        result.put("currentPage", page);
        result.put("pageSize", pageSize);
        
        return result;
    }
    
    @Override
    public List<TcmArticle> getHotArticles(int limit) {
        logger.info("===============Getting hot articles with limit: {}", limit);
        return tcmArticleRepository.findHotArticles(limit);
    }
    
    @Override
    public List<TcmArticle> getRecommendedArticles() {
        logger.info("===============Getting recommended articles");
        return tcmArticleRepository.findByIsRecommendedTrueAndStatus(1);
    }
    
    @Override
    public List<TcmArticle> getLatestArticles(int limit) {
        logger.info("===============Getting latest articles with limit: {}", limit);
        return tcmArticleRepository.findLatestArticles(limit);
    }
    
    @Override
    @Transactional
    public boolean incrementViewCount(Long id) {
        logger.info("===============Incrementing view count for article: {}", id);
        try {
            tcmArticleRepository.incrementViewCount(id);
            return true;
        } catch (Exception e) {
            logger.error("===============Error incrementing view count for article: {}", id, e);
            return false;
        }
    }
    
    @Override
    @Transactional
    public boolean incrementLikeCount(Long id) {
        logger.info("===============Incrementing like count for article: {}", id);
        try {
            tcmArticleRepository.incrementLikeCount(id);
            return true;
        } catch (Exception e) {
            logger.error("===============Error incrementing like count for article: {}", id, e);
            return false;
        }
    }
    
    @Override
    @Transactional
    public boolean decrementLikeCount(Long id) {
        logger.info("===============Decrementing like count for article: {}", id);
        try {
            tcmArticleRepository.decrementLikeCount(id);
            return true;
        } catch (Exception e) {
            logger.error("===============Error decrementing like count for article: {}", id, e);
            return false;
        }
    }
    
    @Override
    @Transactional
    public boolean incrementCommentCount(Long id) {
        logger.info("===============Incrementing comment count for article: {}", id);
        try {
            tcmArticleRepository.incrementCommentCount(id);
            return true;
        } catch (Exception e) {
            logger.error("===============Error incrementing comment count for article: {}", id, e);
            return false;
        }
    }
    
    @Override
    @Transactional
    public boolean decrementCommentCount(Long id) {
        logger.info("===============Decrementing comment count for article: {}", id);
        try {
            tcmArticleRepository.decrementCommentCount(id);
            return true;
        } catch (Exception e) {
            logger.error("===============Error decrementing comment count for article: {}", id, e);
            return false;
        }
    }
    
    @Override
    @Transactional
    public boolean incrementCollectCount(Long id) {
        logger.info("===============Incrementing collect count for article: {}", id);
        try {
            tcmArticleRepository.incrementCollectCount(id);
            return true;
        } catch (Exception e) {
            logger.error("===============Error incrementing collect count for article: {}", id, e);
            return false;
        }
    }
    
    @Override
    @Transactional
    public boolean decrementCollectCount(Long id) {
        logger.info("===============Decrementing collect count for article: {}", id);
        try {
            tcmArticleRepository.decrementCollectCount(id);
            return true;
        } catch (Exception e) {
            logger.error("===============Error decrementing collect count for article: {}", id, e);
            return false;
        }
    }
    
    @Override
    @Transactional
    public boolean updateHotStatus(Long id, Boolean isHot) {
        logger.info("===============Updating hot status for article: {}, isHot: {}", id, isHot);
        try {
            tcmArticleRepository.updateHotStatus(List.of(id), isHot);
            return true;
        } catch (Exception e) {
            logger.error("===============Error updating hot status for article: {}", id, e);
            return false;
        }
    }
    
    @Override
    @Transactional
    public boolean batchUpdateHotStatus(List<Long> ids, Boolean isHot) {
        logger.info("===============Batch updating hot status for articles: {}, isHot: {}", ids, isHot);
        try {
            tcmArticleRepository.updateHotStatus(ids, isHot);
            return true;
        } catch (Exception e) {
            logger.error("===============Error batch updating hot status for articles", e);
            return false;
        }
    }
    
    @Override
    @Transactional
    public boolean updateRecommendedStatus(Long id, Boolean isRecommended) {
        logger.info("===============Updating recommended status for article: {}, isRecommended: {}", id, isRecommended);
        try {
            tcmArticleRepository.updateRecommendedStatus(List.of(id), isRecommended);
            return true;
        } catch (Exception e) {
            logger.error("===============Error updating recommended status for article: {}", id, e);
            return false;
        }
    }
    
    @Override
    @Transactional
    public boolean batchUpdateRecommendedStatus(List<Long> ids, Boolean isRecommended) {
        logger.info("===============Batch updating recommended status for articles: {}, isRecommended: {}", ids, isRecommended);
        try {
            tcmArticleRepository.updateRecommendedStatus(ids, isRecommended);
            return true;
        } catch (Exception e) {
            logger.error("===============Error batch updating recommended status for articles", e);
            return false;
        }
    }
    
    @Override
    public long countArticlesByUserId(Long userId) {
        logger.info("===============Counting articles by user id: {}", userId);
        return tcmArticleRepository.countByUserId(userId);
    }
    
    @Override
    public long countArticlesByCategoryId(Long categoryId) {
        logger.info("===============Counting articles by category id: {}", categoryId);
        return tcmArticleRepository.countByCategoryId(categoryId);
    }
    
    @Override
    public List<Map<String, Object>> getArticlesWithUserNameByCategoryId(Long categoryId) {
        logger.info("===============Getting articles with user name by category id: {}", categoryId);
        
        List<Object[]> results = tcmArticleRepository.findArticlesWithUserNameByCategoryId(categoryId);
        List<Map<String, Object>> articles = new ArrayList<>();
        
        for (Object[] result : results) {
            Map<String, Object> articleMap = new HashMap<>();
            
            // 这里需要根据数据库表的字段顺序来提取值，确保与SQL查询中的字段顺序一致
            articleMap.put("id", result[0]);
            articleMap.put("title", result[1]);
            articleMap.put("content", result[2]);
            articleMap.put("userId", result[3]);
            articleMap.put("userName", result[result.length - 1]); // 用户名在最后一列
            articleMap.put("categoryId", result[4]);
            articleMap.put("tags", result[5]);
            articleMap.put("summary", result[6]);
            articleMap.put("status", result[7]);
            articleMap.put("viewCount", result[8]);
            articleMap.put("likeCount", result[9]);
            articleMap.put("commentCount", result[10]);
            articleMap.put("collectCount", result[11]);
            articleMap.put("isHot", result[12]);
            articleMap.put("isRecommended", result[13]);
            articleMap.put("createdAt", result[14]);
            articleMap.put("updatedAt", result[15]);
            articleMap.put("isUpdated", result[16]);
            
            articles.add(articleMap);
        }
        
        return articles;
    }
}