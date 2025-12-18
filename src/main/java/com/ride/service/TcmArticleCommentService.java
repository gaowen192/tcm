package com.ride.service;

import com.ride.dto.TcmArticleCommentDTO;
import com.ride.entity.TcmArticleComment;
import java.util.List;
import java.util.Map;

/**
 * 文章评论Service接口
 * 
 * @author 千行团队
 * @version 1.0.0
 */
public interface TcmArticleCommentService {
    
    /**
     * 创建评论
     * 
     * @param comment 评论对象
     * @return 创建后的评论对象
     */
    TcmArticleComment createComment(TcmArticleComment comment);
    
    /**
     * 根据ID获取评论
     * 
     * @param id 评论ID
     * @return 评论对象
     */
    TcmArticleComment getCommentById(Long id);
    
    /**
     * 根据ID获取评论DTO（包含用户信息）
     * 
     * @param id 评论ID
     * @return 评论DTO
     */
    TcmArticleCommentDTO getCommentDTOById(Long id);
    
    /**
     * 更新评论
     * 
     * @param comment 评论对象
     * @return 更新后的评论对象
     */
    TcmArticleComment updateComment(TcmArticleComment comment);
    
    /**
     * 删除评论
     * 
     * @param id 评论ID
     */
    void deleteComment(Long id);
    
    /**
     * 根据文章ID获取评论列表
     * 
     * @param articleId 文章ID
     * @param status 状态：0-禁用，1-启用
     * @return 评论列表
     */
    List<TcmArticleCommentDTO> getCommentsByArticleId(Long articleId, Integer status);
    
    /**
     * 根据文章ID获取评论列表并分页
     * 
     * @param articleId 文章ID
     * @param status 状态：0-禁用，1-启用
     * @param page 页码
     * @param pageSize 每页数量
     * @return 包含评论列表和分页信息的Map
     */
    Map<String, Object> getCommentsByArticleIdWithPage(Long articleId, Integer status, int page, int pageSize);
    
    /**
     * 根据用户ID获取评论列表
     * 
     * @param userId 用户ID
     * @param status 状态：0-禁用，1-启用
     * @return 评论列表
     */
    List<TcmArticleCommentDTO> getCommentsByUserId(Long userId, Integer status);
    
    /**
     * 根据用户ID获取评论列表并分页
     * 
     * @param userId 用户ID
     * @param status 状态：0-禁用，1-启用
     * @param page 页码
     * @param pageSize 每页数量
     * @return 包含评论列表和分页信息的Map
     */
    Map<String, Object> getCommentsByUserIdWithPage(Long userId, Integer status, int page, int pageSize);
    
    /**
     * 根据父评论ID获取子评论列表
     * 
     * @param parentId 父评论ID
     * @param status 状态：0-禁用，1-启用
     * @return 子评论列表
     */
    List<TcmArticleCommentDTO> getCommentsByParentId(Long parentId, Integer status);
    
    /**
     * 更新评论状态
     * 
     * @param id 评论ID
     * @param status 状态：0-禁用，1-启用
     * @return 更新后的评论对象
     */
    TcmArticleComment updateCommentStatus(Long id, Integer status);
    
    /**
     * 增加评论点赞数
     * 
     * @param id 评论ID
     * @return 更新后的点赞数
     */
    Long incrementCommentLikeCount(Long id);
    
    /**
     * 减少评论点赞数
     * 
     * @param id 评论ID
     * @return 更新后的点赞数
     */
    Long decrementCommentLikeCount(Long id);
    
    /**
     * 统计文章的评论数
     * 
     * @param articleId 文章ID
     * @param status 状态：0-禁用，1-启用
     * @return 评论数
     */
    Long countCommentsByArticleId(Long articleId, Integer status);
    
    /**
     * 统计用户的评论数
     * 
     * @param userId 用户ID
     * @param status 状态：0-禁用，1-启用
     * @return 评论数
     */
    Long countCommentsByUserId(Long userId, Integer status);
}