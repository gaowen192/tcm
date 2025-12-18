package com.ride.service.impl;

import com.ride.entity.TcmNotification;
import com.ride.mapper.TcmNotificationRepository;
import com.ride.service.TcmNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 系统通知业务逻辑实现类
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@Service
@Transactional
public class TcmNotificationServiceImpl implements TcmNotificationService {
    
    private static final Logger log = LoggerFactory.getLogger(TcmNotificationServiceImpl.class);
    
    @Autowired
    private TcmNotificationRepository tcmNotificationRepository;
    
    @Override
    public TcmNotification createNotification(TcmNotification notification) {
        log.debug("=============== Creating notification: {}", notification.getType());
        return tcmNotificationRepository.save(notification);
    }
    
    @Override
    public TcmNotification createSystemNotification(Long userId, String title, String content) {
        log.debug("=============== Creating system notification for user: {}, title: {}", userId, title);
        
        TcmNotification notification = new TcmNotification();
        notification.setUserId(userId);
        notification.setType("system");
        notification.setTitle(title);
        notification.setContent(content);
        notification.setFromUserId(0L); // 系统消息发送者ID为0
        notification.setIsRead(null); // 系统消息不设置已读未读
        
        return tcmNotificationRepository.save(notification);
    }
    
    @Override
    public TcmNotification createViewNotification(Long userId, String type, String title, Long relatedId, Long fromUserId) {
        log.debug("=============== Creating view notification for user: {}, type: {}", userId, type);
        
        String notificationType = type + "_view";
        String notificationTitle = "您的" + getTypeName(type) + "被查看了";
        String notificationContent = "用户查看了您的" + getTypeName(type) + ": " + title;
        
        return createResourceNotification(userId, notificationType, notificationTitle, notificationContent, type, relatedId, fromUserId);
    }
    
    @Override
    public TcmNotification createCommentNotification(Long userId, String type, String title, String content, Long relatedId, Long fromUserId) {
        log.debug("=============== Creating comment notification for user: {}, type: {}", userId, type);
        
        String notificationType = type + "_comment";
        String notificationTitle = "您的" + getTypeName(type) + "被评论了";
        String notificationContent = "用户评论了您的" + getTypeName(type) + ": " + title + "\n评论内容: " + content;
        
        return createResourceNotification(userId, notificationType, notificationTitle, notificationContent, type, relatedId, fromUserId);
    }
    
    @Override
    public TcmNotification createCollectNotification(Long userId, String type, String title, Long relatedId, Long fromUserId) {
        log.debug("=============== Creating collect notification for user: {}, type: {}", userId, type);
        
        String notificationType = type + "_collect";
        String notificationTitle = "您的" + getTypeName(type) + "被收藏了";
        String notificationContent = "用户收藏了您的" + getTypeName(type) + ": " + title;
        
        return createResourceNotification(userId, notificationType, notificationTitle, notificationContent, type, relatedId, fromUserId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<TcmNotification> getNotificationsByUserId(Long userId, Pageable pageable) {
        log.debug("=============== Getting notifications for user: {}, page: {}, pageSize: {}", userId, pageable.getPageNumber() + 1, pageable.getPageSize());
        return tcmNotificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getUnreadNotificationCount(Long userId) {
        log.debug("=============== Getting unread notification count for user: {}", userId);
        return tcmNotificationRepository.countUnreadNotifications(userId);
    }
    
    @Override
    public TcmNotification markAsRead(Long notificationId) {
        log.debug("=============== Marking notification as read: {}", notificationId);
        
        TcmNotification notification = tcmNotificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found with id: " + notificationId));
        
        // 系统消息不设置已读
        if (!"system".equals(notification.getType())) {
            notification.setIsRead(true);
            notification.setReadAt(LocalDateTime.now());
            return tcmNotificationRepository.save(notification);
        }
        
        return notification;
    }
    
    @Override
    public int batchMarkAsRead(List<Long> notificationIds) {
        log.debug("=============== Batch marking notifications as read, count: {}", notificationIds.size());
        return tcmNotificationRepository.batchMarkAsRead(notificationIds, LocalDateTime.now());
    }
    
    @Override
    public int markAllAsRead(Long userId) {
        log.debug("=============== Marking all notifications as read for user: {}", userId);
        
        // 先查询用户所有未读通知
        Page<TcmNotification> notifications = tcmNotificationRepository.findByUserIdOrderByCreatedAtDesc(userId, Pageable.unpaged());
        List<Long> notificationIds = notifications.getContent().stream()
                .filter(n -> !"system".equals(n.getType()) && !Boolean.TRUE.equals(n.getIsRead()))
                .map(TcmNotification::getId)
                .toList();
        
        if (notificationIds.isEmpty()) {
            return 0;
        }
        
        return tcmNotificationRepository.batchMarkAsRead(notificationIds, LocalDateTime.now());
    }
    
    /**
     * 创建资源相关通知
     * 
     * @param userId 接收用户ID
     * @param type 通知类型
     * @param title 通知标题
     * @param content 通知内容
     * @param relatedType 相关资源类型
     * @param relatedId 相关资源ID
     * @param fromUserId 发送者用户ID
     * @return 创建的通知
     */
    private TcmNotification createResourceNotification(Long userId, String type, String title, String content, 
                                                     String relatedType, Long relatedId, Long fromUserId) {
        
        TcmNotification notification = new TcmNotification();
        notification.setUserId(userId);
        notification.setType(type);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setRelatedId(relatedId);
        notification.setRelatedType(relatedType);
        notification.setFromUserId(fromUserId);
        notification.setIsRead(false); // 默认未读
        
        return tcmNotificationRepository.save(notification);
    }
    
    /**
     * 获取资源类型的中文名称
     * 
     * @param type 资源类型
     * @return 中文名称
     */
    private String getTypeName(String type) {
        switch (type) {
            case "post":
                return "帖子";
            case "article":
                return "文章";
            case "video":
                return "视频";
            default:
                return "资源";
        }
    }
}
