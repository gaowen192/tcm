package com.ride.mapper;

import com.ride.entity.TcmSystemMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 中医药系统消息数据访问层
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@Repository
public interface TcmSystemMessageRepository extends JpaRepository<TcmSystemMessage, Long> {

    /**
     * 分页查询系统消息
     * 
     * @param status 状态
     * @param pageable 分页参数
     * @return 分页消息列表
     */
    Page<TcmSystemMessage> findByStatus(Integer status, Pageable pageable);

    /**
     * 根据消息类型分页查询
     * 
     * @param messageType 消息类型
     * @param status 状态
     * @param pageable 分页参数
     * @return 分页消息列表
     */
    Page<TcmSystemMessage> findByMessageTypeAndStatus(Integer messageType, Integer status, Pageable pageable);

    /**
     * 查询未读消息数量
     * 
     * @param isRead 是否已读
     * @param status 状态
     * @return 未读消息数量
     */
    Long countByIsReadAndStatus(Boolean isRead, Integer status);
    
    /**
     * 根据用户ID查询未读消息数量
     * 
     * @param userId 用户ID
     * @param isRead 是否已读
     * @param status 状态
     * @return 未读消息数量
     */
    Long countByUserIdAndIsReadAndStatus(Long userId, Boolean isRead, Integer status);

    /**
     * 批量标记消息为已读
     * 
     * @param ids 消息ID列表
     * @param isRead 是否已读
     */
    @Modifying
    @Query("update TcmSystemMessage msg set msg.isRead = :isRead where msg.id in :ids and msg.status = 0")
    void batchUpdateReadStatus(@Param("ids") List<Long> ids, @Param("isRead") Boolean isRead);

    /**
     * 软删除消息
     * 
     * @param id 消息ID
     */
    @Modifying
    @Query("update TcmSystemMessage msg set msg.status = 1 where msg.id = :id")
    void softDeleteById(@Param("id") Long id);

    /**
     * 批量软删除消息
     * 
     * @param ids 消息ID列表
     */
    @Modifying
    @Query("update TcmSystemMessage msg set msg.status = 1 where msg.id in :ids")
    void batchSoftDelete(@Param("ids") List<Long> ids);

    /**
     * 查询userId为null或等于指定userId且状态为指定值的消息列表
     * @param pageable 分页参数
     * @param userId 用户ID
     * @param status 消息状态
     * @return 分页后的消息列表
     */
    Page<TcmSystemMessage> findByUserIdIsNullOrUserIdAndStatus(Pageable pageable, Long userId, Integer status);

 }