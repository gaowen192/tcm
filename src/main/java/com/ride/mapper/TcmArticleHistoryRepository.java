package com.ride.mapper;

import com.ride.entity.TcmArticleHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 医药论坛文章修改历史数据访问层
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@Repository
public interface TcmArticleHistoryRepository extends JpaRepository<TcmArticleHistory, Long> {
    
    /**
     * 根据文章ID查找所有历史记录，按版本号降序排列
     * 
     * @param articleId 文章ID
     * @return 文章历史记录列表
     */
    List<TcmArticleHistory> findByArticleIdOrderByVersionDesc(Long articleId);
    
    /**
     * 根据文章ID查找所有历史记录（分页）
     * 
     * @param articleId 文章ID
     * @param pageable 分页参数
     * @return 文章历史记录分页结果
     */
    Page<TcmArticleHistory> findByArticleId(Long articleId, Pageable pageable);
    
    /**
     * 根据文章ID和版本号查找特定历史记录
     * 
     * @param articleId 文章ID
     * @param version 版本号
     * @return 文章历史记录
     */
    TcmArticleHistory findByArticleIdAndVersion(Long articleId, Integer version);
    
    /**
     * 根据文章ID查询最新版本号
     * 
     * @param articleId 文章ID
     * @return 最新版本号，如果没有历史记录则返回null
     */
    @Query("SELECT MAX(h.version) FROM TcmArticleHistory h WHERE h.articleId = :articleId")
    Integer findLatestVersionByArticleId(@Param("articleId") Long articleId);
    
    /**
     * 根据文章ID统计历史记录数量
     * 
     * @param articleId 文章ID
     * @return 历史记录数量
     */
    long countByArticleId(Long articleId);
}