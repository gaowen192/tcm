package com.ride.service.impl;

import com.ride.entity.TcmPost;
import com.ride.entity.TcmPostHistory;
import com.ride.mapper.TcmPostHistoryRepository;
import com.ride.mapper.TcmPostRepository;
import com.ride.service.TcmPostHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 帖子修改历史业务逻辑实现类
 *
 * @author 千行团队
 * @version 1.0.0
 */
@Service
@Transactional
public class TcmPostHistoryServiceImpl implements TcmPostHistoryService {

    private static final Logger log = LoggerFactory.getLogger(TcmPostHistoryServiceImpl.class);

    @Autowired
    private TcmPostHistoryRepository tcmPostHistoryRepository;

    @Autowired
    private TcmPostRepository tcmPostRepository;

    @Override
    public TcmPostHistory savePostHistory(TcmPost post) {
        log.info("=============== 开始保存帖子修改历史，帖子ID：{}", post.getId());

        // 获取当前帖子的最新版本号，计算新版本号
        Integer latestVersion = tcmPostHistoryRepository.findLatestVersionByPostId(post.getId());
        Integer newVersion = latestVersion + 1;

        // 创建历史记录
        TcmPostHistory history = new TcmPostHistory(post, newVersion);
        TcmPostHistory savedHistory = tcmPostHistoryRepository.save(history);

        log.info("=============== 帖子修改历史保存成功，帖子ID：{}，版本号：{}", post.getId(), newVersion);
        return savedHistory;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TcmPostHistory> getPostHistoriesByPostId(Long postId) {
        log.debug("查询帖子ID的修改历史列表：{}", postId);
        return tcmPostHistoryRepository.findByPostIdOrderByVersionDesc(postId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TcmPostHistory> getPostHistoriesByPostId(Long postId, Pageable pageable) {
        // 先查询所有历史记录，再手动分页
        List<TcmPostHistory> histories = tcmPostHistoryRepository.findByPostIdOrderByVersionDesc(postId);
        // 手动分页
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), histories.size());
        List<TcmPostHistory> pagedHistories = histories.subList(start, end);
        // 创建Page对象返回
        return new org.springframework.data.domain.PageImpl<>(pagedHistories, pageable, histories.size());
    }

    @Override
    @Transactional(readOnly = true)
    public TcmPostHistory getPostHistoryByPostIdAndVersion(Long postId, Integer version) {
        log.debug("查询帖子ID：{}，版本号：{}的修改历史", postId, version);
        return tcmPostHistoryRepository.findByPostIdAndVersion(postId, version)
                .orElseThrow(() -> new IllegalArgumentException("帖子历史记录不存在，帖子ID：" + postId + "，版本号：" + version));
    }

    @Override
    @Transactional(readOnly = true)
    public TcmPostHistory getLatestPostHistory(Long postId) {
        log.debug("查询帖子ID：{}的最新修改历史", postId);
        List<TcmPostHistory> histories = tcmPostHistoryRepository.findByPostIdOrderByVersionDesc(postId);
        if (histories.isEmpty()) {
            throw new IllegalArgumentException("帖子历史记录不存在，帖子ID：" + postId);
        }
        return histories.get(0);
    }

    @Override
    public void deletePostHistoriesByPostId(Long postId) {
        log.info("开始删除帖子ID：{}的所有修改历史", postId);
        tcmPostHistoryRepository.deleteByPostId(postId);
        log.info("帖子ID：{}的所有修改历史删除成功", postId);
    }

    @Override
    public TcmPost restorePostFromHistory(Long postId, Integer version) {
        log.info("开始从历史记录恢复帖子，帖子ID：{}，版本号：{}", postId, version);

        // 获取指定版本的历史记录
        TcmPostHistory history = getPostHistoryByPostIdAndVersion(postId, version);

        // 获取当前帖子
        TcmPost currentPost = tcmPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("帖子不存在，ID：" + postId));

        // 保存当前版本到历史记录
        savePostHistory(currentPost);

        // 从历史记录恢复帖子
        currentPost.setTitle(history.getTitle());
        currentPost.setContent(history.getContent());
        currentPost.setStatus(history.getStatus());
        currentPost.setUpdatedAt(history.getUpdatedAt());
        currentPost.setLikeCount(history.getLikeCount());
        currentPost.setReplyCount(history.getReplyCount());
        currentPost.setCollectCount(history.getCollectCount());
        currentPost.setIsTop(history.getIsTop());
        currentPost.setIsEssence(history.getIsEssence());
        currentPost.setIsHot(history.getIsHot());
        
        // 根据TcmPost实体的实际字段名设置值
        try {
            // 反射获取方法，确保与TcmPost实体的字段名一致
            if (history.getClass().getMethod("getSummary") != null) {
                // 检查TcmPost是否有setSummary方法
                if (currentPost.getClass().getMethod("setSummary", String.class) != null) {
                    currentPost.getClass().getMethod("setSummary", String.class).invoke(currentPost, history.getSummary());
                }
            }
            if (history.getClass().getMethod("getTags") != null) {
                // 检查TcmPost是否有setTags方法
                if (currentPost.getClass().getMethod("setTags", String.class) != null) {
                    currentPost.getClass().getMethod("setTags", String.class).invoke(currentPost, history.getTags());
                }
            }
        } catch (Exception e) {
            log.debug("=============== 恢复帖子时某些字段不存在，跳过: {}", e.getMessage());
        }

        TcmPost restoredPost = tcmPostRepository.save(currentPost);

        log.info("=============== 帖子恢复成功，帖子ID：{}，恢复到版本号：{}", postId, version);
        return restoredPost;
    }
}