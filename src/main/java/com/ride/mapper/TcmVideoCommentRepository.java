package com.ride.mapper;

import com.ride.entity.TcmVideoComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 医药论坛视频评论数据访问层
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@Repository
public interface TcmVideoCommentRepository extends JpaRepository<TcmVideoComment, Long> {
    
    /**
     * 根据视频ID查找评论列表，按创建时间降序排列
     * 
     * @param videoId 视频ID
     * @return 评论列表
     */
    List<TcmVideoComment> findByVideoIdAndStatusOrderByCreatedAtDesc(Long videoId, Integer status);
    
    /**
     * 根据视频ID分页查找评论列表，按创建时间降序排列
     * 
     * @param videoId 视频ID
     * @param status 状态
     * @param pageable 分页参数
     * @return 分页评论列表
     */
    Page<TcmVideoComment> findByVideoIdAndStatusOrderByCreatedAtDesc(Long videoId, Integer status, Pageable pageable);
    
    /**
     * 根据用户ID查找评论列表
     * 
     * @param userId 用户ID
     * @return 评论列表
     */
    List<TcmVideoComment> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    /**
     * 根据用户ID分页查找评论列表
     * 
     * @param userId 用户ID
     * @param pageable 分页参数
     * @return 分页评论列表
     */
    Page<TcmVideoComment> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    
    /**
     * 根据回复的评论ID查找回复列表
     * 
     * @param replyToCommentId 被回复的评论ID
     * @return 回复列表
     */
    List<TcmVideoComment> findByReplyToCommentIdAndStatusOrderByCreatedAtAsc(Long replyToCommentId, Integer status);
    
    /**
     * 根据视频ID统计评论数量
     * 
     * @param videoId 视频ID
     * @return 评论数量
     */
    long countByVideoIdAndStatus(Long videoId, Integer status);
    
    /**
     * 根据用户ID统计评论数量
     * 
     * @param userId 用户ID
     * @return 评论数量
     */
    long countByUserId(Long userId);
    
    /**
     * 增加评论点赞数
     * 
     * @param id 评论ID
     */
    @Modifying
    @Query("UPDATE TcmVideoComment c SET c.likeCount = c.likeCount + 1 WHERE c.id = :id")
    void incrementLikeCount(@Param("id") Long id);
    
    /**
     * 减少评论点赞数
     * 
     * @param id 评论ID
     */
    @Modifying
    @Query("UPDATE TcmVideoComment c SET c.likeCount = c.likeCount - 1 WHERE c.id = :id AND c.likeCount > 0")
    void decrementLikeCount(@Param("id") Long id);
    
    /**
     * 根据视频ID和状态批量删除评论
     * 
     * @param videoId 视频ID
     * @param status 状态
     */
    @Modifying
    @Query("DELETE FROM TcmVideoComment c WHERE c.videoId = :videoId AND c.status = :status")
    void deleteByVideoIdAndStatus(@Param("videoId") Long videoId, @Param("status") Integer status);
    
    /**
     * 根据用户ID和状态批量删除评论
     * 
     * @param userId 用户ID
     * @param status 状态
     */
    @Modifying
    @Query("DELETE FROM TcmVideoComment c WHERE c.userId = :userId AND c.status = :status")
    void deleteByUserIdAndStatus(@Param("userId") Long userId, @Param("status") Integer status);
}