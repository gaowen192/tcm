package com.ride.service;

import com.ride.entity.TcmPostInteraction;

import java.util.List;
import java.util.Map;

/**
 * 帖子互动服务接口
 */
public interface TcmPostInteractionService {

    /**
     * 记录帖子互动（点赞或观看）
     * 重复操作时更新互动时间
     *
     * @param userId         用户ID
     * @param postId         帖子ID
     * @param interactionType 互动类型：LIKE(点赞)、WATCH(观看)
     */
    void recordPostInteraction(Long userId, Long postId, String interactionType);

    /**
     * 获取用户帖子互动历史
     *
     * @param userId 用户ID
     * @return 互动历史列表
     */
    List<Map<String, Object>> getUserPostHistory(Long userId);

    /**
     * 获取用户帖子互动历史（分页）
     *
     * @param userId   用户ID
     * @param page     页码（从1开始）
     * @param pageSize 每页条数
     * @return 分页后的互动历史数据
     */
    Map<String, Object> getUserPostHistoryWithPagination(Long userId, Integer page, Integer pageSize);
}
