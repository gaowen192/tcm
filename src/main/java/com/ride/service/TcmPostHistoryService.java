package com.ride.service;

import com.ride.entity.TcmPost;
import com.ride.entity.TcmPostHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 帖子修改历史业务逻辑接口
 *
 * @author 千行团队
 * @version 1.0.0
 */
public interface TcmPostHistoryService {

    /**
     * 保存帖子修改历史
     *
     * @param post 帖子实体
     * @return 保存后的历史记录
     */
    TcmPostHistory savePostHistory(TcmPost post);

    /**
     * 根据帖子ID查询所有修改历史
     *
     * @param postId 帖子ID
     * @return 历史记录列表
     */
    List<TcmPostHistory> getPostHistoriesByPostId(Long postId);

    /**
     * 根据帖子ID分页查询修改历史
     *
     * @param postId   帖子ID
     * @param pageable 分页参数
     * @return 分页历史记录
     */
    Page<TcmPostHistory> getPostHistoriesByPostId(Long postId, Pageable pageable);

    /**
     * 根据帖子ID和版本号查询历史记录
     *
     * @param postId 帖子ID
     * @param version 版本号
     * @return 历史记录
     */
    TcmPostHistory getPostHistoryByPostIdAndVersion(Long postId, Integer version);

    /**
     * 获取帖子的最新版本历史记录
     *
     * @param postId 帖子ID
     * @return 最新历史记录
     */
    TcmPostHistory getLatestPostHistory(Long postId);

    /**
     * 根据帖子ID删除所有历史记录
     *
     * @param postId 帖子ID
     */
    void deletePostHistoriesByPostId(Long postId);

    /**
     * 恢复帖子到指定版本
     *
     * @param postId 帖子ID
     * @param version 版本号
     * @return 恢复后的帖子实体
     */
    TcmPost restorePostFromHistory(Long postId, Integer version);
}