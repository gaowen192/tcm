package com.ride.service.impl;

import com.ride.dto.TcmPostCollectionDTO;
import com.ride.entity.TcmPostCollection;
import com.ride.mapper.TcmPostCollectionRepository;
import com.ride.service.TcmPostCollectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 医药论坛帖子收藏业务逻辑实现类
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@Service
@Transactional
public class TcmPostCollectionServiceImpl implements TcmPostCollectionService {
    
    private static final Logger log = LoggerFactory.getLogger(TcmPostCollectionServiceImpl.class);
    
    @Autowired
    private TcmPostCollectionRepository tcmPostCollectionRepository;
    
    @Override
    public TcmPostCollectionDTO createPostCollection(TcmPostCollection postCollection) {
        log.info("开始创建帖子收藏：用户ID {} 帖子ID {}", postCollection.getUserId(), postCollection.getPostId());
        
        // 验证是否已经收藏
        if (existsByUserIdAndPostId(postCollection.getUserId(), postCollection.getPostId())) {
            throw new IllegalArgumentException("用户已经收藏该帖子：用户ID " + postCollection.getUserId() + " 帖子ID " + postCollection.getPostId());
        }
        
        TcmPostCollection savedPostCollection = tcmPostCollectionRepository.save(postCollection);
        log.info("帖子收藏创建成功：{}", savedPostCollection.getId());
        
        return convertToDTO(savedPostCollection);
    }
    
    @Override
    @Transactional(readOnly = true)
    public TcmPostCollectionDTO getPostCollectionById(Long id) {
        log.debug("查询帖子收藏ID：{}", id);
        
        TcmPostCollection postCollection = tcmPostCollectionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("帖子收藏不存在，ID：" + id));
        
        return convertToDTO(postCollection);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TcmPostCollectionDTO> getPostCollectionsByUserId(Long userId) {
        log.debug("查询用户ID的帖子收藏列表：{}", userId);
        
        List<TcmPostCollection> postCollections = tcmPostCollectionRepository.findByUserId(userId);
        return postCollections.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TcmPostCollectionDTO> getPostCollectionsByPostId(Long postId) {
        log.debug("查询帖子ID的收藏列表：{}", postId);
        
        List<TcmPostCollection> postCollections = tcmPostCollectionRepository.findByPostId(postId);
        return postCollections.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TcmPostCollectionDTO> getPostCollectionsByUserIdAndPostId(Long userId, Long postId) {
        log.debug("查询用户ID和帖子ID的收藏记录：{} - {}", userId, postId);
        
        List<TcmPostCollection> postCollections = tcmPostCollectionRepository.findByUserIdAndPostId(userId, postId);
        return postCollections.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public void deletePostCollection(Long id) {
        log.info("开始删除帖子收藏ID：{}", id);
        
        // 验证帖子收藏是否存在
        if (!tcmPostCollectionRepository.existsById(id)) {
            throw new IllegalArgumentException("帖子收藏不存在，ID：" + id);
        }
        
        tcmPostCollectionRepository.deleteById(id);
        log.info("帖子收藏删除成功：{}", id);
    }
    
    @Override
    public void deletePostCollectionByUserIdAndPostId(Long userId, Long postId) {
        log.info("开始删除用户ID和帖子ID的收藏：{} - {}", userId, postId);
        
        List<TcmPostCollection> postCollections = tcmPostCollectionRepository.findByUserIdAndPostId(userId, postId);
        if (!postCollections.isEmpty()) {
            tcmPostCollectionRepository.deleteAll(postCollections);
            log.info("删除用户ID和帖子ID的收藏完成：{} - {}", userId, postId);
        } else {
            log.info("用户ID和帖子ID的收藏不存在：{} - {}", userId, postId);
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TcmPostCollectionDTO> getAllPostCollections() {
        log.debug("查询所有帖子收藏列表");
        
        List<TcmPostCollection> postCollections = tcmPostCollectionRepository.findAll();
        return postCollections.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public void removePostCollection(Long userId, Long postId) {
        log.info("用户{}对帖子{}执行移除收藏操作", userId, postId);
        
        deletePostCollectionByUserIdAndPostId(userId, postId);
        log.info("用户{}对帖子{}移除收藏成功", userId, postId);
    }
    
    @Override
    public void batchRemovePostCollections(Long userId, List<Long> postIds) {
        log.info("用户{}批量移除收藏，帖子ID列表：{}", userId, postIds);
        
        for (Long postId : postIds) {
            deletePostCollectionByUserIdAndPostId(userId, postId);
        }
        log.info("用户{}批量移除收藏完成", userId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean hasUserCollectedPost(Long userId, Long postId) {
        return tcmPostCollectionRepository.existsByUserIdAndPostId(userId, postId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getUserCollectionCount(Long userId) {
        return tcmPostCollectionRepository.countByUserId(userId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getPostCollectionCount(Long postId) {
        return tcmPostCollectionRepository.countByPostId(postId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByUserIdAndPostId(Long userId, Long postId) {
        return tcmPostCollectionRepository.existsByUserIdAndPostId(userId, postId);
    }
    
    @Override
    public void deletePostCollectionsByPostId(Long postId) {
        log.info("开始删除帖子ID的所有收藏记录：{}", postId);
        
        List<TcmPostCollection> postCollections = tcmPostCollectionRepository.findByPostId(postId);
        if (!postCollections.isEmpty()) {
            tcmPostCollectionRepository.deleteAll(postCollections);
            log.info("删除帖子ID的所有收藏记录完成：{}，数量：{}", postId, postCollections.size());
        } else {
            log.info("帖子ID的收藏记录不存在：{}", postId);
        }
    }
    
    @Override
    public void deletePostCollectionByUserAndPost(Long userId, Long postId) {
        log.info("开始删除用户和帖子的收藏记录：{} - {}", userId, postId);
        
        deletePostCollectionByUserIdAndPostId(userId, postId);
        log.info("删除用户和帖子的收藏记录完成：{} - {}", userId, postId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getTotalCollectionCount() {
        log.debug("获取收藏总数统计");
        return tcmPostCollectionRepository.count();
    }
    
    @Override
    public void batchDeletePostCollections(List<Long> ids) {
        log.info("开始批量删除帖子收藏，数量：{}", ids.size());
        
        tcmPostCollectionRepository.deleteAllById(ids);
        log.info("批量删除帖子收藏完成，数量：{}", ids.size());
    }
    
    @Override
    @Transactional(readOnly = true)
    public TcmPostCollectionDTO getPostCollectionByUserAndPost(Long userId, Long postId) {
        log.debug("查询用户和帖子的收藏记录：用户ID {} 帖子ID {}", userId, postId);
        
        List<TcmPostCollection> postCollections = tcmPostCollectionRepository.findByUserIdAndPostId(userId, postId);
        return postCollections.isEmpty() ? null : convertToDTO(postCollections.get(0));
    }
    
    /**
     * 将TcmPostCollection实体转换为TcmPostCollectionDTO
     * 
     * @param postCollection TcmPostCollection实体
     * @return TcmPostCollectionDTO
     */
    private TcmPostCollectionDTO convertToDTO(TcmPostCollection postCollection) {
        TcmPostCollectionDTO dto = new TcmPostCollectionDTO();
        dto.setId(postCollection.getId());
        dto.setUserId(postCollection.getUserId());
        dto.setPostId(postCollection.getPostId());
        dto.setCreatedAt(postCollection.getCreatedAt());
        return dto;
    }
}
