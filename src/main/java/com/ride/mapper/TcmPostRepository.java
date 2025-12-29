package com.ride.mapper;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ride.entity.TcmPost;

/**
 * 医药论坛帖子数据访问层
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@Repository
public interface TcmPostRepository extends JpaRepository<TcmPost, Long> {
    
    /**
     * 根据条件动态查询帖子列表（按最新排序，包含热门帖子过滤）
     * 
     * @param keyword 搜索关键词
     * @param pageable 分页参数
     * @return 分页帖子列表
     */
    @Query(value = "SELECT p FROM TcmPost p WHERE p.viewCount > 1000 AND (:keyword IS NULL OR p.title LIKE CONCAT('%', :keyword, '%') OR p.content LIKE CONCAT('%', :keyword, '%')) ORDER BY p.createdAt DESC, p.id ASC",
           countQuery = "SELECT COUNT(p) FROM TcmPost p WHERE p.viewCount > 1000 AND (:keyword IS NULL OR p.title LIKE CONCAT('%', :keyword, '%') OR p.content LIKE CONCAT('%', :keyword, '%'))")
    Page<TcmPost> findByDynamicConditionsOrderByNewestWithHotpost(@Param("keyword") String keyword, 
                                                                    Pageable pageable);
    
    /**
     * 根据条件动态查询帖子列表（按最新排序，不包含热门帖子过滤）
     * 
     * @param keyword 搜索关键词
     * @param pageable 分页参数
     * @return 分页帖子列表
     */
    @Query(value = "SELECT p FROM TcmPost p WHERE (:keyword IS NULL OR p.title LIKE CONCAT('%', :keyword, '%') OR p.content LIKE CONCAT('%', :keyword, '%')) ORDER BY p.createdAt DESC, p.id ASC",
           countQuery = "SELECT COUNT(p) FROM TcmPost p WHERE (:keyword IS NULL OR p.title LIKE CONCAT('%', :keyword, '%') OR p.content LIKE CONCAT('%', :keyword, '%'))")
    Page<TcmPost> findByDynamicConditionsOrderByNewestWithoutHotpost(@Param("keyword") String keyword, 
                                                                      Pageable pageable);
    
    /**
     * 根据条件动态查询帖子列表（按ID排序，包含热门帖子过滤）
     * 
     * @param keyword 搜索关键词
     * @param pageable 分页参数
     * @return 分页帖子列表
     */
    @Query(value = "SELECT p FROM TcmPost p WHERE p.viewCount > 1000 AND (:keyword IS NULL OR p.title LIKE CONCAT('%', :keyword, '%') OR p.content LIKE CONCAT('%', :keyword, '%')) ORDER BY p.id ASC",
           countQuery = "SELECT COUNT(p) FROM TcmPost p WHERE p.viewCount > 1000 AND (:keyword IS NULL OR p.title LIKE CONCAT('%', :keyword, '%') OR p.content LIKE CONCAT('%', :keyword, '%'))")
    Page<TcmPost> findByDynamicConditionsOrderByIdWithHotpost(@Param("keyword") String keyword, 
                                                                Pageable pageable);
    
    /**
     * 根据条件动态查询帖子列表（按ID排序，不包含热门帖子过滤）
     * 
     * @param keyword 搜索关键词
     * @param pageable 分页参数
     * @return 分页帖子列表
     */
    @Query(value = "SELECT p FROM TcmPost p WHERE (:keyword IS NULL OR p.title LIKE CONCAT('%', :keyword, '%') OR p.content LIKE CONCAT('%', :keyword, '%')) ORDER BY p.id ASC",
           countQuery = "SELECT COUNT(p) FROM TcmPost p WHERE (:keyword IS NULL OR p.title LIKE CONCAT('%', :keyword, '%') OR p.content LIKE CONCAT('%', :keyword, '%'))")
    Page<TcmPost> findByDynamicConditionsOrderByIdWithoutHotpost(@Param("keyword") String keyword, 
                                                                   Pageable pageable);
    
    /**
     * 根据用户ID查找帖子列表
     * 
     * @param userId 用户ID
     * @return 帖子列表
     */
    List<TcmPost> findByUserId(Long userId);
    
    /**
     * 根据用户ID查找帖子列表（分页）
     * 
     * @param userId 用户ID
     * @param pageable 分页参数
     * @return 分页帖子列表
     */
    Page<TcmPost> findByUserId(Long userId, Pageable pageable);
    
    /**
     * 根据板块ID查找帖子列表
     * 
     * @param categoryId 板块ID
     * @return 帖子列表
     */
    List<TcmPost> findByCategoryId(Long categoryId);
    
    /**
     * 根据状态查找帖子列表
     * 
     * @param status 状态
     * @return 帖子列表
     */
    List<TcmPost> findByStatus(Integer status);
    
    /**
     * 根据用户ID和状态查找帖子列表
     * 
     * @param userId 用户ID
     * @param status 状态
     * @return 帖子列表
     */
    List<TcmPost> findByUserIdAndStatus(Long userId, Integer status);
    
    /**
     * 根据板块ID和状态查找帖子列表，按创建时间倒序
     * 
     * @param categoryId 板块ID
     * @param status 状态
     * @return 帖子列表
     */
    List<TcmPost> findByCategoryIdAndStatusOrderByCreatedAtDesc(Long categoryId, Integer status);
    
    /**
     * 根据标题模糊查询
     * 
     * @param title 标题关键字
     * @return 帖子列表
     */
    @Query("SELECT p FROM TcmPost p WHERE p.title LIKE CONCAT('%', :title, '%') AND p.status = 1")
    List<TcmPost> findByTitleLike(@Param("title") String title);
    
    /**
     * 统计用户的帖子数量
     * 
     * @param userId 用户ID
     * @return 帖子数量
     */
    @Query("SELECT COUNT(p) FROM TcmPost p WHERE p.userId = :userId AND p.status = 1")
    long countPostsByUserId(@Param("userId") Long userId);
    
    /**
     * 统计板块下的帖子数量
     * 
     * @param categoryId 板块ID
     * @return 帖子数量
     */
    @Query("SELECT COUNT(p) FROM TcmPost p WHERE p.categoryId = :categoryId AND p.status = 1")
    long countPostsByCategoryId(@Param("categoryId") Long categoryId);
    
    /**
     * 统计总帖子数量
     * 
     * @param status 状态
     * @return 总帖子数量
     */
    @Query("SELECT COUNT(p) FROM TcmPost p WHERE p.status = :status")
    long countByStatus(@Param("status") Integer status);
    
    /**
     * 根据用户ID统计帖子数量
     * 
     * @param userId 用户ID
     * @return 帖子数量
     */
    long countByUserId(Long userId);
    
    /**
     * 根据板块ID统计帖子数量
     * 
     * @param categoryId 板块ID
     * @return 帖子数量
     */
    long countByCategoryId(Long categoryId);
    
    /**
     * 查询热门帖子（按点赞数排序）
     * 
     * @param status 状态
     * @return 帖子列表
     */
    @Query("SELECT p FROM TcmPost p WHERE p.status = :status ORDER BY p.likeCount DESC, p.viewCount DESC")
    List<TcmPost> findHotPosts(@Param("status") Integer status);
    
    /**
     * 查询浏览量最多的帖子
     * 
     * @param status 状态
     * @return 帖子列表
     */
    @Query("SELECT p FROM TcmPost p WHERE p.status = :status ORDER BY p.viewCount DESC")
    List<TcmPost> findTopPostsByViewCount(@Param("status") Integer status);
    
    /**
     * 查询最新帖子（按创建时间倒序）
     * 
     * @param status 状态
     * @return 帖子列表
     */
    @Query("SELECT p FROM TcmPost p WHERE p.status = :status ORDER BY p.createdAt DESC")
    List<TcmPost> findLatestPosts(@Param("status") Integer status);
    
    /**
     * 根据标题或内容搜索帖子
     * 
     * @param title 标题关键字
     * @param content 内容关键字
     * @return 帖子列表
     */
    @Query("SELECT p FROM TcmPost p WHERE (p.title LIKE CONCAT('%', :title, '%') OR p.content LIKE CONCAT('%', :content, '%')) AND p.status = 1")
    List<TcmPost> findByTitleContainingOrContentContaining(@Param("title") String title, @Param("content") String content);
    
    /**
     * 根据标题或内容搜索帖子（分页，按创建时间倒序）
     * 
     * @param title 标题关键字
     * @param content 内容关键字
     * @param pageable 分页参数
     * @return 分页帖子列表
     */
    @Query(value = "SELECT p FROM TcmPost p WHERE (p.title LIKE CONCAT('%', :title, '%') OR p.content LIKE CONCAT('%', :content, '%')) AND p.status = 1 ORDER BY p.createdAt  desc",
           countQuery = "SELECT COUNT(p) FROM TcmPost p WHERE (p.title LIKE CONCAT('%', :title, '%') OR p.content LIKE CONCAT('%', :content, '%')) AND p.status = 1")
    Page<TcmPost> findByTitleContainingOrContentContaining(@Param("title") String title, @Param("content") String content, Pageable pageable);
    
    /**
     * 点赞帖子
     * 
     * @param postId 帖子ID
     */
    @Modifying
    @Query("UPDATE TcmPost p SET p.likeCount = p.likeCount + 1 WHERE p.id = :postId")
    void likePost(@Param("postId") Long postId);
    
    /**
     * 取消点赞帖子
     * 
     * @param postId 帖子ID
     */
    @Modifying
    @Query("UPDATE TcmPost p SET p.likeCount = CASE WHEN p.likeCount > 0 THEN p.likeCount - 1 ELSE 0 END WHERE p.id = :postId")
    void unlikePost(@Param("postId") Long postId);
    
    /**
     * 增加浏览量
     * 
     * @param postId 帖子ID
     */
    @Modifying
    @Query("UPDATE TcmPost p SET p.viewCount = p.viewCount + 1 WHERE p.id = :postId")
    void increaseViewCount(Long postId);
    
    /**
     * 减少回复数量
     * 
     * @param postId 帖子ID
     */
    @Modifying
    @Query("UPDATE TcmPost p SET p.replyCount = CASE WHEN p.replyCount > 0 THEN p.replyCount - 1 ELSE 0 END WHERE p.id = :postId")
    void decreaseReplyCount(@Param("postId") Long postId);
    
    /**
     * 增加回复数量
     * 
     * @param postId 帖子ID
     */
    @Modifying
    @Query("UPDATE TcmPost p SET p.replyCount = p.replyCount + 1 WHERE p.id = :postId")
    void increaseReplyCount(@Param("postId") Long postId);
    
    /**
     * 根据用户ID和板块ID查询帖子
     * 
     * @param userId 用户ID
     * @param categoryId 板块ID
     * @return 帖子列表
     */
    List<TcmPost> findByUserIdAndCategoryId(Long userId, Long categoryId);
}
