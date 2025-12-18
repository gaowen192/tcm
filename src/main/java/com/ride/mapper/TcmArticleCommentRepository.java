package com.ride.mapper;

import com.ride.entity.TcmArticleComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 文章评论数据访问层
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@Repository
public interface TcmArticleCommentRepository extends JpaRepository<TcmArticleComment, Long> {
    
    /**
     * 根据文章ID查找评论列表
     * 
     * @param articleId 文章ID
     * @return 评论列表
     */
    List<TcmArticleComment> findByArticleId(Long articleId);
    
    /**
     * 根据文章ID查找评论列表（分页）
     * 
     * @param articleId 文章ID
     * @param pageable 分页参数
     * @return 评论分页结果
     */
    Page<TcmArticleComment> findByArticleId(Long articleId, Pageable pageable);
    
    /**
     * 根据文章ID和状态查找评论列表（分页）
     * 
     * @param articleId 文章ID
     * @param status 状态
     * @param pageable 分页参数
     * @return 评论分页结果
     */
    Page<TcmArticleComment> findByArticleIdAndStatus(Long articleId, Integer status, Pageable pageable);
    
    /**
     * 根据文章ID和状态查找评论列表并按创建时间降序排序
     * 
     * @param articleId 文章ID
     * @param status 状态
     * @return 评论列表
     */
    List<TcmArticleComment> findByArticleIdAndStatusOrderByCreatedAtDesc(Long articleId, Integer status);
    
    /**
     * 根据文章ID和状态查找评论列表并按创建时间降序排序（分页）
     * 
     * @param articleId 文章ID
     * @param status 状态
     * @param pageable 分页参数
     * @return 评论分页结果
     */
    Page<TcmArticleComment> findByArticleIdAndStatusOrderByCreatedAtDesc(Long articleId, Integer status, Pageable pageable);
    
    /**
     * 根据文章ID查找顶级评论（parentId=0）
     * 
     * @param articleId 文章ID
     * @param status 状态
     * @param pageable 分页参数
     * @return 顶级评论分页结果
     */
    Page<TcmArticleComment> findByArticleIdAndParentIdAndStatus(Long articleId, Long parentId, Integer status, Pageable pageable);
    
    /**
     * 根据父评论ID查找子评论
     * 
     * @param parentId 父评论ID
     * @param status 状态
     * @return 子评论列表
     */
    List<TcmArticleComment> findByParentIdAndStatus(Long parentId, Integer status);
    
    /**
     * 根据父评论ID和状态查找子评论并按创建时间降序排序
     * 
     * @param parentId 父评论ID
     * @param status 状态
     * @return 子评论列表
     */
    List<TcmArticleComment> findByParentIdAndStatusOrderByCreatedAtDesc(Long parentId, Integer status);
    
    /**
     * 根据用户ID查找评论列表（分页）
     * 
     * @param userId 用户ID
     * @param pageable 分页参数
     * @return 评论分页结果
     */
    Page<TcmArticleComment> findByUserId(Long userId, Pageable pageable);
    
    /**
     * 根据用户ID和状态查找评论列表（分页）
     * 
     * @param userId 用户ID
     * @param status 状态
     * @param pageable 分页参数
     * @return 评论分页结果
     */
    Page<TcmArticleComment> findByUserIdAndStatus(Long userId, Integer status, Pageable pageable);
    
    /**
     * 根据用户ID和状态查找评论列表并按创建时间降序排序
     * 
     * @param userId 用户ID
     * @param status 状态
     * @return 评论列表
     */
    List<TcmArticleComment> findByUserIdAndStatusOrderByCreatedAtDesc(Long userId, Integer status);
    
    /**
     * 根据用户ID和状态查找评论列表并按创建时间降序排序（分页）
     * 
     * @param userId 用户ID
     * @param status 状态
     * @param pageable 分页参数
     * @return 评论分页结果
     */
    Page<TcmArticleComment> findByUserIdAndStatusOrderByCreatedAtDesc(Long userId, Integer status, Pageable pageable);
    
    /**
     * 统计用户评论数量
     * 
     * @param userId 用户ID
     * @param status 状态
     * @return 评论数量
     */
    long countByUserIdAndStatus(Long userId, Integer status);
    
    /**
     * 统计文章评论数量
     * 
     * @param articleId 文章ID
     * @param status 状态
     * @return 评论数量
     */
    long countByArticleIdAndStatus(Long articleId, Integer status);
    
    /**
     * 统计父评论的子评论数量
     * 
     * @param parentId 父评论ID
     * @param status 状态
     * @return 子评论数量
     */
    long countByParentIdAndStatus(Long parentId, Integer status);
    
    /**
     * 更新评论状态
     * 
     * @param id 评论ID
     * @param status 状态
     */
    @Modifying
    @Query("UPDATE TcmArticleComment c SET c.status = :status WHERE c.id = :id")
    void updateStatus(@Param("id") Long id, @Param("status") Integer status);
    
    /**
     * 批量更新评论状态
     * 
     * @param ids 评论ID列表
     * @param status 状态
     */
    @Modifying
    @Query("UPDATE TcmArticleComment c SET c.status = :status WHERE c.id IN :ids")
    void updateStatusBatch(@Param("ids") List<Long> ids, @Param("status") Integer status);
    
    /**
     * 增加评论点赞数
     * 
     * @param id 评论ID
     */
    @Modifying
    @Query("UPDATE TcmArticleComment c SET c.likeCount = c.likeCount + 1 WHERE c.id = :id")
    void incrementLikeCount(@Param("id") Long id);
    
    /**
     * 减少评论点赞数
     * 
     * @param id 评论ID
     */
    @Modifying
    @Query("UPDATE TcmArticleComment c SET c.likeCount = c.likeCount - 1 WHERE c.id = :id AND c.likeCount > 0")
    void decrementLikeCount(@Param("id") Long id);
    
    /**
     * 根据文章ID删除所有评论
     * 
     * @param articleId 文章ID
     */
    @Modifying
    @Query("DELETE FROM TcmArticleComment c WHERE c.articleId = :articleId")
    void deleteByArticleId(@Param("articleId") Long articleId);
    
    /**
     * 根据用户ID删除所有评论
     * 
     * @param userId 用户ID
     */
    @Modifying
    @Query("DELETE FROM TcmArticleComment c WHERE c.userId = :userId")
    void deleteByUserId(@Param("userId") Long userId);
}