package com.ride.mapper;

import com.ride.entity.TcmReply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 医药论坛回复数据访问层
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@Repository
public interface TcmReplyRepository extends JpaRepository<TcmReply, Long> {
    
    /**
     * 根据帖子ID查询回复列表
     * 
     * @param postId 帖子ID
     * @return 回复列表
     */
    List<TcmReply> findByPostId(Long postId);
    
    /**
     * 根据帖子ID分页查询回复列表，并获取用户名
     * 
     * @param postId 帖子ID
     * @param pageable 分页参数
     * @return 包含回复信息和用户名的列表
     */
    @Query(value = "SELECT r.*, u.username FROM tcm_replies r " +
                   "LEFT JOIN tcm_users u ON r.user_id = u.id " +
                   "WHERE r.post_id = :postId AND r.status = 1 " +
                   "ORDER BY r.created_at DESC",
           countQuery = "SELECT COUNT(*) FROM tcm_replies r WHERE r.post_id = :postId AND r.status = 1",
           nativeQuery = true)
    Page<Map<String, Object>> findRepliesWithUsernameByPostId(@Param("postId") Long postId, Pageable pageable);
    
    /**
     * 根据用户ID分页查询回复列表，并获取用户名
     * 
     * @param userId 用户ID
     * @param pageable 分页参数
     * @return 包含回复信息和用户名的列表
     */
    @Query(value = "SELECT r.*, u.username FROM tcm_replies r " +
                   "LEFT JOIN tcm_users u ON r.user_id = u.id " +
                   "WHERE r.user_id = :userId AND r.status = 1 " +
                   "ORDER BY r.created_at DESC",
           countQuery = "SELECT COUNT(*) FROM tcm_replies r WHERE r.user_id = :userId AND r.status = 1",
           nativeQuery = true)
    Page<Map<String, Object>> findRepliesWithUsernameByUserId(@Param("userId") Long userId, Pageable pageable);
    
    /**
     * 根据用户ID查找回复列表
     * 
     * @param userId 用户ID
     * @return 回复列表
     */
    List<TcmReply> findByUserId(Long userId);
    
    /**
     * 根据状态查找回复列表
     * 
     * @param status 状态
     * @return 回复列表
     */
    List<TcmReply> findByStatus(Integer status);
    
    /**
     * 根据帖子ID和状态查找回复列表，按创建时间倒序
     * 
     * @param postId 帖子ID
     * @param status 状态
     * @return 回复列表
     */
    List<TcmReply> findByPostIdAndStatusOrderByCreatedAtDesc(Long postId, Integer status);
    
    /**
     * 根据父回复ID查找子回复列表
     * 
     * @param parentId 父回复ID
     * @return 回复列表
     */
    List<TcmReply> findByParentId(Long parentId);
    
    /**
     * 根据用户ID和帖子ID查找回复列表
     * 
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 回复列表
     */
    List<TcmReply> findByUserIdAndPostId(Long userId, Long postId);
    
    /**
     * 根据帖子ID删除回复
     * 
     * @param postId 帖子ID
     */
    void deleteByPostId(Long postId);
    
    /**
     * 统计帖子的回复数量
     * 
     * @param postId 帖子ID
     * @return 回复数量
     */
    long countByPostId(Long postId);
    
    /**
     * 统计用户的回复数量
     * 
     * @param userId 用户ID
     * @return 回复数量
     */
    long countByUserId(Long userId);
    
    /**
     * 统计帖子特定状态的回复数量
     * 
     * @param postId 帖子ID
     * @param status 状态
     * @return 回复数量
     */
    long countByPostIdAndStatus(Long postId, Integer status);
    
    /**
     * 统计帖子的回复数量
     * 
     * @param postId 帖子ID
     * @return 回复数量
     */
    @Query("SELECT COUNT(r) FROM TcmReply r WHERE r.postId = :postId AND r.status = 1")
    long countRepliesByPostId(@Param("postId") Long postId);
    
    /**
     * 统计用户的回复数量
     * 
     * @param userId 用户ID
     * @return 回复数量
     */
    @Query("SELECT COUNT(r) FROM TcmReply r WHERE r.userId = :userId AND r.status = 1")
    long countRepliesByUserId(@Param("userId") Long userId);
}
