package com.ride.service;

import com.ride.dto.TcmReplyDTO;
import com.ride.entity.TcmReply;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 医药论坛回复业务逻辑接口
 * 
 * @author 千行团队
 * @version 1.0.0
 */
public interface TcmReplyService {
    
    /**
     * 创建回复
     * 
     * @param reply 回复实体
     * @return 回复信息
     */
    TcmReplyDTO createReply(TcmReply reply);
    
    /**
     * 根据ID查询回复
     * 
     * @param id 回复ID
     * @return 回复信息
     */
    TcmReplyDTO getReplyById(Long id);
    
    /**
     * 根据帖子ID查询回复列表
     * 
     * @param postId 帖子ID
     * @return 回复列表
     */
    List<TcmReplyDTO> getRepliesByPostId(Long postId);
    
    /**
     * 根据帖子ID分页查询回复列表
     * 
     * @param postId 帖子ID
     * @param page 页码
     * @param size 每页数量
     * @return 分页回复列表
     */
    Page<TcmReplyDTO> getRepliesByPostIdWithPagination(Long postId, int page, int size);
    
    /**
     * 根据用户ID查询回复
     * 
     * @param userId 用户ID
     * @return 回复列表
     */
    List<TcmReplyDTO> getRepliesByUserId(Long userId);
    
    /**
     * 根据用户ID分页查询回复列表
     * 
     * @param userId 用户ID
     * @param page 页码
     * @param size 每页数量
     * @return 分页回复列表
     */
    Page<TcmReplyDTO> getRepliesByUserIdWithPagination(Long userId, int page, int size);
    
    /**
     * 查询所有回复
     * 
     * @return 回复列表
     */
    List<TcmReplyDTO> getAllReplies();
    
    /**
     * 根据状态查询回复
     * 
     * @param status 状态
     * @return 回复列表
     */
    List<TcmReplyDTO> getRepliesByStatus(Integer status);
    
    /**
     * 查询帖子的回复（按时间倒序）
     * 
     * @param postId 帖子ID
     * @param status 状态
     * @return 回复列表
     */
    List<TcmReplyDTO> getRepliesByPostIdOrderByTime(Long postId, Integer status);
    
    /**
     * 更新回复信息
     * 
     * @param id 回复ID
     * @param reply 回复信息
     * @return 更新后的回复信息
     */
    TcmReplyDTO updateReply(Long id, TcmReply reply);
    
    /**
     * 删除回复
     * 
     * @param id 回复ID
     */
    void deleteReply(Long id);
    
    /**
     * 批量删除回复
     * 
     * @param ids 回复ID列表
     */
    void batchDeleteReplies(List<Long> ids);
    
    /**
     * 根据用户ID和帖子ID查询回复
     * 
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 回复列表
     */
    List<TcmReplyDTO> getRepliesByUserIdAndPostId(Long userId, Long postId);
    
    /**
     * 删除帖子下的所有回复
     * 
     * @param postId 帖子ID
     */
    void deleteRepliesByPostId(Long postId);
    
    /**
     * 检查用户是否对帖子进行了回复
     * 
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 是否回复
     */
    boolean hasUserRepliedToPost(Long userId, Long postId);
    
    /**
     * 获取帖子的回复数量统计
     * 
     * @param postId 帖子ID
     * @return 回复数量
     */
    long getReplyCountByPostId(Long postId);
    
    /**
     * 获取用户回复统计
     * 
     * @param userId 用户ID
     * @return 回复数量
     */
    long getReplyCountByUserId(Long userId);
    
    /**
     * 获取帖子的回复数量统计
     * 
     * @param postId 帖子ID
     * @return 回复数量
     */
    long getReplyCountByPost(Long postId);
    
    /**
     * 获取用户回复数量统计
     * 
     * @param userId 用户ID
     * @return 回复数量
     */
    long getReplyCountByUser(Long userId);
    
    /**
     * 获取总回复数量统计
     * 
     * @return 总回复数量
     */
    long getTotalReplyCount();
    
    /**
     * 获取帖子的回复数量统计（按状态）
     * 
     * @param postId 帖子ID
     * @param status 状态
     * @return 回复数量
     */
    long getReplyCountByPostIdAndStatus(Long postId, Integer status);
}
