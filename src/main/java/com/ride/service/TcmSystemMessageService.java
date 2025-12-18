package com.ride.service;

import com.ride.entity.TcmSystemMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 中医药系统消息服务接口
 * 
 * @author 千行团队
 * @version 1.0.0
 */
public interface TcmSystemMessageService {

    /**
     * 创建系统消息
     * 
     * @param message 消息实体
     * @return 创建的消息
     */
    TcmSystemMessage createMessage(TcmSystemMessage message);

    /**
     * 根据ID获取消息
     * 
     * @param id 消息ID
     * @return 消息实体
     */
    TcmSystemMessage getMessageById(Long id);

    /**
     * 获取系统消息列表
     * @param pageable 分页参数
     * @return 分页后的消息列表
     */
    Page<TcmSystemMessage> getMessages(Pageable pageable);
    
    /**
     * 根据用户ID获取系统消息列表（查询userId为null或等于指定userId的消息）
     * @param userId 用户ID
     * @param pageable 分页参数
     * @return 分页后的消息列表
     */
    Page<TcmSystemMessage> getMessagesByUserId(Long userId, Pageable pageable);

    /**
     * 根据消息类型分页查询
     * 
     * @param messageType 消息类型
     * @param pageable 分页参数
     * @return 分页消息列表
     */
    Page<TcmSystemMessage> getMessagesByType(Integer messageType, Pageable pageable);

    /**
     * 更新消息
     * 
     * @param message 消息实体
     * @return 更新后的消息
     */
    TcmSystemMessage updateMessage(TcmSystemMessage message);

    /**
     * 删除消息
     * 
     * @param id 消息ID
     */
    void deleteMessage(Long id);

    /**
     * 批量删除消息
     * 
     * @param ids 消息ID列表
     */
    void batchDeleteMessages(List<Long> ids);

    /**
     * 标记消息为已读
     * 
     * @param id 消息ID
     */
    void markAsRead(Long id);

    /**
     * 批量标记消息为已读
     * 
     * @param ids 消息ID列表
     */
    void batchMarkAsRead(List<Long> ids);

    /**
     * 获取未读消息数量
     * 
     * @return 未读消息数量
     */
    Long getUnreadMessageCount();
    
    /**
     * 获取指定用户的未读消息数量
     * 
     * @param userId 用户ID
     * @return 未读消息数量
     */
    Long getUnreadMessageCount(Long userId);


}