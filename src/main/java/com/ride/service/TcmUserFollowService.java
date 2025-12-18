package com.ride.service;

import com.ride.dto.TcmUserFollowDTO;
import com.ride.entity.TcmUserFollow;

import java.util.List;

/**
 * 医药论坛用户关注业务逻辑接口
 * 
 * @author 千行团队
 * @version 1.0.0
 */
public interface TcmUserFollowService {
    
    /**
     * 关注用户
     * 
     * @param userFollow 用户关注实体
     * @return 关注信息
     */
    TcmUserFollowDTO createUserFollow(TcmUserFollow userFollow);
    
    /**
     * 根据ID查询关注记录
     * 
     * @param id 关注ID
     * @return 关注信息
     */
    TcmUserFollowDTO getUserFollowById(Long id);
    
    /**
     * 根据关注者ID和被关注者ID查询关注记录
     * 
     * @param followerId 关注者ID
     * @param followingId 被关注者ID
     * @return 关注记录列表
     */
    List<TcmUserFollowDTO> getUserFollowsByFollowerIdAndFollowingId(Long followerId, Long followingId);
    
    /**
     * 根据关注者ID和被关注者ID查询单个关注记录
     * 
     * @param followerId 关注者ID
     * @param followingId 被关注者ID
     * @return 单个关注记录，如果没有找到则返回null
     */
    TcmUserFollowDTO getUserFollowByFollowerAndFollowed(Long followerId, Long followingId);
    
    /**
     * 根据关注者ID查询关注列表
     * 
     * @param followerId 关注者ID
     * @return 关注列表
     */
    List<TcmUserFollowDTO> getUserFollowsByFollowerId(Long followerId);
    
    /**
     * 根据被关注者ID查询粉丝列表
     * 
     * @param followingId 被关注者ID
     * @return 粉丝列表
     */
    List<TcmUserFollowDTO> getUserFollowsByFollowingId(Long followingId);
    
    /**
     * 查询所有关注记录
     * 
     * @return 关注记录列表
     */
    List<TcmUserFollowDTO> getAllUserFollows();
    
    /**
     * 根据ID删除关注记录
     * 
     * @param id 关注记录ID
     */
    void deleteUserFollow(Long id);
    
    /**
     * 根据关注者ID和被关注者ID删除关注记录
     * 
     * @param followerId 关注者ID
     * @param followingId 被关注者ID
     */
    void deleteUserFollowByFollowerIdAndFollowingId(Long followerId, Long followingId);
    
    /**
     * 取消关注
     * 
     * @param followerId 关注者ID
     * @param followingId 被关注者ID
     */
    void unfollow(Long followerId, Long followingId);
    
    /**
     * 批量删除关注记录
     * 
     * @param ids 关注记录ID列表
     */
    void batchDeleteUserFollows(List<Long> ids);
    
    /**
     * 检查关注关系是否存在
     * 
     * @param followerId 关注者ID
     * @param followingId 被关注者ID
     * @return 是否存在关注关系
     */
    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);
    
    /**
     * 检查用户是否关注了另一个用户
     * 
     * @param followerId 关注者ID
     * @param followingId 被关注者ID
     * @return 是否关注
     */
    boolean isFollowing(Long followerId, Long followingId);
    
    /**
     * 获取用户的关注数量
     * 
     * @param userId 用户ID
     * @return 关注数量
     */
    long getFollowCount(Long userId);
    
    /**
     * 获取用户的粉丝数量
     * 
     * @param userId 用户ID
     * @return 粉丝数量
     */
    long getFollowerCount(Long userId);
    
    /**
     * 获取用户的关注统计
     * 
     * @param followerId 关注者ID
     * @return 关注数量
     */
    long getUserFollowCount(Long followerId);
    
    /**
     * 获取用户的粉丝统计
     * 
     * @param followingId 被关注者ID
     * @return 粉丝数量
     */
    long getUserFansCount(Long followingId);
}
