package com.ride.service;

import com.ride.dto.TcmPostCollectionDTO;
import com.ride.entity.TcmPostCollection;

import java.util.List;

/**
 * 医药论坛帖子收藏业务逻辑接口
 * 
 * @author 千行团队
 * @version 1.0.0
 */
public interface TcmPostCollectionService {
    
    /**
     * 收藏帖子
     * 
     * @param postCollection 收藏记录实体
     * @return 收藏信息
     */
    TcmPostCollectionDTO createPostCollection(TcmPostCollection postCollection);
    
    /**
     * 根据ID查询收藏记录
     * 
     * @param id 收藏ID
     * @return 收藏信息
     */
    TcmPostCollectionDTO getPostCollectionById(Long id);
    
    /**
     * 根据用户ID查询收藏记录
     * 
     * @param userId 用户ID
     * @return 收藏记录列表
     */
    List<TcmPostCollectionDTO> getPostCollectionsByUserId(Long userId);
    
    /**
     * 根据帖子ID查询收藏记录
     * 
     * @param postId 帖子ID
     * @return 收藏记录列表
     */
    List<TcmPostCollectionDTO> getPostCollectionsByPostId(Long postId);
    
    /**
     * 根据用户ID和帖子ID查询收藏记录
     * 
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 收藏记录列表
     */
    List<TcmPostCollectionDTO> getPostCollectionsByUserIdAndPostId(Long userId, Long postId);
    
    /**
     * 查询所有收藏记录
     * 
     * @return 收藏记录列表
     */
    List<TcmPostCollectionDTO> getAllPostCollections();
    
    /**
     * 删除收藏记录
     * 
     * @param id 收藏ID
     */
    void deletePostCollection(Long id);
    
    /**
     * 根据用户ID和帖子ID删除收藏记录
     * 
     * @param userId 用户ID
     * @param postId 帖子ID
     */
    void deletePostCollectionByUserIdAndPostId(Long userId, Long postId);
    
    /**
     * 取消收藏
     * 
     * @param userId 用户ID
     * @param postId 帖子ID
     */
    void removePostCollection(Long userId, Long postId);
    
    /**
     * 批量取消收藏
     * 
     * @param userId 用户ID
     * @param postIds 帖子ID列表
     */
    void batchRemovePostCollections(Long userId, List<Long> postIds);
    
    /**
     * 检查用户是否收藏了帖子
     * 
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 是否收藏
     */
    boolean hasUserCollectedPost(Long userId, Long postId);
    
    /**
     * 获取用户的收藏统计
     * 
     * @param userId 用户ID
     * @return 收藏数量
     */
    long getUserCollectionCount(Long userId);
    
    /**
     * 获取帖子的被收藏统计
     * 
     * @param postId 帖子ID
     * @return 被收藏数量
     */
    long getPostCollectionCount(Long postId);
    
    /**
     * 检查用户是否收藏了帖子
     * 
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 是否收藏
     */
    boolean existsByUserIdAndPostId(Long userId, Long postId);
    
    /**
     * 根据帖子ID删除所有收藏记录
     * 
     * @param postId 帖子ID
     */
    void deletePostCollectionsByPostId(Long postId);
    
    /**
     * 根据用户和帖子删除收藏记录
     * 
     * @param userId 用户ID
     * @param postId 帖子ID
     */
    void deletePostCollectionByUserAndPost(Long userId, Long postId);
    
    /**
     * 获取收藏总数统计
     * 
     * @return 总收藏数
     */
    long getTotalCollectionCount();
    
    /**
     * 根据用户和帖子获取收藏记录
     * 
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 收藏记录
     */
    TcmPostCollectionDTO getPostCollectionByUserAndPost(Long userId, Long postId);
    
    /**
     * 批量删除收藏记录
     * 
     * @param ids 收藏ID列表
     */
    void batchDeletePostCollections(List<Long> ids);
}
