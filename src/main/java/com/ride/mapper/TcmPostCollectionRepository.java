package com.ride.mapper;

import com.ride.entity.TcmPostCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 医药论坛帖子收藏数据访问层
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@Repository
public interface TcmPostCollectionRepository extends JpaRepository<TcmPostCollection, Long> {
    
    /**
     * 根据用户ID查找收藏记录
     * 
     * @param userId 用户ID
     * @return 收藏记录列表
     */
    List<TcmPostCollection> findByUserId(Long userId);
    
    /**
     * 根据帖子ID查找收藏记录
     * 
     * @param postId 帖子ID
     * @return 收藏记录列表
     */
    List<TcmPostCollection> findByPostId(Long postId);
    
    /**
     * 检查用户是否收藏了帖子
     * 
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 是否收藏
     */
    boolean existsByUserIdAndPostId(Long userId, Long postId);
    
    /**
     * 根据用户ID和帖子ID查找收藏记录
     * 
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 收藏记录列表
     */
    List<TcmPostCollection> findByUserIdAndPostId(Long userId, Long postId);
    
    /**
     * 取消收藏
     * 
     * @param userId 用户ID
     * @param postId 帖子ID
     */
    void deleteByUserIdAndPostId(Long userId, Long postId);
    
    /**
     * 根据帖子ID删除收藏记录
     * 
     * @param postId 帖子ID
     */
    @Modifying
    @Query("DELETE FROM TcmPostCollection pc WHERE pc.postId = :postId")
    void deleteByPostId(@Param("postId") Long postId);
    
    /**
     * 统计用户的收藏数量
     * 
     * @param userId 用户ID
     * @return 收藏数量
     */
    @Query("SELECT COUNT(pc) FROM TcmPostCollection pc WHERE pc.userId = :userId")
    long countByUserId(@Param("userId") Long userId);
    
    /**
     * 统计帖子的被收藏数量
     * 
     * @param postId 帖子ID
     * @return 被收藏数量
     */
    @Query("SELECT COUNT(pc) FROM TcmPostCollection pc WHERE pc.postId = :postId")
    long countByPostId(@Param("postId") Long postId);
}
