package com.ride.service.impl;

import com.ride.dto.TcmArticleCommentDTO;
import com.ride.entity.TcmArticleComment;
import com.ride.mapper.TcmArticleCommentRepository;
import com.ride.service.TcmArticleCommentService;
import com.ride.service.TcmArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文章评论Service实现类
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@Service
public class TcmArticleCommentServiceImpl implements TcmArticleCommentService {
    
    private static final Logger logger = LoggerFactory.getLogger(TcmArticleCommentServiceImpl.class);
    
    @Autowired
    private TcmArticleCommentRepository tcmArticleCommentRepository;
    
    @Autowired
    private TcmArticleService tcmArticleService;
    
    @Override
    @Transactional
    public TcmArticleComment createComment(TcmArticleComment comment) {
        logger.info("===============Creating comment for article: {}", comment.getArticleId());
        
        // 设置默认值
        if (comment.getStatus() == null) {
            comment.setStatus(1); // 默认状态为启用
        }
        if (comment.getLikeCount() == null) {
            comment.setLikeCount(0L);
        }
        if (comment.getParentId() == null) {
            comment.setParentId(0L); // 默认父评论ID为0
        }
        
        // 创建评论
        TcmArticleComment createdComment = tcmArticleCommentRepository.save(comment);
        
        // 更新文章评论数
        tcmArticleService.incrementCommentCount(comment.getArticleId());
        logger.info("===============Incremented comment count for article: {}", comment.getArticleId());
        
        return createdComment;
    }
    
    @Override
    public TcmArticleComment getCommentById(Long id) {
        logger.info("===============Getting comment by id: {}", id);
        return tcmArticleCommentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + id));
    }
    
    @Override
    public TcmArticleCommentDTO getCommentDTOById(Long id) {
        logger.info("===============Getting comment DTO by id: {}", id);
        TcmArticleComment comment = getCommentById(id);
        return convertToDTO(comment);
    }
    
    @Override
    @Transactional
    public TcmArticleComment updateComment(TcmArticleComment comment) {
        logger.info("===============Updating comment: {}", comment.getId());
        
        TcmArticleComment existingComment = getCommentById(comment.getId());
        
        // 复制属性，不覆盖null值
        BeanUtils.copyProperties(comment, existingComment, "id", "createdAt");
        
        return tcmArticleCommentRepository.save(existingComment);
    }
    
    @Override
    @Transactional
    public void deleteComment(Long id) {
        logger.info("===============Deleting comment: {}", id);
        
        if (tcmArticleCommentRepository.existsById(id)) {
            tcmArticleCommentRepository.deleteById(id);
        } else {
            throw new RuntimeException("Comment not found with id: " + id);
        }
    }
    
    @Override
    public List<TcmArticleCommentDTO> getCommentsByArticleId(Long articleId, Integer status) {
        logger.info("===============Getting comments by article id: {}, status: {}", articleId, status);
        List<TcmArticleComment> comments = tcmArticleCommentRepository.findByArticleIdAndStatusOrderByCreatedAtDesc(articleId, status);
        return convertToDTOList(comments);
    }
    
    @Override
    public Map<String, Object> getCommentsByArticleIdWithPage(Long articleId, Integer status, int page, int pageSize) {
        logger.info("===============Getting comments by article id: {}, status: {}, page: {}, pageSize: {}", 
                articleId, status, page, pageSize);
        
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<TcmArticleComment> commentPage = tcmArticleCommentRepository.findByArticleIdAndStatusOrderByCreatedAtDesc(articleId, status, pageable);
        
        Map<String, Object> result = new HashMap<>();
        result.put("comments", convertToDTOList(commentPage.getContent()));
        result.put("total", commentPage.getTotalElements());
        result.put("pages", commentPage.getTotalPages());
        result.put("current", page);
        result.put("pageSize", pageSize);
        
        return result;
    }
    
    @Override
    public List<TcmArticleCommentDTO> getCommentsByUserId(Long userId, Integer status) {
        logger.info("===============Getting comments by user id: {}, status: {}", userId, status);
        List<TcmArticleComment> comments = tcmArticleCommentRepository.findByUserIdAndStatusOrderByCreatedAtDesc(userId, status);
        return convertToDTOList(comments);
    }
    
    @Override
    public Map<String, Object> getCommentsByUserIdWithPage(Long userId, Integer status, int page, int pageSize) {
        logger.info("===============Getting comments by user id: {}, status: {}, page: {}, pageSize: {}", 
                userId, status, page, pageSize);
        
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<TcmArticleComment> commentPage = tcmArticleCommentRepository.findByUserIdAndStatusOrderByCreatedAtDesc(userId, status, pageable);
        
        Map<String, Object> result = new HashMap<>();
        result.put("comments", convertToDTOList(commentPage.getContent()));
        result.put("total", commentPage.getTotalElements());
        result.put("pages", commentPage.getTotalPages());
        result.put("current", page);
        result.put("pageSize", pageSize);
        
        return result;
    }
    
    @Override
    public List<TcmArticleCommentDTO> getCommentsByParentId(Long parentId, Integer status) {
        logger.info("===============Getting comments by parent id: {}, status: {}", parentId, status);
        List<TcmArticleComment> comments = tcmArticleCommentRepository.findByParentIdAndStatusOrderByCreatedAtDesc(parentId, status);
        return convertToDTOList(comments);
    }
    
    @Override
    @Transactional
    public TcmArticleComment updateCommentStatus(Long id, Integer status) {
        logger.info("===============Updating comment status: {}, new status: {}", id, status);
        
        TcmArticleComment existingComment = getCommentById(id);
        existingComment.setStatus(status);
        
        return tcmArticleCommentRepository.save(existingComment);
    }
    
    @Override
    @Transactional
    public Long incrementCommentLikeCount(Long id) {
        logger.info("===============Incrementing comment like count: {}", id);
        
        TcmArticleComment existingComment = getCommentById(id);
        existingComment.setLikeCount(existingComment.getLikeCount() + 1);
        
        return tcmArticleCommentRepository.save(existingComment).getLikeCount();
    }
    
    @Override
    @Transactional
    public Long decrementCommentLikeCount(Long id) {
        logger.info("===============Decrementing comment like count: {}", id);
        
        TcmArticleComment existingComment = getCommentById(id);
        if (existingComment.getLikeCount() > 0) {
            existingComment.setLikeCount(existingComment.getLikeCount() - 1);
        }
        
        return tcmArticleCommentRepository.save(existingComment).getLikeCount();
    }
    
    @Override
    public Long countCommentsByArticleId(Long articleId, Integer status) {
        logger.info("===============Counting comments by article id: {}, status: {}", articleId, status);
        return tcmArticleCommentRepository.countByArticleIdAndStatus(articleId, status);
    }
    
    @Override
    public Long countCommentsByUserId(Long userId, Integer status) {
        logger.info("===============Counting comments by user id: {}, status: {}", userId, status);
        return tcmArticleCommentRepository.countByUserIdAndStatus(userId, status);
    }
    
    /**
     * 将Comment实体转换为DTO
     */
    private TcmArticleCommentDTO convertToDTO(TcmArticleComment comment) {
        TcmArticleCommentDTO dto = new TcmArticleCommentDTO();
        BeanUtils.copyProperties(comment, dto);
        // 目前没有用户信息表，所以userName暂时使用userId
        dto.setUserName("User_" + comment.getUserId());
        if (comment.getParentId() != null && comment.getParentId() > 0) {
            dto.setParentUserName("User_" + comment.getParentId());
        }
        return dto;
    }
    
    /**
     * 将Comment实体列表转换为DTO列表
     */
    private List<TcmArticleCommentDTO> convertToDTOList(List<TcmArticleComment> comments) {
        List<TcmArticleCommentDTO> dtos = new ArrayList<>();
        for (TcmArticleComment comment : comments) {
            dtos.add(convertToDTO(comment));
        }
        return dtos;
    }
}