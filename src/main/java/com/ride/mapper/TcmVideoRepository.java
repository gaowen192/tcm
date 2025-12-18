package com.ride.mapper;

import com.ride.entity.TcmVideo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 医药论坛视频数据访问层
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@Repository
public interface TcmVideoRepository extends JpaRepository<TcmVideo, Long> {
    
    /**
     * 根据用户ID查找视频列表
     * 
     * @param userId 用户ID
     * @return 视频列表
     */
    List<TcmVideo> findByUserId(Long userId);
    
    /**
     * 根据用户ID查找视频列表（分页）
     * 
     * @param userId 用户ID
     * @param pageable 分页参数
     * @return 视频分页结果
     */
    Page<TcmVideo> findByUserId(Long userId, Pageable pageable);
    
    /**
     * 根据板块ID查找视频列表
     * 
     * @param categoryId 板块ID
     * @return 视频列表
     */
    List<TcmVideo> findByCategoryId(Long categoryId);
    
    /**
     * 根据板块ID查找视频列表，包含用户名称
     * 使用原生SQL进行关联查询
     * 
     * @param categoryId 板块ID
     * @return 视频和用户信息列表
     */
    @Query(value = "SELECT v.*, u.username AS userName " +
                   "FROM tcm_videos v " +
                   "LEFT JOIN tcm_users u ON v.user_id = u.id " +
                   "WHERE v.category_id = :categoryId", 
           nativeQuery = true)
    List<Object[]> findVideosWithUserNameByCategoryId(@Param("categoryId") Long categoryId);
    
    /**
     * 根据状态查找视频列表
     * 
     * @param status 状态
     * @return 视频列表
     */
    List<TcmVideo> findByStatus(Integer status);
    
    /**
     * 根据状态查找视频列表（分页）
     * 
     * @param status 状态
     * @param pageable 分页参数
     * @return 视频分页结果
     */
    Page<TcmVideo> findByStatus(Integer status, Pageable pageable);
    
    /**
     * 根据用户ID和状态查找视频列表
     * 
     * @param userId 用户ID
     * @param status 状态
     * @return 视频列表
     */
    List<TcmVideo> findByUserIdAndStatus(Long userId, Integer status);
    
    /**
     * 根据板块ID和状态查找视频列表，按创建时间倒序
     * 
     * @param categoryId 板块ID
     * @param status 状态
     * @return 视频列表
     */
    List<TcmVideo> findByCategoryIdAndStatusOrderByCreatedAtDesc(Long categoryId, Integer status);
    
    /**
     * 根据标题模糊查询
     * 
     * @param title 标题关键字
     * @return 视频列表
     */
    @Query("SELECT v FROM TcmVideo v WHERE v.title LIKE CONCAT('%', :title, '%') AND v.status = 1")
    List<TcmVideo> findByTitleLike(@Param("title") String title);
    
    /**
     * 根据标签查询视频
     * 
     * @param tag 标签
     * @return 视频列表
     */
    @Query("SELECT v FROM TcmVideo v WHERE v.tags LIKE CONCAT('%', :tag, '%') AND v.status = 1")
    List<TcmVideo> findByTag(@Param("tag") String tag);
    
    /**
     * 获取热门视频列表
     * 
     * @param limit 限制数量
     * @return 热门视频列表
     */
    @Query("SELECT v FROM TcmVideo v WHERE v.status = 1 ORDER BY v.viewCount DESC")
    List<TcmVideo> findHotVideos(@Param("limit") int limit);
    
    /**
     * 获取推荐视频列表
     * 
     * @return 推荐视频列表
     */
    List<TcmVideo> findByIsRecommendedTrueAndStatus(Integer status);
    
    /**
     * 获取最新视频列表
     * 
     * @param limit 限制数量
     * @return 最新视频列表
     */
    @Query("SELECT v FROM TcmVideo v WHERE v.status = 1 ORDER BY v.createdAt DESC")
    List<TcmVideo> findLatestVideos(@Param("limit") int limit);
    
    /**
     * 统计用户视频数量
     * 
     * @param userId 用户ID
     * @return 视频数量
     */
    long countByUserId(Long userId);
    
    /**
     * 统计板块视频数量
     * 
     * @param categoryId 板块ID
     * @return 视频数量
     */
    long countByCategoryId(Long categoryId);
    
    /**
     * 增加观看次数
     * 
     * @param id 视频ID
     */
    @Modifying
    @Query("UPDATE TcmVideo v SET v.viewCount = v.viewCount + 1 WHERE v.id = :id")
    void incrementViewCount(@Param("id") Long id);
    
    /**
     * 增加点赞次数
     * 
     * @param id 视频ID
     */
    @Modifying
    @Query("UPDATE TcmVideo v SET v.likeCount = v.likeCount + 1 WHERE v.id = :id")
    void incrementLikeCount(@Param("id") Long id);
    
    /**
     * 减少点赞次数
     * 
     * @param id 视频ID
     */
    @Modifying
    @Query("UPDATE TcmVideo v SET v.likeCount = v.likeCount - 1 WHERE v.id = :id AND v.likeCount > 0")
    void decrementLikeCount(@Param("id") Long id);
    
    /**
     * 增加评论次数
     * 
     * @param id 视频ID
     */
    @Modifying
    @Query("UPDATE TcmVideo v SET v.commentCount = v.commentCount + 1 WHERE v.id = :id")
    void incrementCommentCount(@Param("id") Long id);
    
    /**
     * 减少评论次数
     * 
     * @param id 视频ID
     */
    @Modifying
    @Query("UPDATE TcmVideo v SET v.commentCount = v.commentCount - 1 WHERE v.id = :id AND v.commentCount > 0")
    void decrementCommentCount(@Param("id") Long id);
    
    /**
     * 增加下载次数
     * 
     * @param id 视频ID
     */
    @Modifying
    @Query("UPDATE TcmVideo v SET v.downloadCount = v.downloadCount + 1 WHERE v.id = :id")
    void incrementDownloadCount(@Param("id") Long id);
    
    /**
     * 批量更新热门状态
     * 
     * @param ids 视频ID列表
     * @param isHot 是否热门
     */
    @Modifying
    @Query("UPDATE TcmVideo v SET v.isHot = :isHot WHERE v.id IN :ids")
    void updateHotStatus(@Param("ids") List<Long> ids, @Param("isHot") Boolean isHot);
    
    /**
     * 批量更新推荐状态
     * 
     * @param ids 视频ID列表
     * @param isRecommended 是否推荐
     */
    @Modifying
    @Query("UPDATE TcmVideo v SET v.isRecommended = :isRecommended WHERE v.id IN :ids")
    void updateRecommendedStatus(@Param("ids") List<Long> ids, @Param("isRecommended") Boolean isRecommended);
    
    /**
     * 按视频格式分组统计数量
     * 
     * @return 格式统计结果
     */
    @Query("SELECT v.format, COUNT(v) FROM TcmVideo v WHERE v.status = 1 GROUP BY v.format")
    List<Object[]> countByFormat();
    
    /**
     * 按分辨率分组统计数量
     * 
     * @return 分辨率统计结果
     */
    @Query("SELECT v.resolution, COUNT(v) FROM TcmVideo v WHERE v.status = 1 GROUP BY v.resolution")
    List<Object[]> countByResolution();
    
    /**
     * 获取视频总大小
     * 
     * @return 总大小（字节）
     */
    @Query("SELECT SUM(v.fileSize) FROM TcmVideo v WHERE v.status = 1")
    Long getTotalSize();
    
    /**
     * 根据视频时长范围查询
     * 
     * @param minDuration 最小时长
     * @param maxDuration 最大时长
     * @return 视频列表
     */
    List<TcmVideo> findByDurationBetweenAndStatusOrderByCreatedAtDesc(Integer minDuration, Integer maxDuration, Integer status);
}