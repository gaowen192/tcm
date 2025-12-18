package com.ride.service.impl;

import com.ride.dto.TcmReplyDTO;
import com.ride.entity.TcmReply;
import com.ride.mapper.TcmReplyRepository;
import com.ride.service.TcmPostService;
import com.ride.service.TcmReplyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 医药论坛回复业务逻辑实现类
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@Service
@Transactional
public class TcmReplyServiceImpl implements TcmReplyService {
    
    private static final Logger log = LoggerFactory.getLogger(TcmReplyServiceImpl.class);
    
    @Autowired
    private TcmReplyRepository tcmReplyRepository;
    
    @Autowired
    private TcmPostService tcmPostService;
    
    @Override
    public TcmReplyDTO createReply(TcmReply reply) {
        log.info("开始创建医药论坛回复：帖子ID {}", reply.getPostId());
        
        TcmReply savedReply = tcmReplyRepository.save(reply);
        log.info("医药论坛回复创建成功：{}", savedReply.getId());
        
        return convertToDTO(savedReply);
    }
    
    @Override
    @Transactional(readOnly = true)
    public TcmReplyDTO getReplyById(Long id) {
        log.debug("查询医药论坛回复ID：{}", id);
        
        TcmReply reply = tcmReplyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("回复不存在，ID：" + id));
        
        return convertToDTO(reply);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TcmReplyDTO> getRepliesByPostId(Long postId) {
        log.debug("查询帖子ID的回复列表：{}", postId);
        
        List<TcmReply> replies = tcmReplyRepository.findByPostId(postId);
        return replies.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<TcmReplyDTO> getRepliesByPostIdWithPagination(Long postId, int page, int size) {
        log.debug("=============== 查询帖子ID为{}的回复列表，分页参数：page={}, size={}", postId, page, size);
        
        // 创建分页参数，注意Spring Data JPA的page从0开始，原生SQL需要使用数据库列名
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "created_at"));
        
        // 查询包含用户名的回复列表
        Page<Map<String, Object>> resultPage = tcmReplyRepository.findRepliesWithUsernameByPostId(postId, pageable);
        
        // 将Map转换为TcmReplyDTO
        List<TcmReplyDTO> replyDTOs = resultPage.stream()
                .map(this::mapToReplyDTO)
                .collect(Collectors.toList());
        
        // 创建新的Page对象
        return new PageImpl<>(replyDTOs, pageable, resultPage.getTotalElements());
    }
    
    /**
     * 将Map转换为TcmReplyDTO
     * 
     * @param map 查询结果Map
     * @return TcmReplyDTO对象
     */
    private TcmReplyDTO mapToReplyDTO(Map<String, Object> map) {
        TcmReplyDTO dto = new TcmReplyDTO();
        
        // 设置回复基本信息
        dto.setId(Long.valueOf(map.get("id").toString()));
        dto.setPostId(Long.valueOf(map.get("post_id").toString()));
        dto.setUserId(Long.valueOf(map.get("user_id").toString()));
        dto.setParentId(map.get("parent_id") != null ? Long.valueOf(map.get("parent_id").toString()) : 0L);
        dto.setReplyToUserId(map.get("reply_to_user_id") != null ? Long.valueOf(map.get("reply_to_user_id").toString()) : 0L);
        dto.setContent(map.get("content").toString());
        dto.setLikeCount(((Number) map.get("like_count")).intValue());
        dto.setStatus(((Number) map.get("status")).intValue());
        // 处理日期时间类型转换
        if (map.get("created_at") instanceof java.sql.Timestamp) {
            dto.setCreatedAt(((java.sql.Timestamp) map.get("created_at")).toLocalDateTime());
        } else if (map.get("created_at") instanceof java.util.Date) {
            dto.setCreatedAt(((java.util.Date) map.get("created_at")).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
        } else {
            dto.setCreatedAt(null);
        }
        
        if (map.get("updated_at") instanceof java.sql.Timestamp) {
            dto.setUpdatedAt(((java.sql.Timestamp) map.get("updated_at")).toLocalDateTime());
        } else if (map.get("updated_at") instanceof java.util.Date) {
            dto.setUpdatedAt(((java.util.Date) map.get("updated_at")).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
        } else {
            dto.setUpdatedAt(null);
        }
        
        // 设置用户名
        dto.setUserName(map.get("username") != null ? map.get("username").toString() : "");
        
        return dto;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TcmReplyDTO> getRepliesByUserId(Long userId) {
        log.debug("查询用户ID的回复列表：{}", userId);
        
        List<TcmReply> replies = tcmReplyRepository.findByUserId(userId);
        return replies.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<TcmReplyDTO> getRepliesByUserIdWithPagination(Long userId, int page, int size) {
        log.debug("=============== 查询用户ID为{}的回复列表，分页参数：page={}, size={}", userId, page, size);
        
        // 创建分页参数，注意Spring Data JPA的page从0开始，原生SQL需要使用数据库列名
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "created_at"));
        
        // 查询包含用户名的回复列表
        Page<Map<String, Object>> resultPage = tcmReplyRepository.findRepliesWithUsernameByUserId(userId, pageable);
        
        // 将Map转换为TcmReplyDTO
        List<TcmReplyDTO> replyDTOs = resultPage.stream()
                .map(this::mapToReplyDTO)
                .collect(Collectors.toList());
        
        // 创建新的Page对象
        return new PageImpl<>(replyDTOs, pageable, resultPage.getTotalElements());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TcmReplyDTO> getRepliesByUserIdAndPostId(Long userId, Long postId) {
        log.debug("查询用户ID和帖子ID的回复列表：{} - {}", userId, postId);
        
        List<TcmReply> replies = tcmReplyRepository.findByUserIdAndPostId(userId, postId);
        return replies.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TcmReplyDTO> getRepliesByStatus(Integer status) {
        log.debug("查询状态为{}的回复列表", status);
        
        List<TcmReply> replies = tcmReplyRepository.findByStatus(status);
        return replies.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Method removed - functionality moved to getRepliesByPostIdOrderByTime
    
    @Override
    public TcmReplyDTO updateReply(Long id, TcmReply reply) {
        log.info("开始更新医药论坛回复ID：{}", id);
        
        // 验证回复是否存在
        TcmReply existingReply = tcmReplyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("回复不存在，ID：" + id));
        
        // 设置ID并更新
        reply.setId(id);
        reply.setUserId(existingReply.getUserId());
        reply.setPostId(existingReply.getPostId());
        reply.setCreatedAt(existingReply.getCreatedAt());
        TcmReply updatedReply = tcmReplyRepository.save(reply);
        
        log.info("医药论坛回复更新成功：{}", updatedReply.getId());
        return convertToDTO(updatedReply);
    }
    
    @Override
    public void deleteReply(Long id) {
        log.info("开始删除医药论坛回复ID：{}", id);
        
        // 验证回复是否存在
        TcmReply reply = tcmReplyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("回复不存在，ID：" + id));
        
        // 获取帖子ID，用于减少帖子的回复数量
        Long postId = reply.getPostId();
        
        // 删除回复
        tcmReplyRepository.deleteById(id);
        log.info("医药论坛回复删除成功：{}", id);
        
        // 减少帖子的回复数量
        tcmPostService.decreaseReplyCount(postId);
        log.info("帖子{}的回复数量减少1", postId);
    }
    
    @Override
    public void batchDeleteReplies(List<Long> ids) {
        log.info("开始批量删除医药论坛回复，数量：{}", ids.size());
        
        tcmReplyRepository.deleteAllById(ids);
        log.info("批量删除医药论坛回复完成，数量：{}", ids.size());
    }
    
    @Override
    public void deleteRepliesByPostId(Long postId) {
        log.info("开始删除帖子ID的所有回复：{}", postId);
        
        tcmReplyRepository.deleteByPostId(postId);
        log.info("删除帖子ID的所有回复完成：{}", postId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getReplyCountByPostId(Long postId) {
        return tcmReplyRepository.countByPostId(postId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getReplyCountByUserId(Long userId) {
        return tcmReplyRepository.countByUserId(userId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getReplyCountByPost(Long postId) {
        return getReplyCountByPostId(postId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getReplyCountByUser(Long userId) {
        return getReplyCountByUserId(userId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getTotalReplyCount() {
        return tcmReplyRepository.count();
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean hasUserRepliedToPost(Long userId, Long postId) {
        log.debug("检查用户是否对帖子进行了回复：用户ID {} 帖子ID {}", userId, postId);
        
        List<TcmReply> replies = tcmReplyRepository.findByUserIdAndPostId(userId, postId);
        return !replies.isEmpty();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TcmReplyDTO> getAllReplies() {
        log.debug("获取所有回复列表");
        
        List<TcmReply> replies = tcmReplyRepository.findAll();
        return replies.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TcmReplyDTO> getRepliesByPostIdOrderByTime(Long postId, Integer status) {
        log.debug("查询帖子ID的回复列表（按时间排序）：帖子ID {} 状态 {}", postId, status);
        
        List<TcmReply> replies = tcmReplyRepository.findByPostIdAndStatusOrderByCreatedAtDesc(postId, status);
        return replies.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getReplyCountByPostIdAndStatus(Long postId, Integer status) {
        return tcmReplyRepository.countByPostIdAndStatus(postId, status);
    }
    
    /**
     * 将TcmReply实体转换为TcmReplyDTO
     * 
     * @param reply TcmReply实体
     * @return TcmReplyDTO
     */
    private TcmReplyDTO convertToDTO(TcmReply reply) {
        TcmReplyDTO dto = new TcmReplyDTO();
        dto.setId(reply.getId());
        dto.setPostId(reply.getPostId());
        dto.setUserId(reply.getUserId());
        dto.setContent(reply.getContent());
        dto.setStatus(reply.getStatus());
        dto.setCreatedAt(reply.getCreatedAt());
        dto.setUpdatedAt(reply.getUpdatedAt());
        return dto;
    }
}
