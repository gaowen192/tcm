package com.ride.service.impl;

import com.ride.entity.TcmPostInteraction;
import com.ride.repository.TcmPostInteractionRepository;
import com.ride.service.TcmPostInteractionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 帖子互动记录Service实现类
 */
@Service
public class TcmPostInteractionServiceImpl implements TcmPostInteractionService {

    @Autowired
    private TcmPostInteractionRepository tcmPostInteractionRepository;

    private static final Logger log = LoggerFactory.getLogger(TcmPostInteractionServiceImpl.class);

    @Override
    public void recordPostInteraction(Long userId, Long postId, String interactionType) {
        log.debug("=============== Recording post interaction: userId={}, postId={}, type={}", userId, postId, interactionType);
        
        // 检查是否已存在记录
        Optional<TcmPostInteraction> existingRecordOpt = tcmPostInteractionRepository
                .findByUserIdAndPostIdAndInteractionTypeAndIsDeleted(userId, postId, interactionType, 0);
        
        if (existingRecordOpt.isPresent()) {
            // 已存在记录，更新互动时间
            log.debug("=============== Found existing interaction record, updating interaction date");
            TcmPostInteraction existingRecord = existingRecordOpt.get();
            existingRecord.setInteractionDate(LocalDateTime.now());
            tcmPostInteractionRepository.save(existingRecord);
        } else {
            // 不存在记录，创建新记录
            log.debug("=============== Creating new interaction record");
            TcmPostInteraction newRecord = new TcmPostInteraction();
            newRecord.setUserId(userId);
            newRecord.setPostId(postId);
            newRecord.setInteractionType(interactionType);
            newRecord.setInteractionDate(LocalDateTime.now());
            newRecord.setIsDeleted(0);
            newRecord.setCreatedTime(LocalDateTime.now());
            newRecord.setUpdatedTime(LocalDateTime.now());
            tcmPostInteractionRepository.save(newRecord);
        }
    }

    @Override
    public List<Map<String, Object>> getUserPostHistory(Long userId) {
        log.debug("=============== Getting user post interaction history for user ID: {}", userId);
        return tcmPostInteractionRepository.findUserPostHistory(userId);
    }

    @Override
    public Map<String, Object> getUserPostHistoryWithPagination(Long userId, Integer page, Integer pageSize) {
        log.debug("=============== Getting user post interaction history with pagination: user ID={}, page={}, pageSize={}", userId, page, pageSize);
        
        // 这里可以根据实际需求实现分页逻辑
        // 暂时返回所有数据，后续可以根据需要添加分页
        List<Map<String, Object>> historyList = tcmPostInteractionRepository.findUserPostHistory(userId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("content", historyList);
        result.put("totalElements", historyList.size());
        result.put("totalPages", 1);
        result.put("currentPage", page);
        result.put("pageSize", pageSize);
        result.put("hasNext", false);
        result.put("hasPrevious", false);
        
        return result;
    }
}
