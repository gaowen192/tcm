package com.ride.service.impl;

import com.ride.dto.TcmPostDTO;
import com.ride.entity.TcmPost;
import com.ride.mapper.TcmPostRepository;
import com.ride.service.TcmPostService;
import com.ride.service.TcmPostHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 医药论坛帖子业务逻辑实现类
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@Service
@Transactional
public class TcmPostServiceImpl implements TcmPostService {
    
    private static final Logger log = LoggerFactory.getLogger(TcmPostServiceImpl.class);
    
    @Autowired
    private TcmPostRepository tcmPostRepository;
    
    @Override
    public TcmPostDTO createPost(TcmPost post) {
        log.info("开始创建医药论坛帖子：{}", post.getTitle());
        
        TcmPost savedPost = tcmPostRepository.save(post);
        log.info("医药论坛帖子创建成功：{}", savedPost.getTitle());
        
        return convertToDTO(savedPost);
    }
    
    @Override
    @Transactional(readOnly = true)
    public TcmPostDTO getPostById(Long id) {
        log.debug("查询医药论坛帖子ID：{}", id);
        
        TcmPost post = tcmPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("帖子不存在，ID：" + id));
        
        return convertToDTO(post);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TcmPostDTO> getPostsByUserId(Long userId) {
        log.debug("查询用户ID的帖子列表：{}", userId);
        
        List<TcmPost> posts = tcmPostRepository.findByUserId(userId);
        return posts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<TcmPostDTO> getPostsByUserId(Long userId, Pageable pageable) {
        log.debug("=============== 查询用户ID的帖子列表（分页）：{}，分页参数：{}", userId, pageable);
        
        Page<TcmPost> postsPage = tcmPostRepository.findByUserId(userId, pageable);
        return postsPage.map(this::convertToDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TcmPostDTO> getPostsByCategoryId(Long categoryId) {
        log.debug("查询板块ID的帖子列表：{}", categoryId);
        
        List<TcmPost> posts = tcmPostRepository.findByCategoryId(categoryId);
        return posts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TcmPostDTO> getPostsByUserIdAndCategoryId(Long userId, Long categoryId) {
        log.debug("查询用户ID和板块ID的帖子列表：{} - {}", userId, categoryId);
        
        List<TcmPost> posts = tcmPostRepository.findByUserIdAndCategoryId(userId, categoryId);
        return posts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TcmPostDTO> getPostsByStatus(Integer status) {
        log.debug("查询状态为{}的帖子列表", status);
        
        List<TcmPost> posts = tcmPostRepository.findByStatus(status);
        return posts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TcmPostDTO> getPostsByTitleContaining(String title) {
        log.debug("查询标题包含关键词的帖子：{}", title);
        
        List<TcmPost> posts = tcmPostRepository.findByTitleLike(title);
        return posts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TcmPostDTO> getHotPosts(Integer status, Integer limit) {
        log.debug("=============== 查询浏览量最多的帖子列表");
        
        List<TcmPost> posts = tcmPostRepository.findTopPostsByViewCount(status);
        return posts.stream()
                .map(this::convertToDTO)
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TcmPostDTO> getLatestPosts(Integer status, Integer limit) {
        log.debug("查询最新帖子列表");
        
        List<TcmPost> posts = tcmPostRepository.findLatestPosts(status);
        return posts.stream()
                .map(this::convertToDTO)
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    @Autowired
    private TcmPostHistoryService tcmPostHistoryService;

    @Override
    public TcmPostDTO updatePost(Long id, TcmPost post) {
        log.info("开始更新医药论坛帖子ID：{}", id);
        
        // 验证帖子是否存在
        TcmPost existingPost = tcmPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("帖子不存在，ID：" + id));
        
        // 保存当前版本到历史记录
        tcmPostHistoryService.savePostHistory(existingPost);
        
        // 设置ID并更新
        post.setId(id);
        post.setUserId(existingPost.getUserId());
        post.setCreatedAt(existingPost.getCreatedAt());
        post.setViewCount(existingPost.getViewCount());
        // 设置为已更新
        post.setIsUpdated(1);
        TcmPost updatedPost = tcmPostRepository.save(post);
        
        log.info("医药论坛帖子更新成功：{}", updatedPost.getTitle());
        return convertToDTO(updatedPost);
    }
    
    @Override
    public void deletePost(Long id) {
        log.info("开始删除医药论坛帖子ID：{}", id);
        
        // 验证帖子是否存在
        if (!tcmPostRepository.existsById(id)) {
            throw new IllegalArgumentException("帖子不存在，ID：" + id);
        }
        
        tcmPostRepository.deleteById(id);
        log.info("医药论坛帖子删除成功：{}", id);
    }
    
    @Override
    public void batchDeletePosts(List<Long> ids) {
        log.info("开始批量删除医药论坛帖子，数量：{}", ids.size());
        
        tcmPostRepository.deleteAllById(ids);
        log.info("批量删除医药论坛帖子完成，数量：{}", ids.size());
    }
    
    @Override
    public void likePost(Long postId, Long userId) {
        log.info("用户{}对帖子{}执行点赞操作", userId, postId);
        
        // 验证帖子是否存在
        TcmPost post = tcmPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("帖子不存在，ID：" + postId));
        
        tcmPostRepository.likePost(postId);
        log.info("用户{}对帖子{}点赞成功", userId, postId);
    }
    
    @Override
    public void unlikePost(Long postId, Long userId) {
        log.info("用户{}对帖子{}执行取消点赞操作", userId, postId);
        
        tcmPostRepository.unlikePost(postId);
        log.info("用户{}对帖子{}取消点赞成功", userId, postId);
    }
    
    @Override
    public long incrementViewCount(Long id) {
        log.debug("帖子{}浏览量增加", id);
        
        tcmPostRepository.increaseViewCount(id);
        TcmPost post = tcmPostRepository.findById(id).orElseThrow();
        log.debug("帖子{}浏览量增加成功，当前浏览量：{}", id, post.getViewCount());
        return post.getViewCount();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TcmPostDTO> getAllPosts() {
        log.debug("查询所有帖子");
        
        List<TcmPost> posts = tcmPostRepository.findAll();
        return posts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<TcmPostDTO> getAllPosts(Pageable pageable, String hotpost, String isNew, String keyword) {
        log.debug("=============== Getting all posts with SQL filters - hotpost: {}, isNew: {}, keyword: {}", 
                hotpost, isNew, keyword);
        
        // 处理空参数，确保即使参数为空或空字符串，SQL查询也能正常工作
        // 如果hotpost是空字符串，则设置为null，保持一致性
        if (hotpost != null && hotpost.trim().isEmpty()) {
            hotpost = null;
        }
        
        // 如果isNew是空字符串，则设置为null，保持一致性
        if (isNew != null && isNew.trim().isEmpty()) {
            isNew = null;
        }
        isNew="1";
        // 如果keyword是空字符串，则设置为null，保持一致性
        if (keyword != null && keyword.trim().isEmpty()) {
            keyword = null;
        }
        
        // 调用Repository的动态查询方法
        Page<TcmPost> postsPage = tcmPostRepository.findByDynamicConditions(
                hotpost, isNew, keyword, pageable);
        
        // 将实体转换为DTO并返回
        return postsPage.map(this::convertToDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getPostCountByUser(Long userId) {
        return tcmPostRepository.countByUserId(userId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getPostCountByCategory(Long categoryId) {
        return tcmPostRepository.countByCategoryId(categoryId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getTotalPostCount() {
        return tcmPostRepository.count();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TcmPostDTO> searchPosts(String keyword) {
        log.debug("搜索关键词：{}", keyword);
        
        List<TcmPost> posts = tcmPostRepository.findByTitleContainingOrContentContaining(keyword, keyword);
        return posts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<TcmPostDTO> searchPosts(String keyword, Pageable pageable) {
        log.debug("=============== Searching posts with keyword: {}, pageable: {}", keyword, pageable);
        
        Page<TcmPost> postsPage = tcmPostRepository.findByTitleContainingOrContentContaining(keyword, keyword, pageable);
        return postsPage.map(this::convertToDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TcmPostDTO> searchPostsByTitle(String title) {
        log.debug("搜索标题：{}", title);
        
        List<TcmPost> posts = tcmPostRepository.findByTitleLike(title);
        return posts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TcmPostDTO> searchPostsByTitleOrContent(String keyword) {
        log.debug("搜索关键词：{}", keyword);
        
        List<TcmPost> posts = tcmPostRepository.findByTitleContainingOrContentContaining(keyword, keyword);
        return posts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isPostLikedByUser(Long postId, Long userId) {
        // 这里需要调用TcmPostLikeService来检查用户是否点赞过帖子
        // 由于当前没有依赖注入TcmPostLikeService，暂时返回false
        return false;
    }
    
    @Override
    public void decreaseReplyCount(Long postId) {
        log.debug("减少帖子{}的回复数量", postId);
        
        // 验证帖子是否存在
        if (!tcmPostRepository.existsById(postId)) {
            throw new IllegalArgumentException("帖子不存在，ID：" + postId);
        }
        
        tcmPostRepository.decreaseReplyCount(postId);
        log.debug("帖子{}回复数量减少成功", postId);
    }
    
    @Override
    public void increaseReplyCount(Long postId) {
        log.debug("=============== 增加帖子{}的回复数量", postId);
        
        // 验证帖子是否存在
        if (!tcmPostRepository.existsById(postId)) {
            throw new IllegalArgumentException("帖子不存在，ID：" + postId);
        }
        
        tcmPostRepository.increaseReplyCount(postId);
        log.debug("=============== 帖子{}回复数量增加成功", postId);
    }
    
    /**
     * 将TcmPost实体转换为TcmPostDTO
     * 
     * @param post TcmPost实体
     * @return TcmPostDTO
     */
    private TcmPostDTO convertToDTO(TcmPost post) {
        TcmPostDTO dto = new TcmPostDTO();
        dto.setId(post.getId());
        dto.setUserId(post.getUserId());
        dto.setCategoryId(post.getCategoryId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setStatus(post.getStatus());
        dto.setViewCount(post.getViewCount());
        dto.setLikeCount(post.getLikeCount());
        dto.setReplyCount(post.getReplyCount());
        dto.setCollectCount(post.getCollectCount());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());
        dto.setIsUpdated(post.getIsUpdated());
        return dto;
    }
}
