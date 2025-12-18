package com.ride.mapper;

import com.ride.entity.TcmNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 系统通知数据访问层
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@Repository
public interface TcmNotificationRepository extends JpaRepository<TcmNotification, Long> {
    
    /**
     * 根据用户ID查询通知列表（分页）
     * 按创建时间倒序排列
     * 
     * @param userId 用户ID
     * @param pageable 分页参数
     * @return 分页通知列表
     */
    Page<TcmNotification> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    
    /**
     * 根据用户ID查询未读通知数量
     * 系统消息不计算在内
     * 
     * @param userId 用户ID
     * @return 未读通知数量
     */
    @Query("SELECT COUNT(n) FROM TcmNotification n WHERE n.userId = :userId AND n.type != 'system' AND n.isRead = false")
    long countUnreadNotifications(@Param("userId") Long userId);
    
    /**
     * 批量标记通知为已读
     * 系统消息不处理
     * 
     * @param ids 通知ID列表
     * @param readAt 阅读时间
     * @return 更新的记录数
     */
    @Modifying
    @Query("UPDATE TcmNotification n SET n.isRead = true, n.readAt = :readAt WHERE n.id IN :ids AND n.type != 'system'")
    int batchMarkAsRead(@Param("ids") List<Long> ids, @Param("readAt") LocalDateTime readAt);
    
    /**
     * 标记所有通知为已读
     * 系统消息不处理
     * 
     * @param userId 用户ID
     * @param readAt 阅读时间
     * @return 更新的记录数
     */
    @Modifying
    @Query("UPDATE TcmNotification n SET n.isRead = true, n.readAt = :readAt WHERE n.userId = :userId AND n.type != 'system'")
    int markAllAsRead(@Param("userId") Long userId, @Param("readAt") LocalDateTime readAt);
}
