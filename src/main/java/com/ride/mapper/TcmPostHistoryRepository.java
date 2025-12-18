package com.ride.mapper;

import com.ride.entity.TcmPostHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 医药论坛帖子修改历史数据访问层
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@Repository
public interface TcmPostHistoryRepository extends JpaRepository<TcmPostHistory, Long> {
    
    /**
     * 根据帖子ID查询所有修改历史记录，按版本号降序排列
     * 
     * @param postId 帖子ID
     * @return 帖子修改历史记录列表
     */
    List<TcmPostHistory> findByPostIdOrderByVersionDesc(Long postId);
    
    /**
     * 根据帖子ID和版本号查询特定版本的历史记录
     * 
     * @param postId 帖子ID
     * @param version 版本号
     * @return 特定版本的历史记录
     */
    Optional<TcmPostHistory> findByPostIdAndVersion(Long postId, Integer version);
    
    /**
     * 查询帖子的最新版本号
     * 
     * @param postId 帖子ID
     * @return 最新版本号，如果没有历史记录则返回0
     */
    @Query("SELECT COALESCE(MAX(h.version), 0) FROM TcmPostHistory h WHERE h.postId = :postId")
    Integer findLatestVersionByPostId(@Param("postId") Long postId);
    
    /**
     * 根据帖子ID删除所有历史记录
     * 
     * @param postId 帖子ID
     */
    void deleteByPostId(Long postId);
}
