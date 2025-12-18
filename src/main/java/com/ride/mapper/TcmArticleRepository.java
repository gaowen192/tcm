package com.ride.mapper;

import com.ride.entity.TcmArticle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 医药论坛文章数据访问层
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@Repository
public interface TcmArticleRepository extends JpaRepository<TcmArticle, Long> {
    
    /**
     * 根据用户ID查找文章列表
     * 
     * @param userId 用户ID
     * @return 文章列表
     */
    List<TcmArticle> findByUserId(Long userId);
    
    /**
     * 根据用户ID查找文章列表（分页）
     * 
     * @param userId 用户ID
     * @param pageable 分页参数
     * @return 文章分页结果
     */
    Page<TcmArticle> findByUserId(Long userId, Pageable pageable);
    
    /**
     * 根据板块ID查找文章列表
     * 
     * @param categoryId 板块ID
     * @return 文章列表
     */
    List<TcmArticle> findByCategoryId(Long categoryId);
    
    /**
     * 根据板块ID查找文章列表（分页）
     * 
     * @param categoryId 板块ID
     * @param pageable 分页参数
     * @return 文章分页结果
     */
    Page<TcmArticle> findByCategoryId(Long categoryId, Pageable pageable);
    
    /**
     * 根据板块ID查找文章列表，包含用户名称
     * 使用原生SQL进行关联查询
     * 
     * @param categoryId 板块ID
     * @return 文章和用户信息列表
     */
    @Query(value = "SELECT a.*, u.username AS userName " +
                   "FROM tcm_articles a " +
                   "LEFT JOIN tcm_users u ON a.user_id = u.id " +
                   "WHERE a.category_id = :categoryId", 
           nativeQuery = true)
    List<Object[]> findArticlesWithUserNameByCategoryId(@Param("categoryId") Long categoryId);
    
    /**
     * 根据状态查找文章列表
     * 
     * @param status 状态
     * @return 文章列表
     */
    List<TcmArticle> findByStatus(Integer status);
    
    /**
     * 根据状态查找文章列表（分页）
     * 
     * @param status 状态
     * @param pageable 分页参数
     * @return 文章分页结果
     */
    Page<TcmArticle> findByStatus(Integer status, Pageable pageable);
    
    /**
     * 根据用户ID和状态查找文章列表
     * 
     * @param userId 用户ID
     * @param status 状态
     * @return 文章列表
     */
    List<TcmArticle> findByUserIdAndStatus(Long userId, Integer status);
    
    /**
     * 根据板块ID和状态查找文章列表，按创建时间倒序
     * 
     * @param categoryId 板块ID
     * @param status 状态
     * @return 文章列表
     */
    List<TcmArticle> findByCategoryIdAndStatusOrderByCreatedAtDesc(Long categoryId, Integer status);
    
    /**
     * 根据标题模糊查询
     * 
     * @param title 标题关键字
     * @return 文章列表
     */
    @Query("SELECT a FROM TcmArticle a WHERE a.title LIKE CONCAT('%', :title, '%') AND a.status = 1")
    List<TcmArticle> findByTitleLike(@Param("title") String title);
    
    /**
     * 根据标签查询文章
     * 
     * @param tag 标签
     * @return 文章列表
     */
    @Query("SELECT a FROM TcmArticle a WHERE a.tags LIKE CONCAT('%', :tag, '%') AND a.status = 1")
    List<TcmArticle> findByTag(@Param("tag") String tag);
    
    /**
     * 获取热门文章列表
     * 
     * @param limit 限制数量
     * @return 热门文章列表
     */
    @Query("SELECT a FROM TcmArticle a WHERE a.status = 1 ORDER BY a.viewCount DESC")
    List<TcmArticle> findHotArticles(@Param("limit") int limit);
    
    /**
     * 获取推荐文章列表
     * 
     * @return 推荐文章列表
     */
    List<TcmArticle> findByIsRecommendedTrueAndStatus(Integer status);
    
    /**
     * 获取最新文章列表
     * 
     * @param limit 限制数量
     * @return 最新文章列表
     */
    @Query("SELECT a FROM TcmArticle a WHERE a.status = 1 ORDER BY a.createdAt DESC")
    List<TcmArticle> findLatestArticles(@Param("limit") int limit);
    
    /**
     * 根据标题和内容搜索文章并分页
     * 
     * @param title 标题关键字
     * @param pageable 分页参数
     * @return 文章分页结果
     */
    @Query("SELECT a FROM TcmArticle a WHERE a.status = 1 AND (:title IS NULL OR a.title LIKE CONCAT('%', :title, '%') OR a.content LIKE CONCAT('%', :title, '%')) ORDER BY a.createdAt DESC")
    Page<TcmArticle> searchArticles(@Param("title") String title, Pageable pageable);
    
    /**
     * 统计用户文章数量
     * 
     * @param userId 用户ID
     * @return 文章数量
     */
    long countByUserId(Long userId);
    
    /**
     * 统计板块文章数量
     * 
     * @param categoryId 板块ID
     * @return 文章数量
     */
    long countByCategoryId(Long categoryId);
    
    /**
     * 增加阅读次数
     * 
     * @param id 文章ID
     */
    @Modifying
    @Query("UPDATE TcmArticle a SET a.viewCount = a.viewCount + 1 WHERE a.id = :id")
    void incrementViewCount(@Param("id") Long id);
    
    /**
     * 增加点赞次数
     * 
     * @param id 文章ID
     */
    @Modifying
    @Query("UPDATE TcmArticle a SET a.likeCount = a.likeCount + 1 WHERE a.id = :id")
    void incrementLikeCount(@Param("id") Long id);
    
    /**
     * 减少点赞次数
     * 
     * @param id 文章ID
     */
    @Modifying
    @Query("UPDATE TcmArticle a SET a.likeCount = a.likeCount - 1 WHERE a.id = :id AND a.likeCount > 0")
    void decrementLikeCount(@Param("id") Long id);
    
    /**
     * 增加评论次数
     * 
     * @param id 文章ID
     */
    @Modifying
    @Query("UPDATE TcmArticle a SET a.commentCount = a.commentCount + 1 WHERE a.id = :id")
    void incrementCommentCount(@Param("id") Long id);
    
    /**
     * 减少评论次数
     * 
     * @param id 文章ID
     */
    @Modifying
    @Query("UPDATE TcmArticle a SET a.commentCount = a.commentCount - 1 WHERE a.id = :id AND a.commentCount > 0")
    void decrementCommentCount(@Param("id") Long id);
    
    /**
     * 增加收藏次数
     * 
     * @param id 文章ID
     */
    @Modifying
    @Query("UPDATE TcmArticle a SET a.collectCount = a.collectCount + 1 WHERE a.id = :id")
    void incrementCollectCount(@Param("id") Long id);
    
    /**
     * 减少收藏次数
     * 
     * @param id 文章ID
     */
    @Modifying
    @Query("UPDATE TcmArticle a SET a.collectCount = a.collectCount - 1 WHERE a.id = :id AND a.collectCount > 0")
    void decrementCollectCount(@Param("id") Long id);
    
    /**
     * 批量更新热门状态
     * 
     * @param ids 文章ID列表
     * @param isHot 是否热门
     */
    @Modifying
    @Query("UPDATE TcmArticle a SET a.isHot = :isHot WHERE a.id IN :ids")
    void updateHotStatus(@Param("ids") List<Long> ids, @Param("isHot") Boolean isHot);
    
    /**
     * 批量更新推荐状态
     * 
     * @param ids 文章ID列表
     * @param isRecommended 是否推荐
     */
    @Modifying
    @Query("UPDATE TcmArticle a SET a.isRecommended = :isRecommended WHERE a.id IN :ids")
    void updateRecommendedStatus(@Param("ids") List<Long> ids, @Param("isRecommended") Boolean isRecommended);
}