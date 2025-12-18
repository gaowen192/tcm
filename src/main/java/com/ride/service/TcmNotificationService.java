package com.ride.service;

import com.ride.entity.TcmNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 系统通知业务逻辑层
 * 
 * @author 千行团队
 * @version 1.0.0
 */
public interface TcmNotificationService {
    
    /**
     * 创建通知
     * 
     * @param notification 通知实体
     * @return 创建的通知
     */
    TcmNotification createNotification(TcmNotification notification);
    
    /**
     * 创建系统消息
     * 
     * @param userId 接收用户ID
     * @param title 标题
     * @param content 内容
     * @return 创建的通知
     */
    TcmNotification createSystemNotification(Long userId, String title, String content);
    
    /**
     * 创建资源查看通知
     * 
     * @param userId 接收用户ID
     * @param type 资源类型(post/article/video)
     * @param title 资源标题
     * @param relatedId 资源ID
     * @param fromUserId 查看用户ID
     * @return 创建的通知
     */
    TcmNotification createViewNotification(Long userId, String type, String title, Long relatedId, Long fromUserId);
    
    /**
     * 创建资源评论通知
     * 
     * @param userId 接收用户ID
     * @param type 资源类型(post/article/video)
     * @param title 资源标题
     * @param content 评论内容
     * @param relatedId 资源ID
     * @param fromUserId 评论用户ID
     * @return 创建的通知
     */
    TcmNotification createCommentNotification(Long userId, String type, String title, String content, Long relatedId, Long fromUserId);
    
    /**
     * 创建资源收藏通知
     * 
     * @param userId 接收用户ID
     * @param type 资源类型(post/article/video)
     * @param title 资源标题
     * @param relatedId 资源ID
     * @param fromUserId 收藏用户ID
     * @return 创建的通知
     */
    TcmNotification createCollectNotification(Long userId, String type, String title, Long relatedId, Long fromUserId);
    
    /**
     * 根据用户ID查询通知列表（分页）
     * 
     * @param userId 用户ID
     * @param pageable 分页参数
     * @return 分页通知列表
     */
    Page<TcmNotification> getNotificationsByUserId(Long userId, Pageable pageable);
    
    /**
     * 查询用户未读通知数量
     * 
     * @param userId 用户ID
     * @return 未读通知数量
     */
    long getUnreadNotificationCount(Long userId);
    
    /**
     * 标记单个通知为已读
     * 
     * @param notificationId 通知ID
     * @return 更新后的通知
     */
    TcmNotification markAsRead(Long notificationId);
    
    /**
     * 批量标记通知为已读
     * 
     * @param notificationIds 通知ID列表
     * @return 更新的记录数
     */
    int batchMarkAsRead(List<Long> notificationIds);
    
    /**
     * 标记所有通知为已读
     * 
     * @param userId 用户ID
     * @return 更新的记录数
     */
    int markAllAsRead(Long userId);
}
