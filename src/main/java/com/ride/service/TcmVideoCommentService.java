package com.ride.service;

import com.ride.entity.TcmVideoComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 医药论坛视频评论服务接口
 * 
 * @author 千行团队
 * @version 1.0.0
 */
public interface TcmVideoCommentService {
    
    /**
     * 创建视频评论
     * 
     * @param comment 评论实体
     * @return 创建的评论实体
     */
    TcmVideoComment createComment(TcmVideoComment comment);
    
    /**
     * 获取评论详情
     * 
     * @param id 评论ID
     * @return 评论实体
     */
    TcmVideoComment getCommentById(Long id);
    
    /**
     * 更新评论内容
     * 
     * @param id 评论ID
     * @param content 新评论内容
     * @return 更新后的评论实体
     */
    TcmVideoComment updateComment(Long id, String content);
    
    /**
     * 删除评论（软删除）
     * 
     * @param id 评论ID
     */
    void deleteComment(Long id);
    
    /**
     * 根据视频ID获取评论列表
     * 
     * @param videoId 视频ID
     * @return 评论列表
     */
    List<TcmVideoComment> getCommentsByVideoId(Long videoId);
    
    /**
     * 根据视频ID分页获取评论列表
     * 
     * @param videoId 视频ID
     * @param pageable 分页参数
     * @return 分页评论列表
     */
    Page<TcmVideoComment> getCommentsByVideoId(Long videoId, Pageable pageable);
    
    /**
     * 根据用户ID获取评论列表
     * 
     * @param userId 用户ID
     * @return 评论列表
     */
    List<TcmVideoComment> getCommentsByUserId(Long userId);
    
    /**
     * 根据用户ID分页获取评论列表
     * 
     * @param userId 用户ID
     * @param pageable 分页参数
     * @return 分页评论列表
     */
    Page<TcmVideoComment> getCommentsByUserId(Long userId, Pageable pageable);
    
    /**
     * 获取评论的回复列表
     * 
     * @param commentId 评论ID
     * @return 回复列表
     */
    List<TcmVideoComment> getRepliesByCommentId(Long commentId);
    
    /**
     * 增加评论点赞数
     * 
     * @param commentId 评论ID
     */
    void likeComment(Long commentId);
    
    /**
     * 减少评论点赞数
     * 
     * @param commentId 评论ID
     */
    void unlikeComment(Long commentId);
    
    /**
     * 根据视频ID统计评论数量
     * 
     * @param videoId 视频ID
     * @return 评论数量
     */
    long countCommentsByVideoId(Long videoId);
    
    /**
     * 根据用户ID统计评论数量
     * 
     * @param userId 用户ID
     * @return 评论数量
     */
    long countCommentsByUserId(Long userId);
    
    /**
     * 验证评论是否属于指定用户
     * 
     * @param commentId 评论ID
     * @param userId 用户ID
     * @return 是否属于该用户
     */
    boolean isCommentOwnedByUser(Long commentId, Long userId);
}