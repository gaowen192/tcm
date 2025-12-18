package com.ride.mapper;

import com.ride.entity.TcmPostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 医药论坛帖子点赞数据访问层
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@Repository
public interface TcmPostLikeRepository extends JpaRepository<TcmPostLike, Long> {
    
    /**
     * 根据用户ID查找点赞记录
     * 
     * @param userId 用户ID
     * @return 点赞记录列表
     */
    List<TcmPostLike> findByUserId(Long userId);
    
    /**
     * 根据帖子ID查找点赞记录
     * 
     * @param postId 帖子ID
     * @return 点赞记录列表
     */
    List<TcmPostLike> findByPostId(Long postId);
    
    /**
     * 检查用户是否已点赞帖子
     * 
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 是否已点赞
     */
    boolean existsByUserIdAndPostId(Long userId, Long postId);
    
    /**
     * 根据用户ID和帖子ID查找点赞记录
     * 
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 点赞记录列表
     */
    java.util.Optional<TcmPostLike> findByUserIdAndPostId(Long userId, Long postId);
    
    /**
     * 统计帖子的点赞数量
     * 
     * @param postId 帖子ID
     * @return 点赞数量
     */
    @Query("SELECT COUNT(pl) FROM TcmPostLike pl WHERE pl.postId = :postId")
    long countByPostId(@Param("postId") Long postId);
    
    /**
     * 统计用户的点赞数量
     * 
     * @param userId 用户ID
     * @return 点赞数量
     */
    @Query("SELECT COUNT(pl) FROM TcmPostLike pl WHERE pl.userId = :userId")
    long countByUserId(@Param("userId") Long userId);
    
    /**
     * 批量删除用户对特定帖子的点赞记录
     * 
     * @param userId 用户ID
     * @param postId 帖子ID
     */
    void deleteByUserIdAndPostId(Long userId, Long postId);
}
