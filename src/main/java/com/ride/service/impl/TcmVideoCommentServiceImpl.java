package com.ride.service.impl;

import com.ride.entity.TcmVideoComment;
import com.ride.mapper.TcmVideoCommentRepository;
import com.ride.service.TcmVideoCommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 医药论坛视频评论服务实现类
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@Service
public class TcmVideoCommentServiceImpl implements TcmVideoCommentService {
    
    private static final Logger log = LoggerFactory.getLogger(TcmVideoCommentServiceImpl.class);
    
    @Autowired
    private TcmVideoCommentRepository tcmVideoCommentRepository;
    
    private static final Integer STATUS_NORMAL = 1; // 正常状态
    private static final Integer STATUS_DISABLED = 0; // 禁用状态
    
    @Override
    @Transactional
    public TcmVideoComment createComment(TcmVideoComment comment) {
        log.debug("=============== Creating comment: videoId={}, userId={}", comment.getVideoId(), comment.getUserId());
        // 确保评论状态正确设置
        if (comment.getStatus() == null) {
            comment.setStatus(STATUS_NORMAL);
        }
        // 确保点赞数初始化为0
        if (comment.getLikeCount() == null) {
            comment.setLikeCount(0L);
        }
        return tcmVideoCommentRepository.save(comment);
    }
    
    @Override
    public TcmVideoComment getCommentById(Long id) {
        log.debug("=============== Getting comment by id: {}", id);
        Optional<TcmVideoComment> comment = tcmVideoCommentRepository.findById(id);
        return comment.orElse(null);
    }
    
    @Override
    @Transactional
    public TcmVideoComment updateComment(Long id, String content) {
        log.debug("=============== Updating comment content: id={}", id);
        Optional<TcmVideoComment> commentOpt = tcmVideoCommentRepository.findById(id);
        if (commentOpt.isPresent()) {
            TcmVideoComment comment = commentOpt.get();
            comment.setContent(content);
            return tcmVideoCommentRepository.save(comment);
        }
        return null;
    }
    
    @Override
    @Transactional
    public void deleteComment(Long id) {
        log.debug("=============== Deleting comment: id={}", id);
        Optional<TcmVideoComment> commentOpt = tcmVideoCommentRepository.findById(id);
        if (commentOpt.isPresent()) {
            // 使用软删除，将状态设置为禁用
            TcmVideoComment comment = commentOpt.get();
            comment.setStatus(STATUS_DISABLED);
            tcmVideoCommentRepository.save(comment);
        }
    }
    
    @Override
    public List<TcmVideoComment> getCommentsByVideoId(Long videoId) {
        log.debug("=============== Getting comments by videoId: {}", videoId);
        return tcmVideoCommentRepository.findByVideoIdAndStatusOrderByCreatedAtDesc(videoId, STATUS_NORMAL);
    }
    
    @Override
    public Page<TcmVideoComment> getCommentsByVideoId(Long videoId, Pageable pageable) {
        log.debug("=============== Getting paginated comments by videoId: {}", videoId);
        return tcmVideoCommentRepository.findByVideoIdAndStatusOrderByCreatedAtDesc(videoId, STATUS_NORMAL, pageable);
    }
    
    @Override
    public List<TcmVideoComment> getCommentsByUserId(Long userId) {
        log.debug("=============== Getting comments by userId: {}", userId);
        return tcmVideoCommentRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
    
    @Override
    public Page<TcmVideoComment> getCommentsByUserId(Long userId, Pageable pageable) {
        log.debug("=============== Getting paginated comments by userId: {}", userId);
        return tcmVideoCommentRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }
    
    @Override
    public List<TcmVideoComment> getRepliesByCommentId(Long commentId) {
        log.debug("=============== Getting replies by commentId: {}", commentId);
        return tcmVideoCommentRepository.findByReplyToCommentIdAndStatusOrderByCreatedAtAsc(commentId, STATUS_NORMAL);
    }
    
    @Override
    @Transactional
    public void likeComment(Long commentId) {
        log.debug("=============== Liking comment: id={}", commentId);
        tcmVideoCommentRepository.incrementLikeCount(commentId);
    }
    
    @Override
    @Transactional
    public void unlikeComment(Long commentId) {
        log.debug("=============== Unliking comment: id={}", commentId);
        tcmVideoCommentRepository.decrementLikeCount(commentId);
    }
    
    @Override
    public long countCommentsByVideoId(Long videoId) {
        log.debug("=============== Counting comments by videoId: {}", videoId);
        return tcmVideoCommentRepository.countByVideoIdAndStatus(videoId, STATUS_NORMAL);
    }
    
    @Override
    public long countCommentsByUserId(Long userId) {
        log.debug("=============== Counting comments by userId: {}", userId);
        return tcmVideoCommentRepository.countByUserId(userId);
    }
    
    @Override
    public boolean isCommentOwnedByUser(Long commentId, Long userId) {
        log.debug("=============== Checking if comment {} is owned by user {}", commentId, userId);
        Optional<TcmVideoComment> commentOpt = tcmVideoCommentRepository.findById(commentId);
        return commentOpt.isPresent() && commentOpt.get().getUserId().equals(userId);
    }
}
