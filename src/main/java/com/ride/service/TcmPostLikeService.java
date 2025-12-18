package com.ride.service;

import com.ride.dto.TcmPostLikeDTO;
import com.ride.entity.TcmPostLike;

import java.util.List;

/**
 * 医药论坛帖子点赞业务逻辑接口
 * 
 * @author 千行团队
 * @version 1.0.0
 */
public interface TcmPostLikeService {
    
    /**
     * 点赞帖子
     * 
     * @param postLike 点赞记录实体
     * @return 点赞信息
     */
    TcmPostLikeDTO createPostLike(TcmPostLike postLike);
    
    /**
     * 根据ID查询点赞记录
     * 
     * @param id 点赞ID
     * @return 点赞信息
     */
    TcmPostLikeDTO getPostLikeById(Long id);
    
    /**
     * 根据用户ID查询点赞记录
     * 
     * @param userId 用户ID
     * @return 点赞记录列表
     */
    List<TcmPostLikeDTO> getPostLikesByUserId(Long userId);
    
    /**
     * 根据帖子ID查询点赞记录
     * 
     * @param postId 帖子ID
     * @return 点赞记录列表
     */
    List<TcmPostLikeDTO> getPostLikesByPostId(Long postId);
    
    /**
     * 查询所有点赞记录
     * 
     * @return 点赞记录列表
     */
    List<TcmPostLikeDTO> getAllPostLikes();
    
    /**
     * 根据用户ID和帖子ID查询点赞记录
     * 
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 点赞记录列表
     */
    List<TcmPostLikeDTO> getPostLikesByUserIdAndPostId(Long userId, Long postId);
    
    /**
     * 删除点赞记录
     * 
     * @param id 点赞ID
     */
    void deletePostLike(Long id);
    
    /**
     * 根据用户ID和帖子ID删除点赞记录
     * 
     * @param userId 用户ID
     * @param postId 帖子ID
     */
    void deletePostLikeByUserIdAndPostId(Long userId, Long postId);
    
    /**
     * 根据用户和帖子删除点赞记录
     * 
     * @param userId 用户ID
     * @param postId 帖子ID
     */
    void deletePostLikeByUserAndPost(Long userId, Long postId);
    
    /**
     * 根据帖子ID删除所有点赞记录
     * 
     * @param postId 帖子ID
     */
    void deletePostLikesByPostId(Long postId);
    
    /**
     * 取消点赞
     * 
     * @param userId 用户ID
     * @param postId 帖子ID
     */
    void removePostLike(Long userId, Long postId);
    
    /**
     * 批量删除点赞记录
     * 
     * @param ids 点赞ID列表
     */
    void batchDeletePostLikes(List<Long> ids);
    
    /**
     * 取消点赞
     * 
     * @param userId 用户ID
     * @param postId 帖子ID
     */
    void unlikePost(Long userId, Long postId);
    
    /**
     * 检查用户是否点赞了帖子
     * 
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 是否点赞
     */
    boolean hasUserLikedPost(Long userId, Long postId);
    
    /**
     * 检查用户是否点赞了帖子
     * 
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 是否点赞
     */
    boolean hasLikedPost(Long userId, Long postId);
    
    /**
     * 检查用户是否点赞了帖子
     * 
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 是否点赞
     */
    boolean existsByUserIdAndPostId(Long userId, Long postId);
    
    /**
     * 根据用户和帖子获取点赞记录
     * 
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 点赞记录
     */
    TcmPostLikeDTO getPostLikeByUserAndPost(Long userId, Long postId);
    
    /**
     * 获取点赞总数统计
     * 
     * @return 总点赞数
     */
    long getTotalLikeCount();
    
    /**
     * 根据帖子ID获取点赞数量
     * 
     * @param postId 帖子ID
     * @return 点赞数量
     */
    long getPostLikeCount(Long postId);
    
    /**
     * 获取用户的点赞统计
     * 
     * @param userId 用户ID
     * @return 点赞数量
     */
    long getUserLikeCount(Long userId);
    
    /**
     * 根据用户ID获取点赞数量
     * 
     * @param userId 用户ID
     * @return 点赞数量
     */
    long getPostLikeCountByUserId(Long userId);
}
