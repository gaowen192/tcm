package com.ride.service.impl;

import com.ride.entity.TcmSystemMessage;
import com.ride.mapper.TcmSystemMessageRepository;
import com.ride.service.TcmSystemMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 中医药系统消息服务实现类
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@Service
@Transactional
public class TcmSystemMessageServiceImpl implements TcmSystemMessageService {

    @Autowired
    private TcmSystemMessageRepository tcmSystemMessageRepository;

    @Override
    public TcmSystemMessage createMessage(TcmSystemMessage message) {
        message.setStatus(0); // 设置为正常状态
        message.setIsRead(false); // 设置为未读
        return tcmSystemMessageRepository.save(message);
    }

    @Override
    @Transactional(readOnly = true)
    public TcmSystemMessage getMessageById(Long id) {
        Optional<TcmSystemMessage> optional = tcmSystemMessageRepository.findById(id);
        return optional.orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TcmSystemMessage> getMessages(Pageable pageable) {
        return tcmSystemMessageRepository.findByStatus(0, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<TcmSystemMessage> getMessagesByUserId(Long userId, Pageable pageable) {
        // 调用Mapper层新方法，查询userId为null或等于指定userId的消息
        Page<TcmSystemMessage> page=  tcmSystemMessageRepository.findByUserIdIsNullOrUserIdAndStatus(pageable, userId, 0);
//        page.getContent().forEach(message ->
//                {
//                    List<Long> ids=new ArrayList<>();
//                    if (message.getUserId() != null) {
//                        ids.add(message.getId());
//                    }
//                    tcmSystemMessageRepository.batchUpdateReadStatus(ids,true);
//                }
//
//        );
        return page;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TcmSystemMessage> getMessagesByType(Integer messageType, Pageable pageable) {
        return tcmSystemMessageRepository.findByMessageTypeAndStatus(messageType, 0, pageable);
    }

    @Override
    public TcmSystemMessage updateMessage(TcmSystemMessage message) {
        Optional<TcmSystemMessage> optional = tcmSystemMessageRepository.findById(message.getId());
        if (optional.isPresent()) {
            TcmSystemMessage existing = optional.get();
            existing.setTitle(message.getTitle());
            existing.setContent(message.getContent());
            existing.setMessageType(message.getMessageType());
            existing.setTargetType(message.getTargetType());
            existing.setTargetValue(message.getTargetValue());
            return tcmSystemMessageRepository.save(existing);
        }
        return null;
    }

    @Override
    public void deleteMessage(Long id) {
        tcmSystemMessageRepository.softDeleteById(id);
    }

    @Override
    public void batchDeleteMessages(List<Long> ids) {
        tcmSystemMessageRepository.batchSoftDelete(ids);
    }

    @Override
    public void markAsRead(Long id) {
        Optional<TcmSystemMessage> optional = tcmSystemMessageRepository.findById(id);
        if (optional.isPresent()) {
            TcmSystemMessage message = optional.get();
            message.setIsRead(true);
            tcmSystemMessageRepository.save(message);
        }
    }

    @Override
    public void batchMarkAsRead(List<Long> ids) {
        tcmSystemMessageRepository.batchUpdateReadStatus(ids, true);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getUnreadMessageCount() {
        return tcmSystemMessageRepository.countByIsReadAndStatus(false, 0);
    }
    
    @Override
    public Long getUnreadMessageCount(Long userId) {
        return tcmSystemMessageRepository.countByUserIdAndIsReadAndStatus(userId, false, 0);
    }


}