package com.ride.service.impl;

import com.ride.dto.TcmPostLikeDTO;
import com.ride.entity.TcmPostLike;
import com.ride.mapper.TcmPostLikeRepository;
import com.ride.service.TcmPostLikeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 医药论坛帖子点赞业务逻辑实现类
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@Service
@Transactional
public class TcmPostLikeServiceImpl implements TcmPostLikeService {
    
    private static final Logger log = LoggerFactory.getLogger(TcmPostLikeServiceImpl.class);
    
    @Autowired
    private TcmPostLikeRepository tcmPostLikeRepository;
    
    @Override
    public TcmPostLikeDTO createPostLike(TcmPostLike postLike) {
        log.info("开始创建帖子点赞：用户ID {} 帖子ID {}", postLike.getUserId(), postLike.getPostId());
        
        // 验证是否已经点赞
        if (existsByUserIdAndPostId(postLike.getUserId(), postLike.getPostId())) {
            throw new IllegalArgumentException("用户已点赞该帖子：用户ID " + postLike.getUserId() + " 帖子ID " + postLike.getPostId());
        }
        
        TcmPostLike savedPostLike = tcmPostLikeRepository.save(postLike);
        log.info("帖子点赞创建成功：{}", savedPostLike.getId());
        
        return convertToDTO(savedPostLike);
    }
    
    @Override
    @Transactional(readOnly = true)
    public TcmPostLikeDTO getPostLikeById(Long id) {
        log.debug("查询帖子点赞ID：{}", id);
        
        TcmPostLike postLike = tcmPostLikeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("帖子点赞不存在，ID：" + id));
        
        return convertToDTO(postLike);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TcmPostLikeDTO> getPostLikesByUserId(Long userId) {
        log.debug("查询用户ID的帖子点赞列表：{}", userId);
        
        List<TcmPostLike> postLikes = tcmPostLikeRepository.findByUserId(userId);
        return postLikes.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TcmPostLikeDTO> getPostLikesByPostId(Long postId) {
        log.debug("查询帖子ID的点赞列表：{}", postId);
        
        List<TcmPostLike> postLikes = tcmPostLikeRepository.findByPostId(postId);
        return postLikes.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TcmPostLikeDTO> getPostLikesByUserIdAndPostId(Long userId, Long postId) {
        log.debug("查询用户ID和帖子ID的点赞记录：{} - {}", userId, postId);
        
        TcmPostLike postLike = tcmPostLikeRepository.findByUserIdAndPostId(userId, postId).orElse(null);
        return postLike != null ? List.of(convertToDTO(postLike)) : List.of();
    }
    
    @Override
    public void deletePostLike(Long id) {
        log.info("开始删除帖子点赞ID：{}", id);
        
        // 验证帖子点赞是否存在
        if (!tcmPostLikeRepository.existsById(id)) {
            throw new IllegalArgumentException("帖子点赞不存在，ID：" + id);
        }
        
        tcmPostLikeRepository.deleteById(id);
        log.info("帖子点赞删除成功：{}", id);
    }
    
    @Override
    public void deletePostLikeByUserIdAndPostId(Long userId, Long postId) {
        log.info("开始删除用户点赞记录：用户ID {} 帖子ID {}", userId, postId);
        
        Optional<TcmPostLike> likeOptional = tcmPostLikeRepository.findByUserIdAndPostId(userId, postId);
        likeOptional.ifPresent(like -> tcmPostLikeRepository.delete(like));
        
        log.info("删除用户点赞记录完成：用户ID {} 帖子ID {}", userId, postId);
    }
    
    @Override
    public void unlikePost(Long userId, Long postId) {
        log.info("用户{}取消点赞帖子{}", userId, postId);
        deletePostLikeByUserIdAndPostId(userId, postId);
        log.info("用户{}取消点赞帖子{}成功", userId, postId);
    }
    
    @Override
    public void batchDeletePostLikes(List<Long> ids) {
        log.info("开始批量删除帖子点赞，数量：{}", ids.size());
        
        tcmPostLikeRepository.deleteAllById(ids);
        log.info("批量删除帖子点赞完成，数量：{}", ids.size());
    }
    
    @Override
    public void removePostLike(Long userId, Long postId) {
        log.info("用户{}对帖子{}执行取消点赞操作", userId, postId);
        
        deletePostLikeByUserIdAndPostId(userId, postId);
        log.info("用户{}对帖子{}取消点赞成功", userId, postId);
    }
    
    @Override
    public void deletePostLikesByPostId(Long postId) {
        log.info("开始删除帖子{}的所有点赞记录", postId);
        
        List<TcmPostLike> postLikes = tcmPostLikeRepository.findByPostId(postId);
        if (!postLikes.isEmpty()) {
            tcmPostLikeRepository.deleteAll(postLikes);
            log.info("删除帖子{}的所有点赞记录完成，数量：{}", postId, postLikes.size());
        } else {
            log.info("帖子{}没有点赞记录", postId);
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByUserIdAndPostId(Long userId, Long postId) {
        return tcmPostLikeRepository.findByUserIdAndPostId(userId, postId).isPresent();
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean hasLikedPost(Long userId, Long postId) {
        return tcmPostLikeRepository.findByUserIdAndPostId(userId, postId).isPresent();
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getPostLikeCount(Long postId) {
        return tcmPostLikeRepository.countByPostId(postId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getPostLikeCountByUserId(Long userId) {
        return tcmPostLikeRepository.countByUserId(userId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getUserLikeCount(Long userId) {
        return tcmPostLikeRepository.countByUserId(userId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean hasUserLikedPost(Long userId, Long postId) {
        return tcmPostLikeRepository.findByUserIdAndPostId(userId, postId).isPresent();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TcmPostLikeDTO> getAllPostLikes() {
        log.debug("获取所有帖子点赞记录");
        
        List<TcmPostLike> postLikes = tcmPostLikeRepository.findAll();
        return postLikes.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public TcmPostLikeDTO getPostLikeByUserAndPost(Long userId, Long postId) {
        log.debug("根据用户ID和帖子ID查询点赞记录：用户ID {} 帖子ID {}", userId, postId);
        
        Optional<TcmPostLike> postLike = tcmPostLikeRepository.findByUserIdAndPostId(userId, postId);
        return postLike.map(this::convertToDTO).orElse(null);
    }
    
    @Override
    public void deletePostLikeByUserAndPost(Long userId, Long postId) {
        log.info("根据用户ID和帖子ID删除点赞记录：用户ID {} 帖子ID {}", userId, postId);
        deletePostLikeByUserIdAndPostId(userId, postId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getTotalLikeCount() {
        log.debug("获取系统点赞总数");
        return tcmPostLikeRepository.count();
    }
    

    
    /**
     * 将TcmPostLike实体转换为TcmPostLikeDTO
     * 
     * @param postLike TcmPostLike实体
     * @return TcmPostLikeDTO
     */
    private TcmPostLikeDTO convertToDTO(TcmPostLike postLike) {
        TcmPostLikeDTO dto = new TcmPostLikeDTO();
        dto.setId(postLike.getId());
        dto.setUserId(postLike.getUserId());
        dto.setPostId(postLike.getPostId());
        dto.setCreatedAt(postLike.getCreatedAt());
        return dto;
    }
}
